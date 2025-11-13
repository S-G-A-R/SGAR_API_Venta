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
        
        Producto producto = productoRepository.findById(dto.getProductoId())
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + dto.getProductoId()));

        ImagenProductos imagen = new ImagenProductos();
        imagen.setProducto(producto);
        imagen.setImagen(dto.getImagen());
        imagen.setTipoMime(dto.getTipoMime());
        imagen.setTamano(dto.getTamano());
        
        ImagenProductos guardada = imagenProductosRepository.save(imagen);
        return toSalidaDto(guardada);
    }

    @Override
    @Transactional
    public ImagenProductoSalidaDto actualizar(Integer id, ImagenProductoModificarDto dto) {
        ImagenProductos existente = imagenProductosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada con id: " + id));

        actualizarImagen(existente, dto);
        ImagenProductos actualizada = imagenProductosRepository.save(existente);
        return toSalidaDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!imagenProductosRepository.existsById(id)) {
            throw new IllegalArgumentException("Imagen no encontrada con id: " + id);
        }
        imagenProductosRepository.deleteById(id);
    }

    @Override
    public Page<ImagenProductoSalidaDto> obtenerPorProductoId(Integer productoId, Pageable pageable) {
        if (!productoRepository.existsById(productoId)) {
            throw new IllegalArgumentException("Producto no encontrado con id: " + productoId);
        }
        return imagenProductosRepository.findByProductoId(productoId, pageable)
                .map(this::toSalidaDto);
    }

    @Override
    @Transactional
    public void eliminarTodasDeProducto(Integer productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new IllegalArgumentException("Producto no encontrado con id: " + productoId);
        }
        imagenProductosRepository.deleteByProductoId(productoId);
    }

    @Override
    public Page<ImagenProductoSalidaDto> buscarConFiltros(Integer productoId, String tipoMime, Pageable pageable) {
        return imagenProductosRepository.buscarConFiltros(productoId, tipoMime, pageable)
                .map(this::toSalidaDto);
    }

    // Métodos privados
    private void validarDto(ImagenProductoGuardarDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }
        if (dto.getProductoId() == null) {
            throw new IllegalArgumentException("El ID del producto es requerido");
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
        if (dto.getProductoId() != null && !dto.getProductoId().equals(existente.getProducto().getId())) {
            Producto nuevoProducto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + dto.getProductoId()));
            existente.setProducto(nuevoProducto);
        }

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
        dto.setProductoId(imagen.getProducto().getId());
        dto.setNombreProducto(imagen.getProducto().getNombre());
        dto.setTipoMime(imagen.getTipoMime());
        dto.setTamano(imagen.getTamano());
        dto.setTieneImagen(imagen.getImagen() != null && imagen.getImagen().length > 0);
        return dto;
    }
}