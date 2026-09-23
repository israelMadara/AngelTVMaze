package com.example.maze.demo.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maze.demo.service.CacheService;
import com.example.maze.demo.service.CommentsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/shows")
@Tag(name = "Shows", description = "Búsqueda e información de shows (TV Maze middleware)")
public class ShowController {

	private final CacheService cacheService;
	private final CommentsService commentsService;

	public ShowController(CacheService cacheService, CommentsService commentsService) {
		this.cacheService = cacheService;
		this.commentsService = commentsService;
	}

	/**
	 * GET /api/shows/{id}
	 * Cache-aside + arreglo comments [{comment, rating}].
	 */
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Obtener show por ID", description = "Show con caché MongoDB e incluye comentarios")
	public Map<String, Object> getShowById(
			@Parameter(description = "ID del show en TV Maze", required = true, example = "82")
			@PathVariable("id") Long id) {
		Map<String, Object> show = cacheService.getShowWithCache(id);
		Map<String, Object> response = new LinkedHashMap<String, Object>(show);
		response.put("comments", commentsService.findCommentItemsByShowId(id));
		return response;
	}
}
