package com.tusuapp.coreapi.controllers;


import io.jsonwebtoken.security.SignatureException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

//    @ExceptionHandler(SignatureException.class)
//    public ResponseEntity<String> handleSignatureException(SignatureException ex) {
//        System.out.println("handleSignatureException");
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body("Invalid or expired JWT token");
//    }
//
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException e){
//        JSONObject response = new JSONObject();
//        response.put("error",e.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toMap());
//    }

}