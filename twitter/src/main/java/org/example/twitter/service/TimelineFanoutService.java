package org.example.twitter.service;

import org.example.twitter.events.NewTweetEvent;
import org.example.twitter.repository.FollowRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TimelineFanoutService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final FollowRepository followRepository; // from Social Graph DB

    public TimelineFanoutService(RedisTemplate<String, Long> redisTemplate,
                                 FollowRepository followRepository) {
        this.redisTemplate = redisTemplate;
        this.followRepository = followRepository;
    }

    @KafkaListener(topics = "tweets.new", groupId = "timeline-service")
    public void handleNewTweet(NewTweetEvent event) {
        Long authorId = event.getAuthorId();
        Long tweetId = event.getTweetId();

        // 1. Find all followers of this author
        List<Long> followerIds = followRepository.findFollowerIdsByFolloweeId(authorId);

        // 2. For each follower, push the tweetId into their Redis timeline
        for (Long followerId : followerIds) {
            String key = "timeline:" + followerId;
            // LPUSH to keep newest first; also trim list to max size (e.g. 1000)
            redisTemplate.opsForList().leftPush(key, tweetId);
            redisTemplate.opsForList().trim(key, 0, 999);
        }
    }
}
