package com.sgar.SGARventaAPI.dto.PlanSuscripcion;

import com.sgar.SGARventaAPI.dto.Empresa.EmpresaResponse;
import com.sgar.SGARventaAPI.dto.TipoSuscripcion.TipoSuscripcionResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeSuscripcionResponse {
    private Integer id;
    private EmpresaResponse empresa;
    private TipoSuscripcionResponse tipoSuscripcion;
}
