package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.ImagenProductos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenProductosRepository extends JpaRepository<ImagenProductos, Integer> {

    // Buscar todas las imágenes de un producto con paginación
    Page<ImagenProductos> findByProductoId(Integer productoId, Pageable pageable);

    // Buscar imágenes por producto con filtros opcionales
    @Query("SELECT i FROM ImagenProductos i WHERE " +
           "(:productoId IS NULL OR i.producto.id = :productoId) AND " +
           "(:tipoMime IS NULL OR LOWER(i.tipoMime) LIKE LOWER(CONCAT('%', :tipoMime, '%')))")
    Page<ImagenProductos> buscarConFiltros(@Param("productoId") Integer productoId,
                                           @Param("tipoMime") String tipoMime,
                                           Pageable pageable);
  
    // Verificar si un producto tiene imágenes
    boolean existsByProductoId(Integer productoId);

    // Eliminar todas las imágenes de un producto
    void deleteByProductoId(Integer productoId);

    // Buscar las primeras N imágenes de un producto (para mostrar previews)
    @Query("SELECT i FROM ImagenProductos i WHERE i.producto.id = :productoId ORDER BY i.id ASC")
    List<ImagenProductos> findTopNByProductoId(@Param("productoId") Integer productoId, Pageable pageable);

}