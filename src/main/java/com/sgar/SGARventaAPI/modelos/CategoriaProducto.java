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
    private Integer id; // Usamos Integer ya que el tipo original es INT

    @Column(name = "NombreCat", length = 120, nullable = false)
    private String nombreCat;
}