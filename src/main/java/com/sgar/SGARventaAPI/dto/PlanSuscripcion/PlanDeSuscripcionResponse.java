package com.sgar.SGARventaAPI.dto.PlanSuscripcion;

import com.sgar.SGARventaAPI.dto.TipoSuscripcion.TipoSuscripcionResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeSuscripcionResponse {
    private Integer id;
    private Integer asociadoId;
    private TipoSuscripcionResponse tipoSuscripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean activo;
}
