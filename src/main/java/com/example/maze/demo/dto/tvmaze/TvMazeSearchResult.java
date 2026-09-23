package com.example.maze.demo.dto.tvmaze;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Respuesta de GET /search/shows?q=...
 * TV Maze envuelve el show en un objeto con score + show.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvMazeSearchResult {

	private Double score;
	private TvMazeShow show;

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public TvMazeShow getShow() {
		return show;
	}

	public void setShow(TvMazeShow show) {
		this.show = show;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TvMazeShow {
		private Long id;
		private String name;
		private String summary;
		private List<String> genres;
		private TvMazeNetwork network;
		private TvMazeNetwork webChannel;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public List<String> getGenres() {
			return genres;
		}

		public void setGenres(List<String> genres) {
			this.genres = genres;
		}

		public TvMazeNetwork getNetwork() {
			return network;
		}

		public void setNetwork(TvMazeNetwork network) {
			this.network = network;
		}

		public TvMazeNetwork getWebChannel() {
			return webChannel;
		}

		public void setWebChannel(TvMazeNetwork webChannel) {
			this.webChannel = webChannel;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TvMazeNetwork {
		private Long id;
		private String name;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
