package com.example.maze.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShowNotFoundException extends RuntimeException {

	public ShowNotFoundException(Long showId) {
		super("Show no encontrado con id: " + showId);
	}
}
