package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>, JpaSpecificationExecutor<Producto> {
    
    // Contar productos por empresa
    long countByEmpresaId(Long empresaId);
}
