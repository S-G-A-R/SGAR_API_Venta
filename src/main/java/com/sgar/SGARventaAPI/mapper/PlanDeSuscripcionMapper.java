package com.sgar.SGARventaAPI.mapper;

import com.sgar.SGARventaAPI.dto.PlanSuscripcion.PlanDeSuscripcionRequets;
import com.sgar.SGARventaAPI.dto.PlanSuscripcion.PlanDeSuscripcionResponse;
import com.sgar.SGARventaAPI.modelos.PlanDeSuscripcion;
import com.sgar.SGARventaAPI.modelos.TipoSuscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanDeSuscripcionMapper {

    @Autowired
    private TipoSuscripcionMapper tipoSuscripcionMapper;

    public PlanDeSuscripcionResponse toDTO(PlanDeSuscripcion plan) {
        if (plan == null) {
            return null;
        }
        PlanDeSuscripcionResponse dto = new PlanDeSuscripcionResponse();
        dto.setId(plan.getId());
        dto.setAsociadoId(plan.getAsociadoId());
        dto.setFechaInicio(plan.getFechaInicio());
        dto.setFechaFin(plan.getFechaFin());
        dto.setActivo(plan.getActivo());
        dto.setTipoSuscripcion(tipoSuscripcionMapper.toDTO(plan.getTipoSuscripcion()));
        return dto;
    }

    public PlanDeSuscripcion toEntity(PlanDeSuscripcionRequets dto) {
        if (dto == null) {
            return null;
        }
        PlanDeSuscripcion plan = new PlanDeSuscripcion();
        // Configurar asociado
        if (dto.getAsociadoId() != null) {
            plan.setAsociadoId(dto.getAsociadoId());
        }
        // Configurar tipo de suscripción
        if (dto.getTipoSuscripcionId() != null) {
            TipoSuscripcion tipoSuscripcion = new TipoSuscripcion();
            tipoSuscripcion.setId(dto.getTipoSuscripcionId());
            plan.setTipoSuscripcion(tipoSuscripcion);
        }
        return plan;
    }
}
