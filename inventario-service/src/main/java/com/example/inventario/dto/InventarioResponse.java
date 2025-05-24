package com.example.inventario.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponse {
    
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidadDisponible;
    private Integer cantidadReservada;
}