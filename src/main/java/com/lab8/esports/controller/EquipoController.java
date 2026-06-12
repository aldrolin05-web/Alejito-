package com.lab8.esports.controller;

import com.lab8.esports.dto.EquipoResponseDTO;
import com.lab8.esports.dto.EquipoUpdateDTO;
import com.lab8.esports.model.Equipo;
import com.lab8.esports.service.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService service;

    // POST /api/equipos — Registrar equipo (P1)
    @PostMapping
    public ResponseEntity<Equipo> registrar(@Valid @RequestBody Equipo equipo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(equipo));
    }

    // GET /api/equipos — Listar todos (P2)
    @GetMapping
    public ResponseEntity<List<EquipoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /api/equipos/tag/{tag} — Buscar por tag (P2)
    @GetMapping("/tag/{tag}")
    public ResponseEntity<EquipoResponseDTO> buscarPorTag(@PathVariable String tag) {
        return ResponseEntity.ok(service.buscarPorTag(tag));
    }

    // PATCH /api/equipos/{id} — Actualizar parcialmente (P3)
    @PatchMapping("/{id}")
    public ResponseEntity<Equipo> actualizar(@PathVariable Long id,
                                              @Valid @RequestBody EquipoUpdateDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/equipos/{id} — Desactivar equipo (P4)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.ok("Equipo desactivado correctamente");
    }
}
