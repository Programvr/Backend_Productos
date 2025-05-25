package com.example.inventario.feign;

import com.example.inventario.dto.ProductoApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "productos-service", url = "http://localhost:8080")
public interface ProductoClient {
    
    @GetMapping("/api/productos/{id}")
    ProductoApiResponse obtenerProductoPorId(
        @PathVariable Long id,
        @RequestHeader("X-API-KEY") String apiKey // <-- Cambia aquí el nombre del header
    );
}