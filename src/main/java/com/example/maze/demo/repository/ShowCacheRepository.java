package com.example.maze.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.maze.demo.model.ShowCache;

public interface ShowCacheRepository extends MongoRepository<ShowCache, String> {

	Optional<ShowCache> findByShowId(Long showId);

	boolean existsByShowId(Long showId);
}
