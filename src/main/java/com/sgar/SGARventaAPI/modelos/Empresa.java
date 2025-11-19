package com.sgar.SGARventaAPI.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Asociadold", nullable = false)
    private Long asociadoId;

    @Column(name = "NombreEmpresa", length = 150, nullable = false)
    private String nombreEmpresa;

    // Relación Uno a Muchos con PlanDeSuscripcion
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<PlanDeSuscripcion> planes;

    // Relación Uno a Muchos con Producto
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<Producto> productos;
}