package com.example.maze.demo.dto;

public class CommentResponse {

	private int status;
	private String id;
	private String message;

	public CommentResponse() {
	}

	public CommentResponse(int status, String id, String message) {
		this.status = status;
		this.id = id;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
