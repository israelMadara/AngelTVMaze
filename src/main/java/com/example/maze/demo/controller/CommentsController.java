package com.example.maze.demo.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maze.demo.dto.CommentRequest;
import com.example.maze.demo.dto.CommentResponse;
import com.example.maze.demo.model.Comment;
import com.example.maze.demo.service.CommentsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Calificaciones y comentarios de shows")
public class CommentsController {

	private final CommentsService commentsService;

	public CommentsController(CommentsService commentsService) {
		this.commentsService = commentsService;
	}

	/**
	 * POST /api/comments
	 * Body: show_id, comment, rating (0-5)
	 * Retorna status 201 con id del comentario.
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Guardar comentario", description = "Guarda calificación y comentario ligados a un show_id")
	public ResponseEntity<CommentResponse> create(@Valid @RequestBody CommentRequest request) {
		Comment saved = commentsService.saveComment(request);
		CommentResponse body = new CommentResponse(
				HttpStatus.CREATED.value(),
				saved.getId(),
				"Comentario guardado correctamente");
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}
}
