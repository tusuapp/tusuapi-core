package com.tusuapp.coreapi.utils;


import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {


    public static ResponseEntity<?> errorResponse(HttpStatus status, String message){
        JSONObject response= new JSONObject();
        response.put("message",message);
        return ResponseEntity.status(status).body(response.toMap());
    }

}
