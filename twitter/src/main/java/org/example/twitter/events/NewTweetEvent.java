package org.example.twitter.events;

import java.io.Serializable;
import java.time.Instant;

// TweetService/src/main/java/com/example/tweetservice/events/NewTweetEvent.java
public class NewTweetEvent  {
    private Long tweetId;
    private Long authorId;
    private Instant createdAt;
    // constructor, getters/setters


    public NewTweetEvent() {
    }

    public NewTweetEvent(Long tweetId, Long authorId, Instant createdAt) {
        this.tweetId = tweetId;
        this.authorId = authorId;
        this.createdAt = createdAt;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
