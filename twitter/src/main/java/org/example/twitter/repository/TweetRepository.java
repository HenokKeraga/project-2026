package org.example.twitter.repository;

import org.example.twitter.model.Tweet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// TweetService/src/main/java/com/example/tweetservice/repository/TweetRepository.java
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
