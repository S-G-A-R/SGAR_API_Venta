package com.sgar.SGARventaAPI.Servicios.Interfaces;

import com.sgar.SGARventaAPI.modelos.PlanDeSuscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPlanSuscripcionService {
    
    // Crear un nuevo plan de suscripción
    PlanDeSuscripcion crearPlan(Integer asociadoId, Integer tipoPlanId);
    
    // Actualizar un plan existente
    PlanDeSuscripcion actualizarPlan(Integer id, PlanDeSuscripcion plan);
    
    // Eliminar un plan por ID
    void eliminarPlan(Integer id);
    
    // Obtener un plan por ID
    Optional<PlanDeSuscripcion> obtenerPlanPorId(Integer id);
    
    // Obtener todos los planes con paginación
    Page<PlanDeSuscripcion> obtenerTodosLosPlanes(Pageable pageable);
    
    // Buscar planes por asociado ID
    Page<PlanDeSuscripcion> buscarPlanesPorAsociado(Long asociadoId, Pageable pageable);
    
    // Buscar planes por tipo de suscripción ID
    Page<PlanDeSuscripcion> buscarPlanesPorTipoSuscripcion(Integer tipoSuscripcionId, Pageable pageable);
    
    // Buscar planes hijos de un plan padre (comentado hasta agregar la relación en el modelo)
    // Page<PlanDeSuscripcion> buscarPlanesPorPlanPadre(Integer planPadreId, Pageable pageable);
    
    // Buscar planes por asociado y tipo de suscripción
    Page<PlanDeSuscripcion> buscarPlanesPorAsociadoYTipo(Long asociadoId, Integer tipoSuscripcionId, Pageable pageable);
}
