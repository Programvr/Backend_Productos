package com.example.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long productoId;
    
    private Integer cantidadDisponible;
    
    private Integer cantidadReservada;
}