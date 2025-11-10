package com.sgar.SGARventaAPI.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación Muchos a Uno con CategoriaProducto (FK CategoriaProductoId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoriaProductoId", nullable = false)
    private CategoriaProducto categoriaProducto;

    @Column(name = "planDeSuscripcionId", nullable = false)
    private Integer planDeSuscripcionId; // FK al PlanDeSuscripcionId, manteniendo el diseño original

    @Column(name = "Nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "Precio", precision = 8, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "Tipo", length = 150, nullable = false)
    private String tipo;

    @Lob // Para mapear el tipo TEXT
    @Column(name = "Descripcion")
    private String descripcion;

    // Relación Muchos a Uno con TipoSuscripcion (FK TipoSuscripcionId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoSuscripcionId", nullable = false)
    private TipoSuscripcion tipoSuscripcion;
}
