package com.sgar.SGARventaAPI.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(name = "NombreCat", length = 120, nullable = false)
    private String nombreCat;

    @Column(name = "Asociadold", nullable = true)
    private Long asociadoId;

    // Relación Uno a Muchos con Producto
    @OneToMany(mappedBy = "categoriaProducto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.Set<Producto> productos;
}