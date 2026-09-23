package com.example.maze.demo.model;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Caché de shows en MongoDB (colección shows_cache).
 * Guarda el objeto completo retornado por TV Maze.
 */
@Document(collection = "shows_cache")
public class ShowCache {

	@Id
	private String id;

	@Indexed(unique = true)
	private Long showId;

	/** Payload completo del show (respuesta TV Maze). */
	private Map<String, Object> data;

	private Date cachedAt;

	public ShowCache() {
	}

	public ShowCache(Long showId, Map<String, Object> data, Date cachedAt) {
		this.showId = showId;
		this.data = data;
		this.cachedAt = cachedAt;
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

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Date getCachedAt() {
		return cachedAt;
	}

	public void setCachedAt(Date cachedAt) {
		this.cachedAt = cachedAt;
	}
}
