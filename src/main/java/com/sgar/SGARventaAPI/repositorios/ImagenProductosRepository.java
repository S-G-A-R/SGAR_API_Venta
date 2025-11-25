package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.ImagenProductos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ImagenProductosRepository extends JpaRepository<ImagenProductos, Integer> {

    // Buscar imágenes con filtros opcionales (por tipoMime)
    @Query("SELECT i FROM ImagenProductos i WHERE " +
           "(:tipoMime IS NULL OR LOWER(i.tipoMime) LIKE LOWER(CONCAT('%', :tipoMime, '%'))) ")
    Page<ImagenProductos> buscarConFiltros(@Param("tipoMime") String tipoMime, Pageable pageable);

}