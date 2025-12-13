package org.example.twitter.model;


import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FollowId implements Serializable {

    private Long followerId;
    private Long followeeId;

    public FollowId() {}

    public FollowId(Long followerId, Long followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }
}

