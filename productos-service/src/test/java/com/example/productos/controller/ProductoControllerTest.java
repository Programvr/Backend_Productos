package com.example.productos.controller;

import com.example.productos.dto.ProductoDTO;
import com.example.productos.dto.ProductoResponse;
import com.example.productos.jsonapi.JsonApiResponse;
import com.example.productos.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductoController.
 * Se validan los casos de creación, actualización, obtención, eliminación,
 * listado de productos y manejo de errores.
 */
class ProductoControllerTest {

    /**
     * Prueba la creación de un producto.
     * Verifica que el controlador responde con CREATED y los datos correctos.
     */
    @Test
    void testCrearProducto() {
        ProductoService service = mock(ProductoService.class);
        ProductoController controller = new ProductoController(service);

        ProductoDTO dto = new ProductoDTO();
        ProductoResponse response = new ProductoResponse();
        response.setId(1L);

        when(service.crearProducto(dto)).thenReturn(response);

        ResponseEntity<JsonApiResponse<ProductoResponse>> result = controller.crearProducto(dto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        // Verifica que el tipo y el id sean correctos en la respuesta
        assertEquals("productos", result.getBody().getData().getType());
        assertEquals("1", result.getBody().getData().getId());
    }

    /**
     * Prueba la actualización de un producto.
     * Verifica que el controlador responde con OK y los datos correctos.
     */
    @Test
    void testActualizarProducto() {
        ProductoService service = mock(ProductoService.class);
        ProductoController controller = new ProductoController(service);

        ProductoDTO dto = new ProductoDTO();
        ProductoResponse response = new ProductoResponse();
        response.setId(2L);

        when(service.actualizarProducto(2L, dto)).thenReturn(response);

        ResponseEntity<JsonApiResponse<ProductoResponse>> result = controller.actualizarProducto(2L, dto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("productos", result.getBody().getData().getType());
        assertEquals("2", result.getBody().getData().getId());
    }

    /**
     * Prueba la obtención de un producto por ID.
     * Verifica que el controlador responde con OK y los datos correctos.
     */
    @Test
    void testObtenerProductoPorId() {
        ProductoService service = mock(ProductoService.class);
        ProductoController controller = new ProductoController(service);

        ProductoResponse response = new ProductoResponse();
        response.setId(3L);

        when(service.obtenerProductoPorId(3L)).thenReturn(response);

        ResponseEntity<JsonApiResponse<ProductoResponse>> result = controller.obtenerProductoPorId(3L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("productos", result.getBody().getData().getType());
        assertEquals("3", result.getBody().getData().getId());
    }

    /**
     * Prueba la eliminación de un producto.
     * Verifica que el controlador responde con NO_CONTENT y que el servicio es invocado.
     */
    @Test
    void testEliminarProducto() {
        ProductoService service = mock(ProductoService.class);
        ProductoController controller = new ProductoController(service);

        doNothing().when(service).eliminarProducto(4L);

        ResponseEntity<Void> result = controller.eliminarProducto(4L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(service, times(1)).eliminarProducto(4L);
    }

    /**
     * Prueba el listado de productos paginados.
     * Verifica que el controlador responde con OK y que la respuesta contiene la clave "data".
     */
    @Test
    void testListarProductos() {
        ProductoService service = mock(ProductoService.class);
        ProductoController controller = new ProductoController(service);

        ProductoResponse p1 = new ProductoResponse();
        p1.setId(5L);
        ProductoResponse p2 = new ProductoResponse();
        p2.setId(6L);

        Page<ProductoResponse> page = new PageImpl<>(List.of(p1, p2));
        when(service.listarTodosLosProductos(any(Pageable.class))).thenReturn(page);

        ResponseEntity<?> result = controller.listarProductos(Pageable.unpaged());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody() instanceof java.util.Map);
        java.util.Map<?,?> map = (java.util.Map<?,?>) result.getBody();
        assertTrue(map.containsKey("data"));
    }

    /**
     * Prueba el manejo de error cuando el producto no existe.
     * Verifica que se lanza una excepción con el mensaje esperado.
     */
    @Test
    void testErrorProductoNoEncontrado() {
        ProductoService service = mock(ProductoService.class);
        ProductoController controller = new ProductoController(service);

        when(service.obtenerProductoPorId(100L)).thenThrow(new RuntimeException("Producto no encontrado"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            controller.obtenerProductoPorId(100L);
        });

        assertTrue(exception.getMessage().contains("Producto no encontrado"));
    }
}