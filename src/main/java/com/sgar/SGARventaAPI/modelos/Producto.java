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

    // NUEVA FK PlanDeSuscripcionId: El producto pertenece a un plan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PlanDeSuscripcionId", nullable = false)
    private PlanDeSuscripcion planDeSuscripcion;

    @Column(name = "Nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "Precio", precision = 8, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "Tipo", length = 150, nullable = false)
    private String tipo;

    @Lob
    @Column(name = "Descripcion")
    private String descripcion;
    
    // Relación Uno a Muchos con ImagenProducto
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<ImagenProductos> imagenes;
}