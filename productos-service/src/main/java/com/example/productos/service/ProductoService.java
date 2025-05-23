package com.example.productos.service;

import com.example.productos.dto.ProductoDTO;
import com.example.productos.dto.ProductoResponse;
import com.example.productos.exception.ResourceNotFoundException;
import com.example.productos.model.Producto;
import com.example.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    @Transactional
    public ProductoResponse crearProducto(ProductoDTO productoDTO) {
        Producto producto = mapToEntity(productoDTO);
        Producto nuevoProducto = productoRepository.save(producto);
        return mapToResponse(nuevoProducto);
    }
    
    @Transactional(readOnly = true)
    public ProductoResponse obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return mapToResponse(producto);
    }
    
    @Transactional
    public ProductoResponse actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        
        Producto productoActualizado = productoRepository.save(producto);
        return mapToResponse(productoActualizado);
    }
    
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        productoRepository.delete(producto);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductoResponse> listarTodosLosProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(this::mapToResponse);
    }
    
    private ProductoResponse mapToResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();
    }
    
    private Producto mapToEntity(ProductoDTO productoDTO) {
        return Producto.builder()
                .nombre(productoDTO.getNombre())
                .descripcion(productoDTO.getDescripcion())
                .precio(productoDTO.getPrecio())
                .stock(productoDTO.getStock())
                .build();
    }
}