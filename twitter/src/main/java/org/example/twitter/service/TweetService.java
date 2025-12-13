package org.example.twitter.service;

import org.example.twitter.events.NewTweetEvent;
import org.example.twitter.model.Tweet;
import org.example.twitter.dto.TweetDto;
import org.example.twitter.repository.TweetRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

// TweetService/src/main/java/com/example/tweetservice/service/TweetService.java
@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final KafkaTemplate<String, NewTweetEvent> kafkaTemplate;
    private final RedisTemplate<String, TweetDto> tweetRedisTemplate;

    public TweetService(TweetRepository tweetRepository,
                        KafkaTemplate<String, NewTweetEvent> kafkaTemplate, RedisTemplate<String, TweetDto> tweetRedisTemplate) {
        this.tweetRepository = tweetRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.tweetRedisTemplate = tweetRedisTemplate;
    }

    @Transactional
    public TweetDto createTweet(Long userId, String text) {
        Tweet tweet = new Tweet();
        tweet.setUserId(userId);
        tweet.setText(text);
        tweet.setCreatedAt(Instant.now());
//        // ✅ 1) cache tweet in Redis
//        String cacheKey = "tweet:" + tweet.getId();
//
//        TweetDto dto = new TweetDto(
//                tweet.getId(),
//                tweet.getUserId(),
//                tweet.getText(),
//                tweet.getCreatedAt()
//        );
//        tweetRedisTemplate.opsForValue().set(cacheKey, dto);

        Tweet saved = tweetRepository.save(tweet);

        NewTweetEvent event = new NewTweetEvent(saved.getId(), saved.getUserId(), saved.getCreatedAt());

        kafkaTemplate.send("tweets.new", Long.toString(saved.getId()), event);

        return TweetDto.from(saved);
    }

    public Optional<TweetDto> getTweet(Long id) {
        return tweetRepository.findById(id).map(TweetDto::from);
    }
}
