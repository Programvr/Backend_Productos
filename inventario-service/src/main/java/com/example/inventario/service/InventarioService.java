package com.example.inventario.service;

import com.example.inventario.dto.InventarioDTO;
import com.example.inventario.dto.InventarioResponse;
import com.example.inventario.event.InventarioEventPublisher;
import com.example.inventario.exception.ResourceNotFoundException;
import com.example.inventario.feign.ProductoClient;
import com.example.inventario.model.Inventario;
import com.example.inventario.repository.InventarioRepository;
import com.example.inventario.jsonapi.JsonApiResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoClient productoClient;
    private final InventarioEventPublisher eventPublisher;

    @Value("${productos.api.key}")
    private String productosApiKey;

    @Transactional(readOnly = true)
    public JsonApiResponse<InventarioResponse> consultarInventario(Long productoId) {
        try {
            var productoResponse = productoClient.obtenerProductoPorId(
                productoId,
                productosApiKey
            );

            Inventario inventario = inventarioRepository.findByProductoId(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para el producto ID: " + productoId));

            return mapToJsonApiResponse(inventario, productoResponse.getNombre());
        } catch (FeignException ex) {
            throw new IllegalArgumentException("Error al comunicarse con el microservicio de productos: " + ex.getMessage());
        }
    }

    @Transactional
    public JsonApiResponse<InventarioResponse> actualizarInventario(InventarioDTO inventarioDTO) {
        try {
            Inventario inventario = inventarioRepository.findByProductoId(inventarioDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para el producto ID: " + inventarioDTO.getProductoId()));

            // Actualizar cantidades
            inventario.setCantidadDisponible(inventario.getCantidadDisponible() - inventarioDTO.getCantidad());
            inventario.setCantidadReservada(inventario.getCantidadReservada() + inventarioDTO.getCantidad());

            Inventario inventarioActualizado = inventarioRepository.save(inventario);

            // Emitir evento después de actualizar inventario
            eventPublisher.publishInventarioActualizadoEvent(
                    inventarioActualizado.getProductoId(),
                    inventarioActualizado.getCantidadDisponible()
            );

            // Obtener nombre del producto actualizado
            var productoResponse = productoClient.obtenerProductoPorId(
                inventarioDTO.getProductoId(),
                productosApiKey
            );

            return mapToJsonApiResponse(inventarioActualizado, productoResponse.getNombre());
        } catch (FeignException ex) {
            throw new IllegalArgumentException("Error al comunicarse con el microservicio de productos: " + ex.getMessage());
        }
    }

    @Transactional
    public JsonApiResponse<InventarioResponse> crearInventario(Long productoId, Integer cantidadInicial) {
        try {
            var productoResponse = productoClient.obtenerProductoPorId(productoId, productosApiKey);

            if (inventarioRepository.findByProductoId(productoId).isPresent()) {
                throw new IllegalArgumentException("El inventario para este producto ya existe.");
            }

            Inventario inventario = new Inventario();
            inventario.setProductoId(productoId);
            inventario.setCantidadDisponible(cantidadInicial);
            inventario.setCantidadReservada(0);

            Inventario guardado = inventarioRepository.save(inventario);

            // Emitir evento después de crear inventario
            eventPublisher.publishInventarioActualizadoEvent(
                guardado.getProductoId(),
                guardado.getCantidadDisponible()
            );

            return mapToJsonApiResponse(guardado, productoResponse.getNombre());
        } catch (FeignException ex) {
            throw new IllegalArgumentException("Error al comunicarse con el microservicio de productos: " + ex.getMessage());
        }
    }

    // Método utilitario para mapear la entidad a respuesta JSON:API
    private JsonApiResponse<InventarioResponse> mapToJsonApiResponse(Inventario inventario, String nombreProducto) {
        InventarioResponse attributes = new InventarioResponse(
                inventario.getId(),
                inventario.getProductoId(),
                nombreProducto,
                inventario.getCantidadDisponible(),
                inventario.getCantidadReservada()
        );
        return new JsonApiResponse<>(
                "inventario",
                inventario.getId().toString(),
                attributes
        );
    }
}