package com.sgar.SGARventaAPI.mapper;

import com.sgar.SGARventaAPI.dto.PlanSuscripcion.PlanDeSuscripcionRequets;
import com.sgar.SGARventaAPI.dto.PlanSuscripcion.PlanDeSuscripcionResponse;
import com.sgar.SGARventaAPI.modelos.Empresa;
import com.sgar.SGARventaAPI.modelos.PlanDeSuscripcion;
import com.sgar.SGARventaAPI.modelos.TipoSuscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanDeSuscripcionMapper {

    @Autowired
    private EmpresaMapper empresaMapper;

    @Autowired
    private TipoSuscripcionMapper tipoSuscripcionMapper;

    public PlanDeSuscripcionResponse toDTO(PlanDeSuscripcion plan) {
        if (plan == null) {
            return null;
        }
        PlanDeSuscripcionResponse dto = new PlanDeSuscripcionResponse();
        dto.setId(plan.getId());
        dto.setEmpresa(empresaMapper.toDTO(plan.getEmpresa()));
        dto.setTipoSuscripcion(tipoSuscripcionMapper.toDTO(plan.getTipoSuscripcion()));
        return dto;
    }

    public PlanDeSuscripcion toEntity(PlanDeSuscripcionRequets dto) {
        if (dto == null) {
            return null;
        }
        PlanDeSuscripcion plan = new PlanDeSuscripcion();
        
        // Configurar empresa
        if (dto.getEmpresaId() != null) {
            Empresa empresa = new Empresa();
            empresa.setId(dto.getEmpresaId());
            plan.setEmpresa(empresa);
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
