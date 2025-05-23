package com.example.productos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para crear o actualizar productos")
public class ProductoDTO {
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Schema(description = "Nombre del producto", example = "Laptop")
    private String nombre;
    
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    @Schema(description = "Descripción del producto", example = "Laptop de última generación")
    private String descripcion;
    
    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @Schema(description = "Precio del producto", example = "1200.50")
    private BigDecimal precio;
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Stock del producto", example = "10")
    private Integer stock;
}