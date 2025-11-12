package com.sgar.SGARventaAPI.Servicios.Implementaciones;

import com.sgar.SGARventaAPI.Servicios.Interfaces.ITipoSuscripcionService;
import com.sgar.SGARventaAPI.modelos.TipoSuscripcion;
import com.sgar.SGARventaAPI.repositorios.TipoSuscripcionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;


@Service
public class TipoSuscripcionService implements ITipoSuscripcionService {

    @Autowired
    private TipoSuscripcionRepository tipoSuscripcionRepository;

    @Override
    @Transactional
    public TipoSuscripcion crearTipoSuscripcion(TipoSuscripcion tipoSuscripcion) {
        return tipoSuscripcionRepository.save(tipoSuscripcion);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoSuscripcion> obtenerTodosTiposSuscripcion(Pageable pageable) {
        return tipoSuscripcionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoSuscripcion> obtenerTipoSuscripcionPorId(Integer id) {
        return tipoSuscripcionRepository.findById(id);
    }

    @Override
    @Transactional
    public TipoSuscripcion actualizarTipoSuscripcion(Integer id, TipoSuscripcion tipoSuscripcion) {
        return tipoSuscripcionRepository.findById(id)
                .map(tipoExistente -> {
                    tipoExistente.setSuscripcionNombre(tipoSuscripcion.getSuscripcionNombre());
                    tipoExistente.setPrecio(tipoSuscripcion.getPrecio());
                    tipoExistente.setLimite(tipoSuscripcion.getLimite());
                    return tipoSuscripcionRepository.save(tipoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Tipo de suscripción no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public void eliminarTipoSuscripcion(Integer id) {
        if (!tipoSuscripcionRepository.existsById(id)) {
            throw new RuntimeException("Tipo de suscripción no encontrado con id: " + id);
        }
        tipoSuscripcionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoSuscripcion> buscarPorNombre(String nombre, Pageable pageable) {
        return tipoSuscripcionRepository.findBySuscripcionNombreContainingIgnoreCase(nombre, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoSuscripcion> buscarPorRangoPrecio(BigDecimal minPrecio, BigDecimal maxPrecio, Pageable pageable) {
        return tipoSuscripcionRepository.findByPrecioBetween(minPrecio, maxPrecio, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoSuscripcion> buscarPorLimite(Integer limite, Pageable pageable) {
        return tipoSuscripcionRepository.findByLimiteGreaterThanEqual(limite, pageable);
    }
}
