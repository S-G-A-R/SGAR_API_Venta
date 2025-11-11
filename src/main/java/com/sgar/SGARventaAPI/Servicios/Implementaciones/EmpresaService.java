package com.sgar.SGARventaAPI.Servicios.Implementaciones;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IEmpresaService;
import com.sgar.SGARventaAPI.modelos.Empresa;
import com.sgar.SGARventaAPI.repositorios.EmpresaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmpresaService implements IEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public Empresa crearEmpresa(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa actualizarEmpresa(Long id, Empresa empresa) {
        Optional<Empresa> empresaExistente = empresaRepository.findById(id);
        if (empresaExistente.isPresent()) {
            Empresa empresaActualizada = empresaExistente.get();
            empresaActualizada.setAsociadoId(empresa.getAsociadoId());
            empresaActualizada.setNombreEmpresa(empresa.getNombreEmpresa());
            return empresaRepository.save(empresaActualizada);
        }
        throw new RuntimeException("Empresa no encontrada con ID: " + id);
    }

    @Override
    public void eliminarEmpresa(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new RuntimeException("Empresa no encontrada con ID: " + id);
        }
        empresaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empresa> obtenerEmpresaPorId(Long id) {
        return empresaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empresa> obtenerTodasLasEmpresas(Pageable pageable) {
        return empresaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empresa> buscarEmpresasPorNombre(String nombreEmpresa, Pageable pageable) {
        return empresaRepository.findByNombreEmpresaContainingIgnoreCase(nombreEmpresa, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empresa> buscarEmpresasPorAsociadoId(Long asociadoId, Pageable pageable) {
        return empresaRepository.findByAsociadoId(asociadoId, pageable);
    }
}
