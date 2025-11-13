package com.sgar.SGARventaAPI.dto.imagenproducto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImagenProductoCompletaDto {
    private Integer id;
    private Integer productoId;
    private String nombreProducto;
    
    // Imagen completa en formato Base64 para envío por JSON
    private String imagenBase64;
    
    // Metadatos de la imagen
    private String tipoMime;
    private Long tamano;
    
    // Constructor para convertir byte[] a Base64
    public void setImagenFromBytes(byte[] imagen) {
        if (imagen != null) {
            this.imagenBase64 = java.util.Base64.getEncoder().encodeToString(imagen);
        }
    }
}