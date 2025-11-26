package com.tusuapp.coreapi.controllers;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        JSONObject response = new JSONObject();
        response.put("error", e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toMap());
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwt(ExpiredJwtException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", "Token has expired. Please log in again.");
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(EntityNotFoundException e) {
        JSONObject response = new JSONObject();
        response.put("error", e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toMap());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFound(Exception e) {
        JSONObject response = new JSONObject();
        response.put("error", e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toMap());
    }

}