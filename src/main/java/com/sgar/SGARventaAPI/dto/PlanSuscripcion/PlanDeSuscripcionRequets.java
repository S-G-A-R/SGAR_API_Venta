package com.sgar.SGARventaAPI.dto.PlanSuscripcion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeSuscripcionRequets {
    private Long empresaId;
    private Integer tipoSuscripcionId;
}
