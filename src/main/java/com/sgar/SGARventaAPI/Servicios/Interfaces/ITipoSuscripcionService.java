package com.sgar.SGARventaAPI.Servicios.Interfaces;

import com.sgar.SGARventaAPI.modelos.TipoSuscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface ITipoSuscripcionService {
    
    TipoSuscripcion crearTipoSuscripcion(TipoSuscripcion tipoSuscripcion);
    
    Page<TipoSuscripcion> obtenerTodosTiposSuscripcion(Pageable pageable);
    
    Optional<TipoSuscripcion> obtenerTipoSuscripcionPorId(Integer id);
    
    TipoSuscripcion actualizarTipoSuscripcion(Integer id, TipoSuscripcion tipoSuscripcion);
    
    void eliminarTipoSuscripcion(Integer id);
    
    Page<TipoSuscripcion> buscarPorNombre(String nombre, Pageable pageable);
    
    Page<TipoSuscripcion> buscarPorRangoPrecio(BigDecimal minPrecio, BigDecimal maxPrecio, Pageable pageable);
    
    Page<TipoSuscripcion> buscarPorLimite(Integer limite, Pageable pageable);
}
