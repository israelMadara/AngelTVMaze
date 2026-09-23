package com.example.maze.demo.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Comentario y calificación ligados a un show (colección comments).
 */
@Document(collection = "comments")
public class Comment {

	@Id
	private String id;

	@Indexed
	private Long showId;

	private String comment;

	/** Rating 0-5 */
	private Integer rating;

	private Date createdAt;

	public Comment() {
	}

	public Comment(Long showId, String comment, Integer rating, Date createdAt) {
		this.showId = showId;
		this.comment = comment;
		this.rating = rating;
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
