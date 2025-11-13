package com.sgar.SGARventaAPI.Servicios.Interfaces;

import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoGuardarDto;
import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoModificarDto;
import com.sgar.SGARventaAPI.dto.imagenproducto.ImagenProductoSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IImagenProductosService {
    
    // Operaciones CRUD básicas
    ImagenProductoSalidaDto obtenerPorId(Integer id);
    byte[] obtenerImagenPorId(Integer id);
    ImagenProductoSalidaDto crear(ImagenProductoGuardarDto dto);
    ImagenProductoSalidaDto actualizar(Integer id, ImagenProductoModificarDto dto);
    void eliminar(Integer id);
    
    // Operaciones específicas por producto
    Page<ImagenProductoSalidaDto> obtenerPorProductoId(Integer productoId, Pageable pageable);
    void eliminarTodasDeProducto(Integer productoId);
    
    // Operaciones con filtros
    Page<ImagenProductoSalidaDto> buscarConFiltros(Integer productoId, String tipoMime, Pageable pageable);
}