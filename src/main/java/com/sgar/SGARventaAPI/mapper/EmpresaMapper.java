package com.sgar.SGARventaAPI.mapper;

import com.sgar.SGARventaAPI.dto.Empresa.EmpresaRequets;
import com.sgar.SGARventaAPI.dto.Empresa.EmpresaResponse;
import com.sgar.SGARventaAPI.modelos.Empresa;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public EmpresaResponse toDTO(Empresa empresa) {
        if (empresa == null) {
            return null;
        }
        EmpresaResponse dto = new EmpresaResponse();
        dto.setId(empresa.getId());
        dto.setAsociadoId(empresa.getAsociadoId());
        dto.setNombreEmpresa(empresa.getNombreEmpresa());
        return dto;
    }

    public Empresa toEntity(EmpresaRequets dto) {
        if (dto == null) {
            return null;
        }
        Empresa empresa = new Empresa();
        empresa.setAsociadoId(dto.getAsociadoId());
        empresa.setNombreEmpresa(dto.getNombreEmpresa());
        return empresa;
    }

    public void updateEntityFromDTO(EmpresaRequets dto, Empresa empresa) {
        if (dto == null || empresa == null) {
            return;
        }
        empresa.setAsociadoId(dto.getAsociadoId());
        empresa.setNombreEmpresa(dto.getNombreEmpresa());
    }
}
