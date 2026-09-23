package com.example.maze.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.maze.demo.dto.ShowDTO;
import com.example.maze.demo.dto.tvmaze.TvMazeSearchResult;
import com.example.maze.demo.exception.ShowNotFoundException;
import com.example.maze.demo.exception.TvMazeClientException;
import com.example.maze.demo.mapper.ShowMapper;

import reactor.core.publisher.Mono;

@Service
public class TVMazeService {

	private static final Logger log = LoggerFactory.getLogger(TVMazeService.class);

	private final WebClient tvMazeWebClient;
	private final ShowMapper showMapper;

	public TVMazeService(WebClient tvMazeWebClient, ShowMapper showMapper) {
		this.tvMazeWebClient = tvMazeWebClient;
		this.showMapper = showMapper;
	}

	/**
	 * Busca shows en TV Maze y los mapea al formato de respuesta del middleware.
	 *
	 * @param searchQuery criterio de búsqueda (requerido, no vacío)
	 * @return lista de ShowDTO; lista vacía si no hay resultados
	 */
	public List<ShowDTO> searchShows(String searchQuery) {
		if (!StringUtils.hasText(searchQuery)) {
			throw new IllegalArgumentException("El parámetro q (search_query) es obligatorio y no puede estar vacío");
		}

		String query = searchQuery.trim();
		log.info("Buscando shows en TV Maze con query='{}'", query);

		try {
			List<TvMazeSearchResult> results = tvMazeWebClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/search/shows")
							.queryParam("q", query)
							.build())
					.retrieve()
					.onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
							.defaultIfEmpty("")
							.flatMap(body -> Mono.error(new TvMazeClientException(
									"Error al consultar TV Maze (HTTP " + response.statusCode().value() + "): " + body))))
					.bodyToMono(new ParameterizedTypeReference<List<TvMazeSearchResult>>() {
					})
					.block();

			if (results == null) {
				return Collections.emptyList();
			}

			List<ShowDTO> shows = showMapper.toShowDtoList(results);
			log.info("TV Maze retornó {} resultado(s) para query='{}'", shows.size(), query);
			return shows;

		} catch (TvMazeClientException ex) {
			throw ex;
		} catch (WebClientResponseException ex) {
			throw new TvMazeClientException(
					"Error al consultar TV Maze (HTTP " + ex.getRawStatusCode() + "): " + ex.getResponseBodyAsString(),
					ex);
		} catch (Exception ex) {
			throw new TvMazeClientException("No se pudo comunicar con TV Maze API: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Obtiene el show completo desde TV Maze por ID.
	 *
	 * @param showId ID del show (debe ser positivo)
	 * @return objeto show completo tal como lo retorna TV Maze
	 * @throws ShowNotFoundException si el show no existe (HTTP 404)
	 */
	public Map<String, Object> getShowById(Long showId) {
		if (showId == null || showId <= 0) {
			throw new IllegalArgumentException("El show_id debe ser un número positivo");
		}

		log.info("Consultando show en TV Maze con id={}", showId);

		try {
			Map<String, Object> show = tvMazeWebClient.get()
					.uri("/shows/{id}", showId)
					.retrieve()
					.onStatus(status -> status == HttpStatus.NOT_FOUND,
							response -> Mono.error(new ShowNotFoundException(showId)))
					.onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
							.defaultIfEmpty("")
							.flatMap(body -> Mono.error(new TvMazeClientException(
									"Error al consultar TV Maze (HTTP " + response.statusCode().value() + "): " + body))))
					.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
					})
					.block();

			if (show == null) {
				throw new ShowNotFoundException(showId);
			}

			log.info("Show id={} obtenido correctamente", showId);
			return show;

		} catch (ShowNotFoundException ex) {
			throw ex;
		} catch (TvMazeClientException ex) {
			throw ex;
		} catch (WebClientResponseException.NotFound ex) {
			throw new ShowNotFoundException(showId);
		} catch (WebClientResponseException ex) {
			throw new TvMazeClientException(
					"Error al consultar TV Maze (HTTP " + ex.getRawStatusCode() + "): " + ex.getResponseBodyAsString(),
					ex);
		} catch (Exception ex) {
			if (ex.getCause() instanceof ShowNotFoundException) {
				throw (ShowNotFoundException) ex.getCause();
			}
			throw new TvMazeClientException("No se pudo comunicar con TV Maze API: " + ex.getMessage(), ex);
		}
	}
}
