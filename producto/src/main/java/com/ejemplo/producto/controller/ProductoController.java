package com.ejemplo.producto.controller;

import com.ejemplo.producto.exception.BusinessException;
import com.ejemplo.producto.model.Producto;
import com.ejemplo.producto.repository.ProductoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository repo;

    // POST /api/productos — Registrar producto
    @PostMapping
    public ResponseEntity<Map<String, Object>> registrar(@Valid @RequestBody Producto producto) {
        Producto guardado = repo.save(producto);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Producto registrado correctamente");
        respuesta.put("id", guardado.getId());
        respuesta.put("nombre", guardado.getNombre());
        respuesta.put("descripcion", guardado.getDescripcion());
        respuesta.put("precio", guardado.getPrecio());
        respuesta.put("stock", guardado.getStock());

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}
