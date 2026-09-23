package com.example.maze.demo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.maze.demo.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {

	List<Comment> findByShowId(Long showId);

	List<Comment> findByShowIdOrderByCreatedAtDesc(Long showId);

	List<Comment> findByShowIdIn(Collection<Long> showIds);
}
