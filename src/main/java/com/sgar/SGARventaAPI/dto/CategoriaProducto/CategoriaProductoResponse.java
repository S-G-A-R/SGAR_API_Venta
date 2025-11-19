package com.sgar.SGARventaAPI.dto.CategoriaProducto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProductoResponse {
    private Integer id;
    private String nombreCat;
}
