package com.example.inventario.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsoleInventarioEventPublisher implements InventarioEventPublisher {
    
    @Override
    public void publishInventarioActualizadoEvent(Long productoId, Integer cantidadDisponible) {
        log.info("Evento de inventario: Producto ID {} - Cantidad disponible actualizada a {}", 
                productoId, cantidadDisponible);
    }
}