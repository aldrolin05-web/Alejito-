package com.lab8.esports.controller;

import com.lab8.esports.exception.BusinessException;
import com.lab8.esports.model.Equipo;
import com.lab8.esports.repository.EquipoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoRepository repo;

    // P1 - Registrar equipo
    @PostMapping
    public ResponseEntity<Equipo> registrar(@Valid @RequestBody Equipo equipo) {
        if (repo.existsByNombre(equipo.getNombre()))
            throw new BusinessException("Ya existe un equipo con el nombre: " + equipo.getNombre());
        if (repo.existsByTag(equipo.getTag()))
            throw new BusinessException("Ya existe un equipo con el tag: " + equipo.getTag());
        equipo.setEstado(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(equipo));
    }

    // P2 - Listar todos los equipos
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar() {
        List<Map<String, Object>> resultado = repo.findAll().stream().map(e -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("nombre", e.getNombre());
            m.put("tag", e.getTag());
            m.put("capitan", e.getCapitan());
            m.put("juego", e.getJuego());
            m.put("pais", e.getPais());
            m.put("estado", e.isEstado() ? "Activo" : "Inactivo");
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    // P2 - Buscar por tag
    @GetMapping("/tag/{tag}")
    public ResponseEntity<Map<String, Object>> buscarPorTag(@PathVariable String tag) {
        Equipo e = repo.findByTag(tag)
                .orElseThrow(() -> new BusinessException("Equipo con tag '" + tag + "' no encontrado"));
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("nombre", e.getNombre());
        m.put("tag", e.getTag());
        m.put("capitan", e.getCapitan());
        m.put("juego", e.getJuego());
        m.put("pais", e.getPais());
        m.put("estado", e.isEstado() ? "Activo" : "Inactivo");
        return ResponseEntity.ok(m);
    }
}
