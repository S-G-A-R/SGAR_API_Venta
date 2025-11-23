package com.sgar.SGARventaAPI.modelos;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PlanDeSuscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Asociado dueño del plan (un asociado puede tener varias empresas bajo el mismo plan)
    @Column(name = "Asociadold", nullable = false)
    private Integer asociadoId;

    // Relación Muchos a Uno con TipoSuscripcion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoSuscripcionId", nullable = false)
    private TipoSuscripcion tipoSuscripcion;

    @Column(name = "FechaInicio", nullable = true)
    private LocalDateTime fechaInicio;

    @Column(name = "FechaFin", nullable = true)
    private LocalDateTime fechaFin;

    @Column(name = "Activo", nullable = true)
    private Boolean activo;
}