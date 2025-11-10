package com.sgar.SGARventaAPI.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ImagenProducto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagenProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación Muchos a Uno con Producto (FK ProductoId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductoId", nullable = false)
    private Productos producto;

    @Lob // Usado para mapear BLOB (Large Object).
    @Column(name = "Imagen")
    private byte[] imagen; // Se recomienda usar byte[] para BLOB en JPA
}
