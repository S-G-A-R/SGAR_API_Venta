package com.sgar.SGARventaAPI.dto.imagenproducto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ImagenProductoModificarDto {
    
    @NotNull(message = "El ID de la imagen es requerido")
    private Integer id;
    
    private Integer productoId;
    
    private byte[] imagen;
    
    @Size(max = 100, message = "El tipo MIME no puede exceder 100 caracteres")
    private String tipoMime;
    
    @Positive(message = "El tamaño debe ser positivo")
    private Long tamano;
}