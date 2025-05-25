package com.example.productos.service;

import com.example.productos.dto.ProductoDTO;
import com.example.productos.dto.ProductoResponse;
import com.example.productos.model.Producto;
import com.example.productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    private ProductoRepository productoRepository;
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        productoService = new ProductoService(productoRepository);
    }

    /**
     * Prueba la creación de un producto.
     */
    @Test
    void testCrearProducto() {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Test");
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Test");

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoResponse response = productoService.crearProducto(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test", response.getNombre());
    }

    /**
     * Prueba la obtención de un producto por ID.
     */
    @Test
    void testObtenerProductoPorId() {
        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Producto 2");

        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));

        ProductoResponse response = productoService.obtenerProductoPorId(2L);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("Producto 2", response.getNombre());
    }

    /**
     * Prueba el manejo de error cuando el producto no existe.
     */
    @Test
    void testObtenerProductoPorIdNoEncontrado() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productoService.obtenerProductoPorId(99L);
        });

        assertTrue(exception.getMessage().contains("no encontrado"));
    }

    // Puedes agregar más pruebas para actualizar, eliminar, etc.
}