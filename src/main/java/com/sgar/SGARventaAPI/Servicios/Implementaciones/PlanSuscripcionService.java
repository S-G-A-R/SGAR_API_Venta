package com.sgar.SGARventaAPI.Servicios.Implementaciones;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IPlanSuscripcionService;
import com.sgar.SGARventaAPI.modelos.PlanDeSuscripcion;
import com.sgar.SGARventaAPI.repositorios.PlanSuscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PlanSuscripcionService implements IPlanSuscripcionService {

    @Autowired
    private PlanSuscripcionRepository planRepository;

    @Override
    public PlanDeSuscripcion crearPlan(PlanDeSuscripcion plan) {
        return planRepository.save(plan);
    }

    @Override
    public PlanDeSuscripcion actualizarPlan(Integer id, PlanDeSuscripcion plan) {
        Optional<PlanDeSuscripcion> planExistente = planRepository.findById(id);
        if (planExistente.isPresent()) {
            PlanDeSuscripcion planActualizado = planExistente.get();
            planActualizado.setEmpresa(plan.getEmpresa());
            planActualizado.setTipoSuscripcion(plan.getTipoSuscripcion());
            return planRepository.save(planActualizado);
        }
        throw new RuntimeException("Plan de suscripción no encontrado con ID: " + id);
    }

    @Override
    public void eliminarPlan(Integer id) {
        if (!planRepository.existsById(id)) {
            throw new RuntimeException("Plan de suscripción no encontrado con ID: " + id);
        }
        planRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanDeSuscripcion> obtenerPlanPorId(Integer id) {
        return planRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanDeSuscripcion> obtenerTodosLosPlanes(Pageable pageable) {
        return planRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanDeSuscripcion> buscarPlanesPorEmpresa(Long empresaId, Pageable pageable) {
        return planRepository.findByEmpresaId(empresaId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanDeSuscripcion> buscarPlanesPorTipoSuscripcion(Integer tipoSuscripcionId, Pageable pageable) {
        return planRepository.findByTipoSuscripcionId(tipoSuscripcionId, pageable);
    }

    // Comentado hasta agregar la relación planSuscripcionPadre en el modelo
    // @Override
    // @Transactional(readOnly = true)
    // public Page<PlanDeSuscripcion> buscarPlanesPorPlanPadre(Integer planPadreId, Pageable pageable) {
    //     return planRepository.findByPlanSuscripcionPadreId(planPadreId, pageable);
    // }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanDeSuscripcion> buscarPlanesPorEmpresaYTipo(Long empresaId, Integer tipoSuscripcionId, Pageable pageable) {
        return planRepository.findByEmpresaIdAndTipoSuscripcionId(empresaId, tipoSuscripcionId, pageable);
    }
}
