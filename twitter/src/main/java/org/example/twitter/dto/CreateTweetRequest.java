package org.example.twitter.dto;

// TweetService/src/main/java/com/example/tweetservice/api/CreateTweetRequest.java
public class CreateTweetRequest {
    private Long userId;
    private String text;
    // getters/setters


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

