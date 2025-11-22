package com.sgar.SGARventaAPI.dto.TipoSuscripcion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoSuscripcionRequets {
    private String suscripcionNombre;
    private BigDecimal precio;
    private Integer limite;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
