package org.example.twitter.repository;


import org.example.twitter.model.Follow;
import org.example.twitter.model.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    // When author tweets → find all followers
    @Query("SELECT f.id.followerId FROM Follow f WHERE f.id.followeeId = :followeeId")
    List<Long> findFollowerIdsByFolloweeId(Long followeeId);

    // Optional: Who does user follow?
    @Query("SELECT f.id.followeeId FROM Follow f WHERE f.id.followerId = :followerId")
    List<Long> findFolloweeIdsByFollowerId(Long followerId);
}
