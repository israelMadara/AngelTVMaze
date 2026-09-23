package com.example.maze.demo.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.maze.demo.dto.ShowDTO;
import com.example.maze.demo.service.CommentsService;
import com.example.maze.demo.service.TVMazeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/shows")
@Validated
@Tag(name = "Shows", description = "Búsqueda e información de shows (TV Maze middleware)")
public class SearchController {

	private final TVMazeService tvMazeService;
	private final CommentsService commentsService;

	public SearchController(TVMazeService tvMazeService, CommentsService commentsService) {
		this.tvMazeService = tvMazeService;
		this.commentsService = commentsService;
	}

	/**
	 * GET /api/shows/search?q={query}
	 * Retorna shows + arreglo comments [{comment, rating}] por cada show.
	 */
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Buscar shows", description = "Busca shows en TV Maze e incluye comentarios de MongoDB")
	public List<ShowDTO> search(
			@Parameter(description = "Criterio de búsqueda (search_query)", required = true, example = "girls")
			@RequestParam("q")
			@NotBlank(message = "El parámetro q no puede estar vacío")
			String q) {
		List<ShowDTO> shows = tvMazeService.searchShows(q);
		commentsService.enrichShowsWithComments(shows);
		return shows;
	}
}
