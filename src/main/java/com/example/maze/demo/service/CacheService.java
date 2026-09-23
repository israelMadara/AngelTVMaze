package com.example.maze.demo.service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.maze.demo.model.ShowCache;
import com.example.maze.demo.repository.ShowCacheRepository;

/**
 * Caché cache-aside para shows (colección shows_cache).
 */
@Service
public class CacheService {

	private static final Logger log = LoggerFactory.getLogger(CacheService.class);

	private final ShowCacheRepository showCacheRepository;
	private final TVMazeService tvMazeService;

	@Value("${tvmaze.cache.ttl-days:7}")
	private int ttlDays;

	public CacheService(ShowCacheRepository showCacheRepository, TVMazeService tvMazeService) {
		this.showCacheRepository = showCacheRepository;
		this.tvMazeService = tvMazeService;
	}

	/**
	 * 1) Busca en MongoDB; si existe y no expiró, retorna caché.
	 * 2) Si no, consume TV Maze, guarda en Mongo y retorna.
	 */
	public Map<String, Object> getShowWithCache(Long showId) {
		if (showId == null || showId <= 0) {
			throw new IllegalArgumentException("El show_id debe ser un número positivo");
		}

		Optional<ShowCache> cached = showCacheRepository.findByShowId(showId);
		if (cached.isPresent()) {
			ShowCache entry = cached.get();
			if (!isExpired(entry)) {
				log.info("CACHE HIT showId={}", showId);
				return entry.getData();
			}
			log.info("CACHE EXPIRED showId={}, invalidando", showId);
			showCacheRepository.delete(entry);
		} else {
			log.info("CACHE MISS showId={}", showId);
		}

		Map<String, Object> fromApi = tvMazeService.getShowById(showId);
		save(showId, fromApi);
		return fromApi;
	}

	public void save(Long showId, Map<String, Object> data) {
		Optional<ShowCache> existing = showCacheRepository.findByShowId(showId);
		ShowCache entry;
		if (existing.isPresent()) {
			entry = existing.get();
			entry.setData(data);
			entry.setCachedAt(new Date());
		} else {
			entry = new ShowCache(showId, data, new Date());
		}
		showCacheRepository.save(entry);
		log.info("Show id={} guardado en shows_cache", showId);
	}

	private boolean isExpired(ShowCache entry) {
		if (entry.getCachedAt() == null || ttlDays <= 0) {
			return false;
		}
		long ageMs = System.currentTimeMillis() - entry.getCachedAt().getTime();
		long ttlMs = TimeUnit.DAYS.toMillis(ttlDays);
		return ageMs > ttlMs;
	}
}
