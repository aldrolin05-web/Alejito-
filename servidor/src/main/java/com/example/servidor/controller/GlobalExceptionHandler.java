package com.example.servidor.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<HashMap<String, String>> tokenExpirado(ExpiredJwtException e) {
        HashMap<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "TOKEN_EXPIRED");
        respuesta.put("mensaje", "El token JWT ha expirado");
        return ResponseEntity.status(401).body(respuesta);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<HashMap<String, String>> tokenMalformado(MalformedJwtException e) {
        HashMap<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "TOKEN_MALFORMED");
        respuesta.put("mensaje", "El token JWT tiene un formato inválido");
        return ResponseEntity.status(401).body(respuesta);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HashMap<String, String>> accesoDenegado(AccessDeniedException e) {
        HashMap<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "ACCESS_DENIED");
        respuesta.put("mensaje", "No tiene permisos para realizar esta acción");
        return ResponseEntity.status(403).body(respuesta);
    }
}
