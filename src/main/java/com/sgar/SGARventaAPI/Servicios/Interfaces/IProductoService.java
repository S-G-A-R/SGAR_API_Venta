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
    
    Page<Producto> buscarProductos(String nombre, String tipo, Integer categoriaId, Long empresaId, Long asociadoId,
                                   BigDecimal minPrecio, BigDecimal maxPrecio, Pageable pageable);

    /** Verifica si la empresa (por id) puede agregar más productos según el plan del asociado. */
    boolean puedeAgregarProducto(Long empresaId);
}
