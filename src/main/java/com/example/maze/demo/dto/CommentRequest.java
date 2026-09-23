package com.example.maze.demo.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRequest {

	@NotNull(message = "show_id es obligatorio")
	@Positive(message = "show_id debe ser un número positivo")
	@JsonProperty("show_id")
	private Long showId;

	@NotBlank(message = "comment es obligatorio")
	@Size(min = 1, max = 1000, message = "comment debe tener entre 1 y 1000 caracteres")
	private String comment;

	@NotNull(message = "rating es obligatorio")
	@Min(value = 0, message = "rating mínimo es 0")
	@Max(value = 5, message = "rating máximo es 5")
	private Integer rating;

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}
