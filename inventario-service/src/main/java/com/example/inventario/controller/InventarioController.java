package com.example.inventario.controller;

import com.example.inventario.dto.InventarioDTO;
import com.example.inventario.dto.InventarioResponse;
import com.example.inventario.jsonapi.JsonApiResponse;
import com.example.inventario.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventarios", description = "Operaciones sobre inventarios")
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {
    
    private final InventarioService inventarioService;
    
    @Operation(summary = "Consultar inventario por productoId", description = "Consultar inventario por productoId")
    @GetMapping("/{productoId}")
    public ResponseEntity<JsonApiResponse<InventarioResponse>> consultarInventario(@PathVariable Long productoId) {
        var response = inventarioService.consultarInventario(productoId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Actualizar inventario", description = "Actualizar inventario")
    @PutMapping
    public ResponseEntity<JsonApiResponse<InventarioResponse>> actualizarInventario(
            @Valid @RequestBody InventarioDTO inventarioDTO) {
        var response = inventarioService.actualizarInventario(inventarioDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear inventario", description = "Crear inventario")
    @PostMapping
    public ResponseEntity<JsonApiResponse<InventarioResponse>> crearInventario(
            @RequestParam Long productoId,
            @RequestParam Integer cantidadInicial) {
        var response = inventarioService.crearInventario(productoId, cantidadInicial);
        return ResponseEntity.ok(response);
    }
}