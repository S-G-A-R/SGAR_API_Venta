package com.sgar.SGARventaAPI.Servicios.Implementaciones;

import com.sgar.SGARventaAPI.Servicios.Interfaces.ICategoriaProductoService;
import com.sgar.SGARventaAPI.modelos.CategoriaProducto;
import com.sgar.SGARventaAPI.repositorios.CategoriaProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoriaProductoService implements ICategoriaProductoService {

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Override
    @Transactional
    public CategoriaProducto crearCategoriaProducto(CategoriaProducto categoriaProducto) {
        return categoriaProductoRepository.save(categoriaProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaProducto> obtenerTodasCategorias(Pageable pageable) {
        return categoriaProductoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaProducto> obtenerCategoriaPorId(Integer id) {
        return categoriaProductoRepository.findById(id);
    }

    @Override
    @Transactional
    public CategoriaProducto actualizarCategoriaProducto(Integer id, CategoriaProducto categoriaProducto) {
        return categoriaProductoRepository.findById(id)
                .map(categoriaExistente -> {
                    categoriaExistente.setNombreCat(categoriaProducto.getNombreCat());
                    return categoriaProductoRepository.save(categoriaExistente);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public void eliminarCategoriaProducto(Integer id) {
        if (!categoriaProductoRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con id: " + id);
        }
        categoriaProductoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaProducto> buscarPorNombre(String nombre, Pageable pageable) {
        return categoriaProductoRepository.findByNombreCatContainingIgnoreCase(nombre, pageable);
    }
}
