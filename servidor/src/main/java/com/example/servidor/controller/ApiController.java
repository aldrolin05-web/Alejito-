package com.example.servidor.controller;

import com.example.servidor.entity.Api;
import com.example.servidor.entity.Equipo;
import com.example.servidor.repository.ApiRepository;
import com.example.servidor.repository.EquipoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/api")
public class ApiController {

    final ApiRepository apiRepository;
    final EquipoRepository equipoRepository;

    public ApiController(ApiRepository apiRepository, EquipoRepository equipoRepository) {
        this.apiRepository = apiRepository;
        this.equipoRepository = equipoRepository;
    }

    @GetMapping("")
    public List<Api> listar() {
        return apiRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> obtener(@PathVariable String id) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            int idInt = Integer.parseInt(id);
            Optional<Api> opt = apiRepository.findById(idInt);
            if (opt.isPresent()) {
                respuesta.put("result", "ok");
                respuesta.put("api", opt.get());
            } else {
                respuesta.put("result", "no existe");
            }
            return ResponseEntity.ok(respuesta);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = {"", "/"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<HashMap<String, Object>> registrar(
            @RequestParam String nombre,
            @RequestParam String version,
            @RequestParam String estado,
            @RequestParam Integer equipoId) {

        HashMap<String, Object> respuesta = new HashMap<>();
        Optional<Equipo> equipo = equipoRepository.findById(equipoId);
        if (!equipo.isPresent()) {
            respuesta.put("result", "error");
            respuesta.put("mensaje", "El equipo con id " + equipoId + " no existe");
            return ResponseEntity.badRequest().body(respuesta);
        }

        Api api = new Api();
        api.setNombre(nombre);
        api.setVersion(version);
        api.setEstado(estado);
        api.setFechaRegistro(LocalDate.now());
        api.setEquipo(equipo.get());
        apiRepository.save(api);

        respuesta.put("result", "creado");
        respuesta.put("id", api.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping(value = {"", "/"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<HashMap<String, Object>> actualizar(
            @RequestParam Integer id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String version,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer equipoId) {

        HashMap<String, Object> respuesta = new HashMap<>();
        Optional<Api> opt = apiRepository.findById(id);
        if (!opt.isPresent()) {
            respuesta.put("result", "error");
            respuesta.put("mensaje", "La API con id " + id + " no existe");
            return ResponseEntity.badRequest().body(respuesta);
        }

        Api api = opt.get();
        if (nombre != null)   api.setNombre(nombre);
        if (version != null)  api.setVersion(version);
        if (estado != null)   api.setEstado(estado);
        if (equipoId != null) {
            Optional<Equipo> equipo = equipoRepository.findById(equipoId);
            if (!equipo.isPresent()) {
                respuesta.put("result", "error");
                respuesta.put("mensaje", "El equipo con id " + equipoId + " no existe");
                return ResponseEntity.badRequest().body(respuesta);
            }
            api.setEquipo(equipo.get());
        }

        apiRepository.save(api);
        respuesta.put("result", "ok");
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> eliminar(@PathVariable String id) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            int idInt = Integer.parseInt(id);
            Optional<Api> opt = apiRepository.findById(idInt);
            if (!opt.isPresent()) {
                respuesta.put("result", "no existe");
                return ResponseEntity.badRequest().body(respuesta);
            }
            apiRepository.deleteById(idInt);
            respuesta.put("result", "ok");
            return ResponseEntity.ok(respuesta);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
