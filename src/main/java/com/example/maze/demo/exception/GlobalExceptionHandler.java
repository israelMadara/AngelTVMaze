package com.example.maze.demo.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
		return build(HttpStatus.BAD_REQUEST, "Parámetro requerido faltante: " + ex.getParameterName());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		return build(HttpStatus.BAD_REQUEST, "Tipo de parámetro inválido: " + ex.getName());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex) {
		return build(HttpStatus.BAD_REQUEST, "Body JSON inválido o mal formado");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		String details = ex.getBindingResult().getFieldErrors().stream()
				.map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return build(HttpStatus.BAD_REQUEST, details);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		String details = ex.getConstraintViolations().stream()
				.map(v -> v.getMessage())
				.collect(Collectors.joining("; "));
		return build(HttpStatus.BAD_REQUEST, details);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(ShowNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleShowNotFound(ShowNotFoundException ex) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(TvMazeClientException.class)
	public ResponseEntity<Map<String, Object>> handleTvMaze(TvMazeClientException ex) {
		return build(HttpStatus.BAD_GATEWAY, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
	}

	private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}
}
