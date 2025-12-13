package org.example.twitter.dto;

import org.example.twitter.model.Tweet;

import java.time.Instant;

public class TweetDto {

    private Long id;
    private Long userId;
    private String text;
    private Instant createdAt;

    public TweetDto() {}

    public TweetDto(Long id, Long userId, String text, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.createdAt = createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // Static mapper from Entity → DTO
    public static TweetDto from(Tweet tweet) {
        return new TweetDto(
                tweet.getId(),
                tweet.getUserId(),
                tweet.getText(),
                tweet.getCreatedAt()
        );
    }

}
