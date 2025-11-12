package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    // Buscar por nombre (contiene, ignorando mayúsculas/minúsculas)
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    
    // Buscar por categoría
    Page<Producto> findByCategoriaProductoId(Integer categoriaId, Pageable pageable);
    
    // Buscar por plan de suscripción
    Page<Producto> findByPlanDeSuscripcionId(Integer planId, Pageable pageable);
    
    // Contar productos por plan de suscripción
    long countByPlanDeSuscripcionId(Integer planId);
    
    // Buscar por tipo
    Page<Producto> findByTipoContainingIgnoreCase(String tipo, Pageable pageable);
    
    // Buscar por rango de precio
    Page<Producto> findByPrecioBetween(BigDecimal minPrecio, BigDecimal maxPrecio, Pageable pageable);
    
    // Buscar por categoría y plan
    Page<Producto> findByCategoriaProductoIdAndPlanDeSuscripcionId(Integer categoriaId, Integer planId, Pageable pageable);
}
