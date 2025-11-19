package com.sgar.SGARventaAPI.modelos;

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

    // FK EmpresaId: Relación Muchos a Uno con Empresa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmpresaId", nullable = false)
    private Empresa empresa;

    // NUEVA FK TipoSuscripcionId: Relación Muchos a Uno con TipoSuscripcion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoSuscripcionId", nullable = false)
    private TipoSuscripcion tipoSuscripcion;
}