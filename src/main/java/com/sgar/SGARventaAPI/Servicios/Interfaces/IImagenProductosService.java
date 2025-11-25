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
    ImagenProductoSalidaDto obtenerImagenPrincipalPorProducto(Integer productoId);
    void eliminarImagenPrincipalDeProducto(Integer productoId);

    // Operaciones con filtros
    Page<ImagenProductoSalidaDto> buscarConFiltros(String tipoMime, Pageable pageable);
}