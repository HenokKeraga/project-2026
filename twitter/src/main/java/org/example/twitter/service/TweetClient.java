package org.example.twitter.service;


import org.example.twitter.dto.TweetDto;
import org.example.twitter.model.Tweet;
import org.example.twitter.repository.TweetRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TweetClient {

    private final TweetRepository tweetRepository;

    public TweetClient(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    public List<TweetDto> getTweets(List<Long> tweetIds) {
        if (tweetIds == null || tweetIds.isEmpty()) {
            return List.of();
        }

        // 1. Load all tweets from DB in one query
        List<Tweet> tweets = tweetRepository.findAllById(tweetIds);

        // 2. Index by id for fast lookup
        Map<Long, Tweet> tweetById = tweets.stream()
                .collect(Collectors.toMap(Tweet::getId, Function.identity()));

        // 3. Preserve order of tweetIds in the result
        List<TweetDto> result = new ArrayList<>(tweetIds.size());
        for (Long id : tweetIds) {
            Tweet tweet = tweetById.get(id);
            if (tweet != null) {
                result.add(TweetDto.from(tweet));
            }
        }

        return result;
    }


}
