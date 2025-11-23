package com.sgar.SGARventaAPI.dto.PlanSuscripcion;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeSuscripcionRequets {
    private Integer asociadoId;
    private Integer tipoSuscripcionId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean activo;
}
