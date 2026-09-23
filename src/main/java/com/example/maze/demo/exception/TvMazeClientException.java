package com.example.maze.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class TvMazeClientException extends RuntimeException {

	public TvMazeClientException(String message) {
		super(message);
	}

	public TvMazeClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
