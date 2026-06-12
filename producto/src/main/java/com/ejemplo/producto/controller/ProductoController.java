package com.ejemplo.producto.controller;

import com.ejemplo.producto.model.Producto;
import com.ejemplo.producto.repository.ProductoRepository;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductoController {

    final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // GET /product → listar todos los productos
    @GetMapping("/product")
    public ResponseEntity<HashMap<String, Object>> listarProductos() {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            List<Producto> productos = productoRepository.findAll();
            respuesta.put("estado", "ok");
            respuesta.put("productos", productos);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // GET /product/{id} → obtener un producto por id
    @GetMapping("/product/{id}")
    public ResponseEntity<HashMap<String, Object>> obtenerProducto(@PathVariable String id) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            int idInt = Integer.parseInt(id);
            Optional<Producto> opt = productoRepository.findById((long) idInt);
            if (opt.isPresent()) {
                respuesta.put("estado", "ok");
                respuesta.put("producto", opt.get());
                return ResponseEntity.ok(respuesta);
            } else {
                respuesta.put("estado", "error");
                respuesta.put("mensaje", "No se encontró el producto con id: " + id);
                return ResponseEntity.badRequest().body(respuesta);
            }
        } catch (NumberFormatException e) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "El id debe ser un número entero");
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // POST /product → crear un producto
    @PostMapping("/product")
    public ResponseEntity<HashMap<String, Object>> crearProducto(@RequestBody Producto producto) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            Producto guardado = productoRepository.save(producto);
            respuesta.put("estado", "creado");
            respuesta.put("id", guardado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (Exception e) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // PUT /product → actualizar un producto
    @PutMapping("/product")
    public ResponseEntity<HashMap<String, Object>> actualizarProducto(@RequestBody Producto producto) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            if (producto.getId() == null || producto.getId() <= 0) {
                respuesta.put("estado", "error");
                respuesta.put("mensaje", "Se debe enviar un id válido para actualizar");
                return ResponseEntity.badRequest().body(respuesta);
            }
            Optional<Producto> opt = productoRepository.findById(producto.getId());
            if (!opt.isPresent()) {
                respuesta.put("estado", "error");
                respuesta.put("mensaje", "No se encontró el producto con id: " + producto.getId());
                return ResponseEntity.badRequest().body(respuesta);
            }
            productoRepository.save(producto);
            respuesta.put("estado", "actualizado");
            respuesta.put("id", producto.getId());
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // DELETE /product/{id} → eliminar un producto por id
    @DeleteMapping("/product/{id}")
    public ResponseEntity<HashMap<String, Object>> eliminarProducto(@PathVariable String id) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            int idInt = Integer.parseInt(id);
            Optional<Producto> opt = productoRepository.findById((long) idInt);
            if (!opt.isPresent()) {
                respuesta.put("estado", "error");
                respuesta.put("mensaje", "No se encontró el producto con id: " + id);
                return ResponseEntity.badRequest().body(respuesta);
            }
            productoRepository.deleteById((long) idInt);
            respuesta.put("estado", "eliminado");
            respuesta.put("id", idInt);
            return ResponseEntity.ok(respuesta);
        } catch (NumberFormatException e) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "El id debe ser un número entero");
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // Manejo de excepción cuando no se envía cuerpo en POST o PUT
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, Object>> handleNotReadable(HttpMessageNotReadableException e) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("estado", "error");
        respuesta.put("mensaje", "Debe enviar la información del producto en el cuerpo del mensaje");
        return ResponseEntity.badRequest().body(respuesta);
    }
}
