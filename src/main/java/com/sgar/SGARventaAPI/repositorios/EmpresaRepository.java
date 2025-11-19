package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    // Buscar empresas por nombre (contiene)
    Page<Empresa> findByNombreEmpresaContainingIgnoreCase(String nombreEmpresa, Pageable pageable);
    
    // Buscar empresas por asociado ID
    Page<Empresa> findByAsociadoId(Long asociadoId, Pageable pageable);
    
    // Buscar empresa con sus planes y tipos de suscripción cargados (evita lazy loading)
    @Query("SELECT e FROM Empresa e LEFT JOIN FETCH e.planes p LEFT JOIN FETCH p.tipoSuscripcion WHERE e.id = :id")
    Optional<Empresa> findByIdWithPlanes(@Param("id") Long id);
    
    // Obtener el límite de productos del plan de suscripción de una empresa
    @Query("SELECT ts.limite FROM Empresa e JOIN e.planes p JOIN p.tipoSuscripcion ts WHERE e.id = :empresaId")
    Optional<Integer> findLimiteProductosByEmpresaId(@Param("empresaId") Long empresaId);
}
