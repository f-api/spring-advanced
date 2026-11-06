package org.example.expert.common.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErrorResponse {

	private final ObjectMapper objectMapper;

	public void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws
		IOException {
		response.setStatus(status.value());
		response.setContentType("application/json;charset=UTF-8");

		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("status", status.name());
		errorResponse.put("code", status.value());
		errorResponse.put("message", message);

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
