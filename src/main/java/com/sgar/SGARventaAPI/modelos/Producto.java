package com.sgar.SGARventaAPI.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FK CategoriaProductoId: Relación Muchos a Uno con CategoriaProducto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoriaProductoId", nullable = false)
    private CategoriaProducto categoriaProducto;

    // FK EmpresaId: El producto pertenece a una empresa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmpresaId", nullable = false)
    private Empresa empresa;

    @Column(name = "Nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "Precio", precision = 8, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "Tipo", length = 150, nullable = false)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdFoto")
    private ImagenProductos foto;

    @Lob
    @Column(name = "Descripcion")
    private String descripcion;
    
}