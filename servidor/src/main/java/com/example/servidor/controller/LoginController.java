package com.example.servidor.controller;

import com.example.servidor.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    final JwtUtil jwtUtil;
    final UserDetailsService userDetailsService;

    public LoginController(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestParam String username,
                                                          @RequestParam String password) {
        HashMap<String, Object> respuesta = new HashMap<>();

        // Usuarios válidos definidos en WebSecurityConfig
        boolean validUser = (username.equals("user") && password.equals("user123")) ||
                            (username.equals("admin") && password.equals("admin123"));

        if (!validUser) {
            respuesta.put("error", "INVALID_CREDENTIALS");
            respuesta.put("mensaje", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(401).body(respuesta);
        }

        String role = username.equals("admin") ? "ADMIN" : "USER";
        String token = jwtUtil.generateToken(username, role);

        respuesta.put("token", token);
        respuesta.put("rol", role);
        return ResponseEntity.ok(respuesta);
    }
}
