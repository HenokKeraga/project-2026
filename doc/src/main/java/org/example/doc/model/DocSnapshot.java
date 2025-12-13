package org.example.doc.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "doc_snapshots",
        indexes = @Index(name = "idx_snap_doc_version", columnList = "docId,version", unique = true))
public class DocSnapshot {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID docId;

    @Column(nullable = false)
    private long version;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Instant createdAt;

    protected DocSnapshot() {}

    public DocSnapshot(UUID id, UUID docId, long version, String content) {
        this.id = id;
        this.docId = docId;
        this.version = version;
        this.content = content;
        this.createdAt = Instant.now();
    }
}

