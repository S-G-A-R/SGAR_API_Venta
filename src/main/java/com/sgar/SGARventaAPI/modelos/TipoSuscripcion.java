package com.sgar.SGARventaAPI.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SuscripcionNombre", length = 120, nullable = false)
    private String suscripcionNombre;

    // Precio del tipo de suscripción
    @Column(name = "Precio", precision = 8, scale = 2, nullable = false)
    private BigDecimal precio;

    // EL ATRIBUTO CLAVE: El límite de productos permitido por este tipo de suscripción
    @Column(name = "Limite", nullable = false)
    private Integer limite;

    @Column(name = "FechaInicio", nullable = true)
    private LocalDateTime fechaInicio;

    @Column(name = "FechaFin", nullable = true)
    private LocalDateTime fechaFin;

    // Relación Uno a Muchos con PlanDeSuscripcion
    @OneToMany(mappedBy = "tipoSuscripcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<PlanDeSuscripcion> planes;
}