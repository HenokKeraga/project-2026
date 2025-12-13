package org.example.doc.model;


import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private long latestVersion;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Version
    private long rowVersion;

    protected Document() {}

    public Document(UUID id, String title) {
        this.id = id;
        this.title = title;
        this.latestVersion = 0;
        this.content = "";
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public long getLatestVersion() { return latestVersion; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void applyNewState(String newContent, long newVersion) {
        this.content = newContent;
        this.latestVersion = newVersion;
        this.updatedAt = Instant.now();
    }
}

