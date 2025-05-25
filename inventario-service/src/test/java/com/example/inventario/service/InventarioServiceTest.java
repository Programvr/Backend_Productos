package com.example.inventario.service;

import com.example.inventario.dto.InventarioDTO;
import com.example.inventario.dto.ProductoApiResponse;
import com.example.inventario.dto.ProductoResponse;
import com.example.inventario.event.InventarioEventPublisher;
import com.example.inventario.exception.ResourceNotFoundException;
import com.example.inventario.feign.ProductoClient;
import com.example.inventario.model.Inventario;
import com.example.inventario.repository.InventarioRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para InventarioService.
 */
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;
    @Mock
    private ProductoClient productoClient;
    @Mock
    private InventarioEventPublisher eventPublisher;

    @InjectMocks
    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inyecta manualmente el API Key para los tests
        inventarioService.setProductosApiKey("2f8e1b9c-4a7d-4c2b-9e3a-123456789abc");
    }

    /**
     * Prueba la creación de inventario cuando no existe previamente.
     */
    @Test
    void testCrearInventario() {
        Long productoId = 1L;
        Integer cantidadInicial = 10;

        // Mock de respuesta del microservicio de productos (DTO anidado)
        ProductoResponse productoResponse = new ProductoResponse(productoId, "Producto", "desc", 10.0, 100);
        ProductoApiResponse.Data data = new ProductoApiResponse.Data();
        data.setType("productos");
        data.setId(productoId.toString());
        data.setAttributes(productoResponse);

        ProductoApiResponse productoApiResponse = new ProductoApiResponse();
        productoApiResponse.setData(data);

        when(productoClient.obtenerProductoPorId(eq(productoId), anyString()))
            .thenReturn(productoApiResponse);
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(java.util.Optional.empty());
        when(inventarioRepository.save(any(Inventario.class)))
            .thenAnswer(invocation -> {
                Inventario inv = invocation.getArgument(0);
                inv.setId(1L); // Simula asignación de ID al guardar
                return inv;
            });

        // Act
        var response = inventarioService.crearInventario(productoId, cantidadInicial);

        // Assert
        assertNotNull(response);
        assertEquals(productoId, response.getData().getAttributes().getProductoId());
    }

    /**
     * Prueba la actualización de inventario existente.
     */
    @Test
    void testActualizarInventario() {
        Long productoId = 1L;
        Integer cantidadInicial = 10;
        Integer cantidadActualizar = 3;

        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(productoId);
        inventario.setCantidadDisponible(cantidadInicial);
        inventario.setCantidadReservada(0);

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(java.util.Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Mock de respuesta del microservicio de productos (DTO anidado)
        ProductoResponse productoResponse = new ProductoResponse(productoId, "Producto", "desc", 10.0, 100);
        ProductoApiResponse.Data data = new ProductoApiResponse.Data();
        data.setType("productos");
        data.setId(productoId.toString());
        data.setAttributes(productoResponse);

        ProductoApiResponse productoApiResponse = new ProductoApiResponse();
        productoApiResponse.setData(data);

        when(productoClient.obtenerProductoPorId(eq(productoId), anyString()))
            .thenReturn(productoApiResponse);

        InventarioDTO inventarioDTO = new InventarioDTO();
        inventarioDTO.setProductoId(productoId);
        inventarioDTO.setCantidad(cantidadActualizar);

        // Act
        var response = inventarioService.actualizarInventario(inventarioDTO);

        // Assert
        assertNotNull(response);
        assertEquals(productoId, response.getData().getAttributes().getProductoId());
        assertEquals(cantidadInicial - cantidadActualizar, response.getData().getAttributes().getCantidadDisponible());
        assertEquals(cantidadActualizar, response.getData().getAttributes().getCantidadReservada());
    }

    /**
     * Prueba la consulta de inventario cuando el inventario no existe.
     */
    @Test
    void testConsultarInventarioProductoNoEncontrado() {
        Long productoId = 99L;

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(java.util.Optional.empty());

        // Mock de respuesta del microservicio de productos (DTO anidado)
        ProductoResponse productoResponse = new ProductoResponse(productoId, "Producto", "desc", 10.0, 100);
        ProductoApiResponse.Data data = new ProductoApiResponse.Data();
        data.setType("productos");
        data.setId(productoId.toString());
        data.setAttributes(productoResponse);

        ProductoApiResponse productoApiResponse = new ProductoApiResponse();
        productoApiResponse.setData(data);

        when(productoClient.obtenerProductoPorId(eq(productoId), anyString()))
            .thenReturn(productoApiResponse);

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
            inventarioService.consultarInventario(productoId)
        );
        assertTrue(ex.getMessage().contains("Inventario no encontrado"));
    }

    /**
     * Prueba el manejo de error cuando el microservicio de productos falla.
     */
    @Test
    void testConsultarInventarioFalloAPI() {
        Long productoId = 1L;
        when(inventarioRepository.findByProductoId(productoId))
            .thenReturn(java.util.Optional.of(new Inventario())); // Simula que el inventario existe
        doThrow(FeignException.class).when(productoClient).obtenerProductoPorId(eq(productoId), anyString());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            inventarioService.consultarInventario(productoId)
        );
        assertTrue(ex.getMessage().contains("Error al comunicarse"));
    }
}