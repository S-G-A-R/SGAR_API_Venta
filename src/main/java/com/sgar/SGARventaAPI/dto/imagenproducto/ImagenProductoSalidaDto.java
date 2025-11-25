package com.sgar.SGARventaAPI.dto.imagenproducto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImagenProductoSalidaDto {
    private Integer id;
    private Integer productoId;
    private String nombreProducto;
    
    // Metadatos de la imagen
    private String tipoMime;
    private Long tamano;
    
    // Nota: No incluimos el byte[] imagen en la salida por defecto 
    // para evitar respuestas JSON muy grandes
    private boolean tieneImagen;
    // Indica si esta imagen es la imagen principal (producto.foto)
    private Boolean esPrincipal = false;
    
    // URL para obtener la imagen (opcional, para futuras implementaciones)
    private String urlImagen;
}