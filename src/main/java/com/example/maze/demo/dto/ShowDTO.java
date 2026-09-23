package com.example.maze.demo.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowDTO {

	private Long id;
	private String name;
	/** network_name o webchannel_name */
	private String channel;
	private String summary;
	private List<String> genres;
	private List<CommentItemDTO> comments;

	public ShowDTO() {
		this.genres = Collections.emptyList();
		this.comments = new ArrayList<CommentItemDTO>();
	}

	public ShowDTO(Long id, String name, String channel, String summary, List<String> genres) {
		this.id = id;
		this.name = name;
		this.channel = channel;
		this.summary = summary;
		this.genres = genres != null ? genres : Collections.<String>emptyList();
		this.comments = new ArrayList<CommentItemDTO>();
	}

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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
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

	public List<CommentItemDTO> getComments() {
		return comments;
	}

	public void setComments(List<CommentItemDTO> comments) {
		this.comments = comments != null ? comments : new ArrayList<CommentItemDTO>();
	}
}
