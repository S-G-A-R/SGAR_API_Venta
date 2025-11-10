package com.sgar.SGARventaAPI.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

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

    // Se usa BigDecimal para DECIMAL(8,2)
    @Column(name = "Precio", precision = 8, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "Limite", nullable = false)
    private Integer limite;

    @Column(name = "ProductoId", nullable = false)
    private Integer productoId; // NOTA: Esto parece ser un error de diseño en el ER,
                                // pues un TipoSuscripcion no debería tener un ProductoId NOT NULL.
                                // Lo mantengo por fidelidad al diagrama.
}