package com.example.maze.demo.dto;

public class CommentItemDTO {

	private String comment;
	private Integer rating;

	public CommentItemDTO() {
	}

	public CommentItemDTO(String comment, Integer rating) {
		this.comment = comment;
		this.rating = rating;
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
}
