package com.sgar.SGARventaAPI.Servicios.Interfaces;

import com.sgar.SGARventaAPI.modelos.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IEmpresaService {
    
    // Crear una nueva empresa
    Empresa crearEmpresa(Empresa empresa);
    
    // Actualizar una empresa existente
    Empresa actualizarEmpresa(Long id, Empresa empresa);
    
    // Eliminar una empresa por ID
    void eliminarEmpresa(Long id);
    
    // Obtener una empresa por ID
    Optional<Empresa> obtenerEmpresaPorId(Long id);
    
    // Obtener todas las empresas con paginación
    Page<Empresa> obtenerTodasLasEmpresas(Pageable pageable);
    
    // Buscar empresas por nombre con paginación
    Page<Empresa> buscarEmpresasPorNombre(String nombreEmpresa, Pageable pageable);
    
    // Buscar empresas por asociado ID con paginación
    Page<Empresa> buscarEmpresasPorAsociadoId(Long asociadoId, Pageable pageable);
}
