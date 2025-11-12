package com.sgar.SGARventaAPI.repositorios;

import com.sgar.SGARventaAPI.modelos.PlanDeSuscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanSuscripcionRepository extends JpaRepository<PlanDeSuscripcion, Integer> {
    
    // Buscar planes por empresa ID
    Page<PlanDeSuscripcion> findByEmpresaId(Long empresaId, Pageable pageable);
    
    // Buscar planes por tipo de suscripción ID
    Page<PlanDeSuscripcion> findByTipoSuscripcionId(Integer tipoSuscripcionId, Pageable pageable);
    
    // Buscar planes hijos de un plan padre (si la relación existe)
    // Si no existe la relación planSuscripcionPadre en el modelo, comentar esta línea
    // Page<PlanDeSuscripcion> findByPlanSuscripcionPadreId(Integer planPadreId, Pageable pageable);
    
    // Buscar planes por empresa y tipo de suscripción
    Page<PlanDeSuscripcion> findByEmpresaIdAndTipoSuscripcionId(Long empresaId, Integer tipoSuscripcionId, Pageable pageable);
}
