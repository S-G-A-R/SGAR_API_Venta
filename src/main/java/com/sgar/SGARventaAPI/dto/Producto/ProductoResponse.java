package com.sgar.SGARventaAPI.dto.Producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.sgar.SGARventaAPI.dto.CategoriaProducto.CategoriaProductoResponse;
import com.sgar.SGARventaAPI.dto.Empresa.EmpresaResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private Integer id;
    private String nombre;
    private BigDecimal precio;
    private String tipo;
    private String descripcion;
    private CategoriaProductoResponse categoriaProducto;
    private EmpresaResponse empresa;
}
