package com.sgar.SGARventaAPI.mapper;

import com.sgar.SGARventaAPI.dto.TipoSuscripcion.TipoSuscripcionRequets;
import com.sgar.SGARventaAPI.dto.TipoSuscripcion.TipoSuscripcionResponse;
import com.sgar.SGARventaAPI.modelos.TipoSuscripcion;
import org.springframework.stereotype.Component;

@Component
public class TipoSuscripcionMapper {

    public TipoSuscripcionResponse toDTO(TipoSuscripcion tipoSuscripcion) {
        if (tipoSuscripcion == null) {
            return null;
        }
        TipoSuscripcionResponse dto = new TipoSuscripcionResponse();
        dto.setId(tipoSuscripcion.getId());
        dto.setSuscripcionNombre(tipoSuscripcion.getSuscripcionNombre());
        dto.setPrecio(tipoSuscripcion.getPrecio());
        dto.setLimite(tipoSuscripcion.getLimite());
        dto.setDuracionDias(tipoSuscripcion.getDuracionDias());
        return dto;
    }

    public TipoSuscripcion toEntity(TipoSuscripcionRequets dto) {
        if (dto == null) {
            return null;
        }
        
        TipoSuscripcion tipoSuscripcion = new TipoSuscripcion();
        tipoSuscripcion.setSuscripcionNombre(dto.getSuscripcionNombre());
        tipoSuscripcion.setPrecio(dto.getPrecio());
        tipoSuscripcion.setLimite(dto.getLimite());
        tipoSuscripcion.setDuracionDias(dto.getDuracionDias());
        
        return tipoSuscripcion;
    }

    public void updateEntityFromDTO(TipoSuscripcion tipoSuscripcion, TipoSuscripcionRequets dto) {
        if (dto == null || tipoSuscripcion == null) {
            return;
        }
        
        tipoSuscripcion.setSuscripcionNombre(dto.getSuscripcionNombre());
        tipoSuscripcion.setPrecio(dto.getPrecio());
        tipoSuscripcion.setLimite(dto.getLimite());
        tipoSuscripcion.setDuracionDias(dto.getDuracionDias());
    }
}
