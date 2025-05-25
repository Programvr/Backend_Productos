package com.example.inventario.controller;

import com.example.inventario.dto.ProductoResponse;
import com.example.inventario.feign.ProductoClient;
import com.example.inventario.model.Inventario;
import com.example.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para el controlador de inventario.
 * Se prueba la autenticación, la comunicación con el microservicio de productos (mockeado)
 * y el manejo de errores.
 */
@SpringBootTest
@AutoConfigureMockMvc
class InventarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventarioRepository inventarioRepository;

    // Mock del cliente Feign para simular la comunicación entre microservicios
    @MockBean
    private ProductoClient productoClient;

    // API Key válida para las pruebas
    private final String API_KEY = "2f8e1b9c-4a7d-4c2b-9e3a-123456789abc";

    @BeforeEach
    void setUp() {
        inventarioRepository.deleteAll();

        // Inserta un inventario de prueba para productoId=1 (para GET y PUT)
        Inventario inventario = new Inventario();
        inventario.setProductoId(1L);
        inventario.setCantidadDisponible(100);
        inventario.setCantidadReservada(0);
        inventarioRepository.save(inventario);
    }

    /**
     * Prueba que una petición sin API Key retorna 401 Unauthorized.
     */
    @Test
    void consultarInventarioUnauthorized() throws Exception {
        mockMvc.perform(get("/api/inventario/1"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Prueba la consulta de inventario con API Key válida y comunicación exitosa con el microservicio de productos.
     */
    @Test
        void consultarInventarioAuthorized() throws Exception {
    // Mock del producto anidado (ProductoApiResponse)
    ProductoResponse productoResponse = new ProductoResponse(1L, "Producto", "desc", 10.0, 100);
    com.example.inventario.dto.ProductoApiResponse.Data data = new com.example.inventario.dto.ProductoApiResponse.Data();
    data.setType("productos");
    data.setId("1");
    data.setAttributes(productoResponse);

    com.example.inventario.dto.ProductoApiResponse productoApiResponse = new com.example.inventario.dto.ProductoApiResponse();
    productoApiResponse.setData(data);

    when(productoClient.obtenerProductoPorId(anyLong(), anyString()))
            .thenReturn(productoApiResponse);

    mockMvc.perform(get("/api/inventario/1")
            .header("X-API-KEY", API_KEY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists());
}

    /**
     * Prueba la creación de inventario con API Key válida y comunicación exitosa con el microservicio de productos.
     */
    @Test
        void crearInventarioAuthorized() throws Exception {
    // Mock del producto anidado (ProductoApiResponse)
    ProductoResponse productoResponse = new ProductoResponse(2L, "Producto2", "desc2", 20.0, 200);
    com.example.inventario.dto.ProductoApiResponse.Data data = new com.example.inventario.dto.ProductoApiResponse.Data();
    data.setType("productos");
    data.setId("2");
    data.setAttributes(productoResponse);

    com.example.inventario.dto.ProductoApiResponse productoApiResponse = new com.example.inventario.dto.ProductoApiResponse();
    productoApiResponse.setData(data);

    when(productoClient.obtenerProductoPorId(anyLong(), anyString()))
            .thenReturn(productoApiResponse);

    mockMvc.perform(post("/api/inventario")
            .param("productoId", "2")
            .param("cantidadInicial", "50")
            .header("X-API-KEY", API_KEY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists());
        }

    /**
     * Prueba la actualización de inventario con API Key válida y comunicación exitosa con el microservicio de productos.
     */
    @Test
        void actualizarInventarioAuthorized() throws Exception {
    ProductoResponse productoResponse = new ProductoResponse(1L, "Producto", "desc", 10.0, 100);
    com.example.inventario.dto.ProductoApiResponse.Data data = new com.example.inventario.dto.ProductoApiResponse.Data();
    data.setType("productos");
    data.setId("1");
    data.setAttributes(productoResponse);

    com.example.inventario.dto.ProductoApiResponse productoApiResponse = new com.example.inventario.dto.ProductoApiResponse();
    productoApiResponse.setData(data);

    when(productoClient.obtenerProductoPorId(anyLong(), anyString()))
            .thenReturn(productoApiResponse);

    String body = "{\"productoId\":1,\"cantidad\":5}";

    mockMvc.perform(put("/api/inventario")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header("X-API-KEY", API_KEY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists());
        }

    /**
     * Prueba el manejo de error cuando el inventario no existe (debe retornar 404 y formato JSON:API de error).
     */
    @Test
        void consultarInventarioProductoNoEncontrado() throws Exception {
    // Mock del producto anidado (ProductoApiResponse)
    ProductoResponse productoResponse = new ProductoResponse(99L, "Producto", "desc", 10.0, 100);
    com.example.inventario.dto.ProductoApiResponse.Data data = new com.example.inventario.dto.ProductoApiResponse.Data();
    data.setType("productos");
    data.setId("99");
    data.setAttributes(productoResponse);

    com.example.inventario.dto.ProductoApiResponse productoApiResponse = new com.example.inventario.dto.ProductoApiResponse();
    productoApiResponse.setData(data);

    when(productoClient.obtenerProductoPorId(anyLong(), anyString()))
            .thenReturn(productoApiResponse);

    mockMvc.perform(get("/api/inventario/99")
            .header("X-API-KEY", API_KEY))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errors").exists());
        }
}