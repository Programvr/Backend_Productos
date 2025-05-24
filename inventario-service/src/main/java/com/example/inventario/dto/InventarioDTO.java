package com.example.inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para crear o actualizar inventarios")
public class InventarioDTO {
    
    @NotNull(message = "El ID de producto no puede ser nulo")
    @Schema(description = "Id del producto", example = "1")
    private Long productoId;
    
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Schema(description = "Cantidad del producto", example = "10")
    private Integer cantidad;
}