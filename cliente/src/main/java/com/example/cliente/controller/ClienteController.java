package com.example.cliente.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClienteController {

    @Value("${servidor.url}")
    private String servidorUrl;

    final RestTemplate restTemplate = new RestTemplate();

    // ===== LOGIN =====
    @GetMapping("/")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, Model model) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("username", username);
            params.add("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    servidorUrl + "/login",
                    new HttpEntity<>(params, headers),
                    Map.class);

            String token = (String) response.getBody().get("token");
            session.setAttribute("token", token);
            session.setAttribute("rol", response.getBody().get("rol"));
            return "redirect:/apis";

        } catch (Exception e) {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ===== LISTAR APIs =====
    @GetMapping("/apis")
    public String listarApis(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            ResponseEntity<List> response = restTemplate.exchange(
                    servidorUrl + "/api/api",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    List.class);

            model.addAttribute("apis", response.getBody());
            model.addAttribute("rol", session.getAttribute("rol"));
            return "apis";
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Error al obtener APIs: " + e.getMessage());
            return "apis";
        }
    }

    // ===== FORMULARIO CREAR =====
    @GetMapping("/apis/nuevo")
    public String formNuevo(HttpSession session, Model model) {
        if (session.getAttribute("token") == null) return "redirect:/";
        cargarEquipos(session, model);
        return "api-form";
    }

    // ===== CREAR API =====
    @PostMapping("/apis/crear")
    public String crearApi(@RequestParam String nombre,
                           @RequestParam String version,
                           @RequestParam String estado,
                           @RequestParam Integer equipoId,
                           HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("nombre", nombre);
            params.add("version", version);
            params.add("estado", estado);
            params.add("equipoId", String.valueOf(equipoId));

            restTemplate.postForEntity(
                    servidorUrl + "/api/api",
                    new HttpEntity<>(params, headers),
                    Map.class);

            return "redirect:/apis";
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear API");
            cargarEquipos(session, model);
            return "api-form";
        }
    }

    // ===== FORMULARIO EDITAR =====
    @GetMapping("/apis/editar/{id}")
    public String formEditar(@PathVariable Integer id, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<Map> response = restTemplate.exchange(
                servidorUrl + "/api/api/" + id,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class);

        model.addAttribute("api", ((Map) response.getBody().get("api")));
        cargarEquipos(session, model);
        return "api-editar";
    }

    // ===== ACTUALIZAR API =====
    @PostMapping("/apis/actualizar")
    public String actualizarApi(@RequestParam Integer id,
                                @RequestParam String nombre,
                                @RequestParam String version,
                                @RequestParam String estado,
                                @RequestParam Integer equipoId,
                                HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", String.valueOf(id));
        params.add("nombre", nombre);
        params.add("version", version);
        params.add("estado", estado);
        params.add("equipoId", String.valueOf(equipoId));

        restTemplate.exchange(
                servidorUrl + "/api/api",
                HttpMethod.PUT,
                new HttpEntity<>(params, headers),
                Map.class);

        return "redirect:/apis";
    }

    // ===== ELIMINAR API =====
    @GetMapping("/apis/eliminar/{id}")
    public String eliminarApi(@PathVariable Integer id, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        restTemplate.exchange(
                servidorUrl + "/api/api/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Map.class);

        return "redirect:/apis";
    }

    // Método auxiliar para cargar equipos en el formulario
    private void cargarEquipos(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<List> equipos = restTemplate.exchange(
                servidorUrl + "/api/equipo",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class);
        model.addAttribute("equipos", equipos.getBody());
    }
}
