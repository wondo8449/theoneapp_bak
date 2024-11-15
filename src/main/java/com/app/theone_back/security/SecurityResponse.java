package com.app.theone_back.security;

import com.app.theone_back.dto.CommonResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityResponse {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        CommonResponseDto responseDto = new CommonResponseDto(status.value(), message, null);
        String json = objectMapper.writeValueAsString(responseDto);

        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

}
