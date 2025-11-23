package com.sgar.SGARventaAPI.Servicios.Implementaciones;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IPlanSuscripcionService;
import com.sgar.SGARventaAPI.modelos.PlanDeSuscripcion;
import com.sgar.SGARventaAPI.modelos.TipoSuscripcion;
import com.sgar.SGARventaAPI.repositorios.PlanSuscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PlanSuscripcionService implements IPlanSuscripcionService {

    @Autowired
    private PlanSuscripcionRepository planRepository;

    @Autowired
    private TipoSuscripcionService tipoSuscripcionService;

   @Override
    // CORRECCIÓN 1: Cambiado Integer a Long para coincidir con la Interfaz
    public PlanDeSuscripcion crearPlan(Integer asociadoId, Integer tipoPlanId) {
        
        // CORRECCIÓN 2: Usar el nombre correcto de la entidad (TipoSuscripcion)
        // Nota: Asumo que tu servicio devuelve un Optional<TipoSuscripcion>
        TipoSuscripcion tipo = tipoSuscripcionService.obtenerTipoSuscripcionPorId(tipoPlanId) // Si este método espera int, cámbialo a Long en el servicio de TipoSuscripcion también
                .orElseThrow(() -> new RuntimeException("El tipo de plan no existe"));
        
        // 2. Calculamos las fechas
        LocalDateTime fechaInicio = LocalDateTime.now();
        
        // Asegúrate de que la entidad tenga el método getDuracionDias() (o getLimite si usas eso como días)
        LocalDateTime fechaFin = fechaInicio.plusDays(tipo.getDuracionDias()); // O el campo que uses para días

        // 3. Construimos el objeto
        PlanDeSuscripcion nuevoPlan = new PlanDeSuscripcion();
        nuevoPlan.setAsociadoId(asociadoId);
        
        // Asegúrate de que PlanDeSuscripcion espere un objeto de tipo TipoSuscripcion
        nuevoPlan.setTipoSuscripcion(tipo); // Cambié setTipoPlan a setTipoSuscripcion para consistencia
        
        nuevoPlan.setFechaInicio(fechaInicio);
        nuevoPlan.setFechaFin(fechaFin);
        nuevoPlan.setActivo(true);

        // 4. Guardamos
        return planRepository.save(nuevoPlan);
    }

    @Override
    public PlanDeSuscripcion actualizarPlan(Integer id, PlanDeSuscripcion plan) {
        Optional<PlanDeSuscripcion> planExistente = planRepository.findById(id);
        if (planExistente.isPresent()) {
            PlanDeSuscripcion planActualizado = planExistente.get();
            planActualizado.setAsociadoId(plan.getAsociadoId());
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
    public Page<PlanDeSuscripcion> buscarPlanesPorAsociado(Long asociadoId, Pageable pageable) {
        return planRepository.findByAsociadoId(asociadoId, pageable);
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
    public Page<PlanDeSuscripcion> buscarPlanesPorAsociadoYTipo(Long asociadoId, Integer tipoSuscripcionId, Pageable pageable) {
        return planRepository.findByAsociadoIdAndTipoSuscripcionId(asociadoId, tipoSuscripcionId, pageable);
    }
}
