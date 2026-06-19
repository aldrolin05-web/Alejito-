package com.example.servidor.controller;

import com.example.servidor.entity.Endpoint;
import com.example.servidor.repository.EndpointRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/endpoint")
public class EndpointController {

    final EndpointRepository endpointRepository;

    public EndpointController(EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    @GetMapping("")
    public List<Endpoint> listar() {
        return endpointRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endpoint> obtener(@PathVariable Integer id) {
        return endpointRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
