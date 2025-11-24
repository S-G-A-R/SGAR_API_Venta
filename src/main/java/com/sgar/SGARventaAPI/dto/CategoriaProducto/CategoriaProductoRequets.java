package com.sgar.SGARventaAPI.dto.CategoriaProducto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProductoRequets {
    private String nombreCat;
    private Long asociadoId;
}
