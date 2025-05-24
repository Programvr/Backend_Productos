package com.example.inventario.event;

public interface InventarioEventPublisher {
    void publishInventarioActualizadoEvent(Long productoId, Integer cantidadDisponible);
}