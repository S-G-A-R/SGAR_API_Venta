package com.sgar.SGARventaAPI.controller;

import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoGuardarDto;
import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoModificarDto;
import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoSalidaDto;
import com.sgar.SGARventaAPI.Servicios.Interfaces.IImagenProductosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/imagenes-productos")
@CrossOrigin(origins = "*")
@Tag(name = "Imágenes de Productos", description = "API para la gestión de imágenes de productos")
public class ImagenProductosController {

    private final IImagenProductosService imagenProductosService;

    @Autowired
    public ImagenProductosController(IImagenProductosService imagenProductosService) {
        this.imagenProductosService = imagenProductosService;
    }



    @Operation(summary = "Obtener metadatos de imagen por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ImagenProductoSalidaDto> obtenerPorId(@PathVariable Integer id) {
        try {
            ImagenProductoSalidaDto imagen = imagenProductosService.obtenerPorId(id);
            return ResponseEntity.ok(imagen);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener imagen como bytes")
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Integer id) {
        try {
            byte[] imagen = imagenProductosService.obtenerImagenPorId(id);
            ImagenProductoSalidaDto metadata = imagenProductosService.obtenerPorId(id);
            
            MediaType mediaType = MediaType.IMAGE_JPEG;
            if (metadata.getTipoMime() != null) {
                try {
                    mediaType = MediaType.parseMediaType(metadata.getTipoMime());
                } catch (Exception ignored) {}
            }
            
            return ResponseEntity.ok().contentType(mediaType).body(imagen);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }



    @Operation(summary = "Subir nueva imagen")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crear(
            @RequestParam("productoId") Integer productoId,
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam(required = false) String tipoMime) {
        try {
            validateImageOrThrow(imagen);
            
            ImagenProductoGuardarDto dto = new ImagenProductoGuardarDto();
            dto.setProductoId(productoId);
            dto.setImagen(imagen.getBytes());
            dto.setTipoMime(tipoMime != null ? tipoMime : imagen.getContentType());
            dto.setTamano(imagen.getSize());

            ImagenProductoSalidaDto creada = imagenProductosService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .location(URI.create("/api/imagenes-productos/" + creada.getId()))
                    .body(creada);
        } catch (IOException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @Operation(summary = "Actualizar imagen existente")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestParam(value = "productoId", required = false) Integer productoId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(required = false) String tipoMime) {
        try {
            ImagenProductoModificarDto dto = new ImagenProductoModificarDto();
            dto.setId(id);
            dto.setProductoId(productoId);
            populateDtoFromMultipart(dto, imagen, tipoMime);

            ImagenProductoSalidaDto actualizada = imagenProductosService.actualizar(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (IOException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @Operation(summary = "Eliminar imagen")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminar(@PathVariable Integer id) {
        try {
            imagenProductosService.eliminar(id);
            return ResponseEntity.ok(new MessageResponse("Imagen eliminada"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener imágenes de un producto")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Page<ImagenProductoSalidaDto>> obtenerPorProducto(
            @PathVariable Integer productoId, Pageable pageable) {
        try {
            Page<ImagenProductoSalidaDto> imagenes = imagenProductosService.obtenerPorProductoId(productoId, pageable);
            return ResponseEntity.ok(imagenes);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }



    @Operation(summary = "Eliminar todas las imágenes del producto")
    @DeleteMapping("/producto/{productoId}")
    public ResponseEntity<MessageResponse> eliminarTodasDeProducto(@PathVariable Integer productoId) {
        try {
            imagenProductosService.eliminarTodasDeProducto(productoId);
            return ResponseEntity.ok(new MessageResponse("Imágenes eliminadas"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar imágenes con filtros")
    @GetMapping("/buscar")
    public ResponseEntity<Page<ImagenProductoSalidaDto>> buscarConFiltros(
            @RequestParam(required = false) Integer productoId,
            @RequestParam(required = false) String tipoMime,
            Pageable pageable) {
        Page<ImagenProductoSalidaDto> imagenes = imagenProductosService.buscarConFiltros(productoId, tipoMime, pageable);
        return ResponseEntity.ok(imagenes);
    }



    // Clase interna para respuestas de mensaje
    private static class MessageResponse {
        private final String mensaje;

        public MessageResponse(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    // Métodos helper para validar y poblar DTOs
    private void validateImageOrThrow(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) {
            throw new IllegalArgumentException("Imagen vacía o nula");
        }
        String contentType = imagen.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen válida");
        }
    }

    private void populateDtoFromMultipart(ImagenProductoModificarDto dto, MultipartFile imagen, String tipoMime) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            validateImageOrThrow(imagen);
            dto.setImagen(imagen.getBytes());
            dto.setTamano(imagen.getSize());
            dto.setTipoMime(tipoMime != null ? tipoMime : imagen.getContentType());
        } else if (tipoMime != null) {
            dto.setTipoMime(tipoMime);
        }
    }
}