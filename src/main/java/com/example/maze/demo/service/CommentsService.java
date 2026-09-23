package com.example.maze.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.maze.demo.dto.CommentItemDTO;
import com.example.maze.demo.dto.CommentRequest;
import com.example.maze.demo.dto.ShowDTO;
import com.example.maze.demo.model.Comment;
import com.example.maze.demo.repository.CommentRepository;

@Service
public class CommentsService {

	private static final Logger log = LoggerFactory.getLogger(CommentsService.class);

	private final CommentRepository commentRepository;

	@Value("${tvmaze.comments.max-per-show:100}")
	private int maxCommentsPerShow;

	public CommentsService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public Comment saveComment(CommentRequest request) {
		String sanitized = sanitize(request.getComment());
		if (!StringUtils.hasText(sanitized)) {
			throw new IllegalArgumentException("comment no puede estar vacío");
		}

		Comment comment = new Comment(
				request.getShowId(),
				sanitized,
				request.getRating(),
				new Date());

		Comment saved = commentRepository.save(comment);
		log.info("Comentario guardado id={} showId={} rating={}", saved.getId(), saved.getShowId(), saved.getRating());
		return saved;
	}

	public List<Comment> findByShowId(Long showId) {
		return commentRepository.findByShowIdOrderByCreatedAtDesc(showId);
	}

	/**
	 * Comentarios de un show (comment + rating), más recientes primero, con tope.
	 */
	public List<CommentItemDTO> findCommentItemsByShowId(Long showId) {
		if (showId == null) {
			return Collections.emptyList();
		}
		List<Comment> comments = commentRepository.findByShowIdOrderByCreatedAtDesc(showId);
		return toLimitedItems(comments);
	}

	/**
	 * Agrega el arreglo comments a cada show de la búsqueda (una sola query batch).
	 */
	public void enrichShowsWithComments(List<ShowDTO> shows) {
		if (shows == null || shows.isEmpty()) {
			return;
		}

		Set<Long> showIds = new HashSet<Long>();
		for (ShowDTO show : shows) {
			if (show != null && show.getId() != null) {
				showIds.add(show.getId());
			}
		}
		if (showIds.isEmpty()) {
			return;
		}

		List<Comment> all = commentRepository.findByShowIdIn(showIds);
		Map<Long, List<Comment>> byShow = groupByShowId(all);

		for (ShowDTO show : shows) {
			if (show == null || show.getId() == null) {
				continue;
			}
			List<Comment> showComments = byShow.get(show.getId());
			if (showComments == null || showComments.isEmpty()) {
				show.setComments(new ArrayList<CommentItemDTO>());
			} else {
				sortNewestFirst(showComments);
				show.setComments(toLimitedItems(showComments));
			}
		}
	}

	private Map<Long, List<Comment>> groupByShowId(List<Comment> all) {
		Map<Long, List<Comment>> byShow = new HashMap<Long, List<Comment>>();
		if (all == null) {
			return byShow;
		}
		for (Comment c : all) {
			if (c.getShowId() == null) {
				continue;
			}
			List<Comment> list = byShow.get(c.getShowId());
			if (list == null) {
				list = new ArrayList<Comment>();
				byShow.put(c.getShowId(), list);
			}
			list.add(c);
		}
		return byShow;
	}

	private void sortNewestFirst(List<Comment> comments) {
		Collections.sort(comments, new Comparator<Comment>() {
			@Override
			public int compare(Comment a, Comment b) {
				Date da = a.getCreatedAt();
				Date db = b.getCreatedAt();
				if (da == null && db == null) {
					return 0;
				}
				if (da == null) {
					return 1;
				}
				if (db == null) {
					return -1;
				}
				return db.compareTo(da);
			}
		});
	}

	private List<CommentItemDTO> toLimitedItems(List<Comment> comments) {
		if (comments == null || comments.isEmpty()) {
			return new ArrayList<CommentItemDTO>();
		}
		int limit = maxCommentsPerShow > 0 ? Math.min(comments.size(), maxCommentsPerShow) : comments.size();
		List<CommentItemDTO> items = new ArrayList<CommentItemDTO>(limit);
		for (int i = 0; i < limit; i++) {
			Comment c = comments.get(i);
			items.add(new CommentItemDTO(c.getComment(), c.getRating()));
		}
		return items;
	}

	private String sanitize(String comment) {
		if (comment == null) {
			return null;
		}
		String trimmed = comment.trim();
		return trimmed.replaceAll("(?i)<script[^>]*>.*?</script>", "")
				.replaceAll("<[^>]+>", "");
	}
}
