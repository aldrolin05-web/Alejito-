package com.ejemplo.producto.controller;

import com.ejemplo.producto.model.Producto;
import com.ejemplo.producto.repository.ProductoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository repo;

    // POST - Registrar producto
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

    // GET - Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    // GET - Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Long id) {
        Optional<Producto> opt = repo.findById(id);

        Map<String, Object> respuesta = new HashMap<>();
        if (opt.isEmpty()) {
            respuesta.put("error", "Producto con id " + id + " no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        Producto p = opt.get();
        respuesta.put("id", p.getId());
        respuesta.put("nombre", p.getNombre());
        respuesta.put("descripcion", p.getDescripcion());
        respuesta.put("precio", p.getPrecio());
        respuesta.put("stock", p.getStock());

        return ResponseEntity.ok(respuesta);
    }

    // PUT - Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody Producto datos) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Producto> opt = repo.findById(id);

        if (opt.isEmpty()) {
            respuesta.put("error", "Producto con id " + id + " no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        Producto producto = opt.get();
        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());

        Producto actualizado = repo.save(producto);

        respuesta.put("mensaje", "Producto actualizado correctamente");
        respuesta.put("id", actualizado.getId());
        respuesta.put("nombre", actualizado.getNombre());
        respuesta.put("descripcion", actualizado.getDescripcion());
        respuesta.put("precio", actualizado.getPrecio());
        respuesta.put("stock", actualizado.getStock());

        return ResponseEntity.ok(respuesta);
    }

    // DELETE - Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Producto> opt = repo.findById(id);

        if (opt.isEmpty()) {
            respuesta.put("error", "Producto con id " + id + " no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        repo.deleteById(id);
        respuesta.put("mensaje", "Producto eliminado correctamente");
        respuesta.put("id", id);

        return ResponseEntity.ok(respuesta);
    }
}
