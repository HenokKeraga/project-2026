package org.example.twitter.controller;

import org.example.twitter.dto.TweetDto;
import org.example.twitter.service.TweetClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeline")
public class TimelineController {

    private final RedisTemplate<String, Long> redisTemplate; // timeline IDs
    private final TweetClient tweetClient;                   // local Redis client

    public TimelineController(RedisTemplate<String, Long> redisTemplate,
                              TweetClient tweetClient) {
        this.redisTemplate = redisTemplate;
        this.tweetClient = tweetClient;
    }

    @GetMapping("/{userId}")
    public List<TweetDto> getTimeline(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {

        String key = "timeline:" + userId;
        long start = (long) page * size;
        long end = start + size - 1;

        List<Long> tweetIds = redisTemplate.opsForList().range(key, start, end);
        if (tweetIds == null || tweetIds.isEmpty()) {
            return List.of();
        }

        return tweetClient.getTweets(tweetIds); // now reads from local Redis
    }
}

