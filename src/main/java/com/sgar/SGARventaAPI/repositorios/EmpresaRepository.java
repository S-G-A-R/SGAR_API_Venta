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
    
    // Obtener el límite de productos del plan de suscripción por asociadoId
    @Query("SELECT ts.limite FROM PlanDeSuscripcion p JOIN p.tipoSuscripcion ts WHERE p.asociadoId = :asociadoId")
    Optional<Integer> findLimiteProductosByAsociadoId(@Param("asociadoId") Long asociadoId);
}
