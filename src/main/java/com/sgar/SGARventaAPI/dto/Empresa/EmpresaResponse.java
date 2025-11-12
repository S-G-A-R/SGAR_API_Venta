package com.sgar.SGARventaAPI.dto.Empresa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {
    private Long id;
    private Long asociadoId;
    private String nombreEmpresa;
}
