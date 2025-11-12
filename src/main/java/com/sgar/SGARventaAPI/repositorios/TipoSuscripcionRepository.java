package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.TipoSuscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoSuscripcionRepository extends JpaRepository<TipoSuscripcion, Integer> {
    
    // Buscar por nombre (contiene, ignorando mayúsculas/minúsculas)
    Page<TipoSuscripcion> findBySuscripcionNombreContainingIgnoreCase(String nombre, Pageable pageable);
    
    // Buscar por rango de precio
    Page<TipoSuscripcion> findByPrecioBetween(java.math.BigDecimal minPrecio, java.math.BigDecimal maxPrecio, Pageable pageable);
    
    // Buscar por límite mayor o igual
    Page<TipoSuscripcion> findByLimiteGreaterThanEqual(Integer limite, Pageable pageable);
}
