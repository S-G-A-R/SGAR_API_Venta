package com.sgar.SGARventaAPI.dto.imagenproducto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ImagenProductoGuardarDto {
    
    @NotNull(message = "El ID del producto es requerido")
    private Integer productoId;
    
    @NotNull(message = "La imagen es requerida")
    private byte[] imagen;
    
    @NotBlank(message = "El tipo MIME es requerido")
    @Size(max = 100, message = "El tipo MIME no puede exceder 100 caracteres")
    private String tipoMime;
    
    @NotNull(message = "El tamaño es requerido")
    @Positive(message = "El tamaño debe ser positivo")
    private Long tamano;

    // Si true, esta imagen será marcada como la imagen principal del producto (producto.foto)
    private Boolean establecerComoPrincipal = false;
}