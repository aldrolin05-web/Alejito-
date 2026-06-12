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

    // P3 - Actualizar parcialmente
    @PatchMapping("/{id}")
    public ResponseEntity<Equipo> actualizar(@PathVariable Long id,
                                              @RequestBody Map<String, Object> campos) {
        Equipo equipo = repo.findById(id)
                .orElseThrow(() -> new BusinessException("Equipo con id " + id + " no encontrado"));

        if (campos.containsKey("nombre")) {
            String nombre = (String) campos.get("nombre");
            if (repo.existsByNombreAndIdNot(nombre, id))
                throw new BusinessException("Ya existe un equipo con el nombre: " + nombre);
            equipo.setNombre(nombre);
        }
        if (campos.containsKey("tag")) {
            String tag = (String) campos.get("tag");
            if (tag.length() < 2 || tag.length() > 5)
                throw new BusinessException("El tag debe tener entre 2 y 5 caracteres");
            if (repo.existsByTagAndIdNot(tag, id))
                throw new BusinessException("Ya existe un equipo con el tag: " + tag);
            equipo.setTag(tag);
        }
        if (campos.containsKey("capitan"))
            equipo.setCapitan((String) campos.get("capitan"));
        if (campos.containsKey("cantidadJugadores")) {
            int cantidad = (int) campos.get("cantidadJugadores");
            if (cantidad < 5 || cantidad > 10)
                throw new BusinessException("La cantidad de jugadores debe estar entre 5 y 10");
            equipo.setCantidadJugadores(cantidad);
        }
        if (campos.containsKey("juego"))
            equipo.setJuego((String) campos.get("juego"));
        if (campos.containsKey("pais"))
            equipo.setPais((String) campos.get("pais"));
        if (campos.containsKey("correo")) {
            String correo = (String) campos.get("correo");
            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
                throw new BusinessException("El correo no tiene un formato válido");
            equipo.setCorreo(correo);
        }
        if (campos.containsKey("telefono")) {
            String tel = (String) campos.get("telefono");
            if (!tel.matches("\\d{9}"))
                throw new BusinessException("El teléfono debe contener exactamente 9 dígitos");
            equipo.setTelefono(tel);
        }
        return ResponseEntity.ok(repo.save(equipo));
    }
}
