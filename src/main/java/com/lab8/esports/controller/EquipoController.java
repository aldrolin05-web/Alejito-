package com.lab8.esports.controller;

import com.lab8.esports.exception.BusinessException;
import com.lab8.esports.model.Equipo;
import com.lab8.esports.repository.EquipoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
