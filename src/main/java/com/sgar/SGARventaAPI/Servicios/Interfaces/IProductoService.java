package com.sgar.SGARventaAPI.Servicios.Interfaces;

import com.sgar.SGARventaAPI.modelos.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface IProductoService {
    
    Producto crearProducto(Producto producto);
    
    Page<Producto> obtenerTodosProductos(Pageable pageable);
    
    Optional<Producto> obtenerProductoPorId(Integer id);
    
    Producto actualizarProducto(Integer id, Producto producto);
    
    void eliminarProducto(Integer id);
    
    Page<Producto> buscarPorNombre(String nombre, Pageable pageable);
    
    Page<Producto> buscarPorCategoria(Integer categoriaId, Pageable pageable);
    
    Page<Producto> buscarPorPlan(Integer planId, Pageable pageable);
    
    Page<Producto> buscarPorTipo(String tipo, Pageable pageable);
    
    Page<Producto> buscarPorRangoPrecio(BigDecimal minPrecio, BigDecimal maxPrecio, Pageable pageable);
    
    Page<Producto> buscarPorCategoriaYPlan(Integer categoriaId, Integer planId, Pageable pageable);
}
