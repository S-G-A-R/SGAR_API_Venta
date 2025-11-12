package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.CategoriaProducto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Integer> {
    
    // Buscar por nombre (contiene, ignorando mayúsculas/minúsculas)
    Page<CategoriaProducto> findByNombreCatContainingIgnoreCase(String nombre, Pageable pageable);
}
