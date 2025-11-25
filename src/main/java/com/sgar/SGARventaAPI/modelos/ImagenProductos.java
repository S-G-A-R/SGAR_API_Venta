package com.sgar.SGARventaAPI.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagenProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob // Usado para mapear BLOB (Large Object).
    @Column(name = "Imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen; // Se recomienda usar byte[] para LONGBLOB en JPA

    @Column(name = "TipoMime", length = 100)
    private String tipoMime;

    @Column(name = "Tamano")
    private Long tamano;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getImagen() {
        return imagen;
    }
    
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
