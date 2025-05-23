package com.example.productos.controller;

import com.example.productos.dto.ProductoDTO;
import com.example.productos.dto.ProductoResponse;
import com.example.productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.productos.jsonapi.JsonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Productos", description = "Operaciones sobre productos")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Crear producto", description = "Crear producto")
    @PostMapping
    public ResponseEntity<JsonApiResponse<ProductoResponse>> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        ProductoResponse response = productoService.crearProducto(productoDTO);
        JsonApiResponse<ProductoResponse> jsonApi = new JsonApiResponse<>("productos", response.getId().toString(), response);
        return new ResponseEntity<>(jsonApi, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto por su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<JsonApiResponse<ProductoResponse>> obtenerProductoPorId(@PathVariable Long id) {
        ProductoResponse response = productoService.obtenerProductoPorId(id);
        JsonApiResponse<ProductoResponse> jsonApi = new JsonApiResponse<>("productos", response.getId().toString(), response);
        return ResponseEntity.ok(jsonApi);
    }

    @Operation(summary = "Actualizar producto por ID", description = "Actualizar un producto por su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<JsonApiResponse<ProductoResponse>> actualizarProducto(
            @PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        ProductoResponse response = productoService.actualizarProducto(id, productoDTO);
        JsonApiResponse<ProductoResponse> jsonApi = new JsonApiResponse<>("productos", response.getId().toString(), response);
        return ResponseEntity.ok(jsonApi);
    }

    @Operation(summary = "Eliminar producto por ID", description = "Eliminar un producto por su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar productos paginados", description = "Obtiene una lista paginada de productos")
    @GetMapping
    public ResponseEntity<?> listarProductos(Pageable pageable) {
        Page<ProductoResponse> productos = productoService.listarTodosLosProductos(pageable);
        // Para JSON:API, deberías mapear cada producto a JsonApiResponse y devolver una lista de data
        var data = productos.map(p -> new JsonApiResponse.Data<>("productos", p.getId().toString(), p)).toList();
        return ResponseEntity.ok().body(java.util.Map.of("data", data));
    }
}