package com.example.maze.demo.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	@Value("${tvmaze.api.base-url}")
	private String baseUrl;

	@Value("${tvmaze.api.timeout-seconds:5}")
	private int timeoutSeconds;

	@Bean
	public WebClient tvMazeWebClient(WebClient.Builder builder) {
		HttpClient httpClient = HttpClient.create()
				.responseTimeout(Duration.ofSeconds(timeoutSeconds))
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSeconds * 1000);

		return builder
				.baseUrl(baseUrl)
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
	}
}
