package com.sgar.SGARventaAPI.dto.Producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequets {
    private String nombre;
    private BigDecimal precio;
    private String tipo;
    private String descripcion;
    private Integer categoriaProductoId;
    private Long empresaId;
    private Integer fotoId;
}
