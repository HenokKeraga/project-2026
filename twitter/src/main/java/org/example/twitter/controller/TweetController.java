package org.example.twitter.controller;

import org.example.twitter.dto.CreateTweetRequest;
import org.example.twitter.dto.TweetDto;
import org.example.twitter.service.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TweetService/src/main/java/com/example/tweetservice/api/TweetController.java
@RestController
@RequestMapping("/tweets")
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity<TweetDto> createTweet(@RequestBody CreateTweetRequest request) {
        TweetDto tweet = tweetService.createTweet(request.getUserId(), request.getText());
        return ResponseEntity.status(HttpStatus.CREATED).body(tweet);
    }

}

