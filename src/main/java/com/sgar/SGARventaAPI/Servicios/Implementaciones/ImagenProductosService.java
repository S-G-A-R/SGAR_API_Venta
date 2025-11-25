package com.sgar.SGARventaAPI.Servicios.Implementaciones;

import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoGuardarDto;
import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoModificarDto;
import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoSalidaDto;
import com.sgar.SGARventaAPI.modelos.ImagenProductos;
import com.sgar.SGARventaAPI.modelos.Producto;
import com.sgar.SGARventaAPI.repositorios.ImagenProductosRepository;
import com.sgar.SGARventaAPI.repositorios.ProductoRepository;
import com.sgar.SGARventaAPI.Servicios.Interfaces.IImagenProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImagenProductosService implements IImagenProductosService {
    
    @Value("${app.imagen.maxSize:10485760}")
    private long maxImageSize;

    private final ImagenProductosRepository imagenProductosRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public ImagenProductosService(ImagenProductosRepository imagenProductosRepository, 
                                  ProductoRepository productoRepository) {
        this.imagenProductosRepository = imagenProductosRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public ImagenProductoSalidaDto obtenerPorId(Integer id) {
        ImagenProductos imagen = imagenProductosRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada con id: " + id));
        return toSalidaDto(imagen);
    }

    @Override
    public byte[] obtenerImagenPorId(Integer id) {
        ImagenProductos imagen = imagenProductosRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada con id: " + id));
        return imagen.getImagen();
    }

    @Override
    @Transactional
    public ImagenProductoSalidaDto crear(ImagenProductoGuardarDto dto) {
        validarDto(dto);
        ImagenProductos imagen = new ImagenProductos();
        imagen.setImagen(dto.getImagen());
        imagen.setTipoMime(dto.getTipoMime());
        imagen.setTamano(dto.getTamano());

        ImagenProductos guardada = imagenProductosRepository.save(imagen);

        // Si se indica que esta imagen debe ser la principal, y se pasa productoId, actualizar el producto.foto
        if (Boolean.TRUE.equals(dto.getEstablecerComoPrincipal()) && dto.getProductoId() != null) {
            Producto producto = productoRepository.findById(dto.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + dto.getProductoId()));
            producto.setFoto(guardada);
            productoRepository.save(producto);
        }

        return toSalidaDto(guardada);
    }

    @Override
    @Transactional
    public ImagenProductoSalidaDto actualizar(Integer id, ImagenProductoModificarDto dto) {
        ImagenProductos existente = imagenProductosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada con id: " + id));
        actualizarImagen(existente, dto);
        ImagenProductos actualizada = imagenProductosRepository.save(existente);

        // Si se solicita establecer como principal
        if (Boolean.TRUE.equals(dto.getEstablecerComoPrincipal())) {
            if (dto.getProductoId() != null) {
                Producto producto = productoRepository.findById(dto.getProductoId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + dto.getProductoId()));
                producto.setFoto(actualizada);
                productoRepository.save(producto);
            }
        } else if (dto.getEstablecerComoPrincipal() != null && Boolean.FALSE.equals(dto.getEstablecerComoPrincipal())) {
            // Quitar como principal: si se pasa productoId, limpiar esa relación; si no, buscar producto que tiene esta imagen como foto
            if (dto.getProductoId() != null) {
                Producto producto = productoRepository.findById(dto.getProductoId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + dto.getProductoId()));
                if (producto.getFoto() != null && producto.getFoto().getId().equals(actualizada.getId())) {
                    producto.setFoto(null);
                    productoRepository.save(producto);
                }
            } else {
                productoRepository.findByFotoId(actualizada.getId()).ifPresent(prod -> {
                    prod.setFoto(null);
                    productoRepository.save(prod);
                });
            }
        }
        return toSalidaDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!imagenProductosRepository.existsById(id)) {
            throw new IllegalArgumentException("Imagen no encontrada con id: " + id);
        }
        ImagenProductos imagen = imagenProductosRepository.findById(id).orElse(null);
        if (imagen != null) {
            // Si algún producto referencia esta imagen como foto, limpiarlo
            productoRepository.findByFotoId(imagen.getId()).ifPresent(prod -> {
                prod.setFoto(null);
                productoRepository.save(prod);
            });
        }
        imagenProductosRepository.deleteById(id);
    }

    @Override
    public ImagenProductoSalidaDto obtenerImagenPrincipalPorProducto(Integer productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + productoId));
        if (producto.getFoto() == null) {
            throw new IllegalArgumentException("El producto no tiene una imagen principal establecida");
        }
        return toSalidaDto(producto.getFoto());
    }

    @Override
    @Transactional
    public void eliminarImagenPrincipalDeProducto(Integer productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + productoId));
        if (producto.getFoto() != null) {
            Integer imagenId = producto.getFoto().getId();
            producto.setFoto(null);
            productoRepository.save(producto);
            imagenProductosRepository.deleteById(imagenId);
        }
    }

    @Override
    public Page<ImagenProductoSalidaDto> buscarConFiltros(String tipoMime, Pageable pageable) {
        return imagenProductosRepository.buscarConFiltros(tipoMime, pageable)
                .map(this::toSalidaDto);
    }

    // Métodos privados
    private void validarDto(ImagenProductoGuardarDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        
        }
        if (dto.getImagen() == null || dto.getImagen().length == 0) {
            throw new IllegalArgumentException("La imagen es requerida");
        }
        if (dto.getImagen().length > maxImageSize) {
            throw new IllegalArgumentException("La imagen excede el tamaño máximo permitido");
        }
        if (dto.getTipoMime() == null || dto.getTipoMime().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo MIME es requerido");
        }
        if (dto.getTamano() == null || dto.getTamano() <= 0) {
            throw new IllegalArgumentException("El tamaño debe ser positivo");
        }
    }

    private void actualizarImagen(ImagenProductos existente, ImagenProductoModificarDto dto) {
        // Nota: ImagenProductos ya no almacena productoId; si se pasa productoId
        // en el DTO se usa solo para marcar como principal o para limpiar la relación.

        if (dto.getImagen() != null && dto.getImagen().length > 0) {
            if (dto.getImagen().length > maxImageSize) {
                throw new IllegalArgumentException("La imagen excede el tamaño máximo permitido");
            }
            existente.setImagen(dto.getImagen());
        }
        
        if (dto.getTipoMime() != null && !dto.getTipoMime().trim().isEmpty()) {
            existente.setTipoMime(dto.getTipoMime());
        }

        if (dto.getTamano() != null && dto.getTamano() > 0) {
            existente.setTamano(dto.getTamano());
        }
    }

    private ImagenProductoSalidaDto toSalidaDto(ImagenProductos imagen) {
        ImagenProductoSalidaDto dto = new ImagenProductoSalidaDto();
        dto.setId(imagen.getId());
        // Determinar si esta imagen es la foto principal de algún producto
        productoRepository.findByFotoId(imagen.getId()).ifPresent(prod -> {
            dto.setProductoId(prod.getId());
            dto.setNombreProducto(prod.getNombre());
        });
        dto.setTipoMime(imagen.getTipoMime());
        dto.setTamano(imagen.getTamano());
        dto.setTieneImagen(imagen.getImagen() != null && imagen.getImagen().length > 0);
        // Es principal si existe un producto que referencia esta imagen como foto
        boolean esPrincipal = productoRepository.findByFotoId(imagen.getId()).isPresent();
        dto.setEsPrincipal(esPrincipal);
        return dto;
    }
}