package org.example.expert.common.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErrorResponse {

	private final ObjectMapper objectMapper;


	//필터 + 인터셉터에서 사용
	public void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws
		IOException {
		response.setStatus(status.value());
		response.setContentType("application/json;charset=UTF-8");

		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("status", status.name());
		errorResponse.put("code", status.value());
		errorResponse.put("message", message);
		errorResponse.put("timestamp", LocalDateTime.now());

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}

	//전역 예외 응답
	public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("status", status.name());
		errorResponse.put("code", status.value());
		errorResponse.put("message", message);
		errorResponse.put("timestamp", LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, status);
	}
}
