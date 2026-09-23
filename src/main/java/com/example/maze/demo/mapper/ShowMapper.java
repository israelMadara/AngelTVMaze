package com.example.maze.demo.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.maze.demo.dto.ShowDTO;
import com.example.maze.demo.dto.tvmaze.TvMazeSearchResult;
import com.example.maze.demo.dto.tvmaze.TvMazeSearchResult.TvMazeNetwork;
import com.example.maze.demo.dto.tvmaze.TvMazeSearchResult.TvMazeShow;

@Component
public class ShowMapper {

	public List<ShowDTO> toShowDtoList(List<TvMazeSearchResult> results) {
		if (results == null || results.isEmpty()) {
			return Collections.emptyList();
		}
		return results.stream()
				.filter(r -> r != null && r.getShow() != null)
				.<ShowDTO>map((TvMazeSearchResult r) -> toShowDto(r.getShow()))
				.collect(Collectors.toList());
	}

	public ShowDTO toShowDto(TvMazeShow show) {
		return new ShowDTO(
				show.getId(),
				show.getName(),
				resolveChannel(show),
				show.getSummary(),
				show.getGenres() != null ? show.getGenres() : Collections.<String>emptyList());
	}

	/**
	 * Prioriza network.name; si no existe, usa webChannel.name.
	 */
	private String resolveChannel(TvMazeShow show) {
		String networkName = networkName(show.getNetwork());
		if (StringUtils.hasText(networkName)) {
			return networkName;
		}
		String webChannelName = networkName(show.getWebChannel());
		if (StringUtils.hasText(webChannelName)) {
			return webChannelName;
		}
		return null;
	}

	private String networkName(TvMazeNetwork network) {
		return network != null ? network.getName() : null;
	}
}
