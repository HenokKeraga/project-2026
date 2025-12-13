package org.example.doc.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "doc_operations",
        indexes = {
                @Index(name = "idx_ops_doc_version", columnList = "docId,version", unique = true),
                @Index(name = "idx_ops_doc", columnList = "docId")
        })
public class DocOperation {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID docId;

    @Column(nullable = false)
    private long version;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String clientOpId;

    @Column(nullable = false)
    private String type; // INSERT, DELETE

    @Column(nullable = false)
    private int pos;

    @Column(nullable = false)
    private int length; // for DELETE

    @Column(nullable = false)
    private String text; // for INSERT

    @Column(nullable = false)
    private Instant createdAt;

    protected DocOperation() {}

    public DocOperation(UUID id, UUID docId, long version, String userId, String clientOpId,
                        String type, int pos, int length, String text) {
        this.id = id;
        this.docId = docId;
        this.version = version;
        this.userId = userId;
        this.clientOpId = clientOpId;
        this.type = type;
        this.pos = pos;
        this.length = length;
        this.text = text == null ? "" : text;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getDocId() { return docId; }
    public long getVersion() { return version; }
    public String getUserId() { return userId; }
    public String getClientOpId() { return clientOpId; }
    public String getType() { return type; }
    public int getPos() { return pos; }
    public int getLength() { return length; }
    public String getText() { return text; }
    public Instant getCreatedAt() { return createdAt; }
}
