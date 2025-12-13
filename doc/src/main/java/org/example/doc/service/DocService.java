package org.example.doc.service;


import org.example.doc.dto.ClientOpMessage;
import org.example.doc.dto.DocResponse;
import org.example.doc.dto.OpPayload;
import org.example.doc.dto.ServerOpMessage;
import org.example.doc.model.DocOperation;
import org.example.doc.model.DocSnapshot;
import org.example.doc.model.Document;
import org.example.doc.repository.DocOperationRepo;
import org.example.doc.repository.DocSnapshotRepo;
import org.example.doc.repository.DocumentRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DocService {

    private static final int SNAPSHOT_EVERY = 20;

    private final DocumentRepo documentRepo;
    private final DocOperationRepo opRepo;
    private final DocSnapshotRepo snapshotRepo;

    public DocService(DocumentRepo documentRepo, DocOperationRepo opRepo, DocSnapshotRepo snapshotRepo) {
        this.documentRepo = documentRepo;
        this.opRepo = opRepo;
        this.snapshotRepo = snapshotRepo;
    }

    @Transactional
    public DocResponse createDoc(String title) {
        UUID id = UUID.randomUUID();
        Document doc = new Document(id, title);
        documentRepo.save(doc);
        snapshotRepo.save(new DocSnapshot(UUID.randomUUID(), id, 0, ""));
        return new DocResponse(doc.getId(), doc.getTitle(), doc.getLatestVersion(), doc.getContent());
    }

    @Transactional(readOnly = true)
    public DocResponse getDoc(UUID docId) {
        Document doc = documentRepo.findById(docId).orElseThrow(() -> new IllegalArgumentException("Doc not found"));
        return new DocResponse(doc.getId(), doc.getTitle(), doc.getLatestVersion(), doc.getContent());
    }

    @Transactional(readOnly = true)
    public List<DocOperation> getOpsSince(UUID docId, long fromVersion) {
        return opRepo.findByDocIdAndVersionGreaterThanOrderByVersionAsc(docId, fromVersion);
    }

    @Transactional
    public ServerOpMessage applyOp(UUID docId, ClientOpMessage msg) {
        // idempotency
        if (opRepo.existsByDocIdAndClientOpId(docId, msg.clientOpId())) {
            Document doc = documentRepo.findById(docId).orElseThrow(() -> new IllegalArgumentException("Doc not found"));
            return new ServerOpMessage(docId, doc.getLatestVersion(), msg.userId(), msg.clientOpId(), msg.op(), null);
        }

        Document doc = documentRepo.findByIdForUpdate(docId).orElseThrow(() -> new IllegalArgumentException("Doc not found"));

        // MVP rule: require baseVersion == latestVersion
        if (msg.baseVersion() != doc.getLatestVersion()) {
            return new ServerOpMessage(
                    docId,
                    doc.getLatestVersion(),
                    msg.userId(),
                    msg.clientOpId(),
                    msg.op(),
                    "VERSION_MISMATCH: latestVersion=" + doc.getLatestVersion()
            );
        }

        String newContent = apply(doc.getContent(), msg.op());
        long newVersion = doc.getLatestVersion() + 1;

        DocOperation op = new DocOperation(
                UUID.randomUUID(),
                docId,
                newVersion,
                msg.userId(),
                msg.clientOpId(),
                msg.op().type().toUpperCase(),
                msg.op().pos(),
                msg.op().length(),
                msg.op().text()
        );

        opRepo.save(op);
        doc.applyNewState(newContent, newVersion);
        documentRepo.save(doc);

        if (newVersion % SNAPSHOT_EVERY == 0) {
            snapshotRepo.save(new DocSnapshot(UUID.randomUUID(), docId, newVersion, newContent));
        }

        return new ServerOpMessage(docId, newVersion, msg.userId(), msg.clientOpId(), msg.op(), null);
    }

    private String apply(String content, OpPayload op) {
        String type = op.type().toUpperCase();

        if (type.equals("INSERT")) {
            String text = op.text() == null ? "" : op.text();
            int pos = clamp(op.pos(), 0, content.length());
            return content.substring(0, pos) + text + content.substring(pos);
        }

        if (type.equals("DELETE")) {
            int pos = clamp(op.pos(), 0, content.length());
            int len = Math.max(0, op.length());
            int end = clamp(pos + len, 0, content.length());
            return content.substring(0, pos) + content.substring(end);
        }

        throw new IllegalArgumentException("Unknown op type: " + op.type());
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
