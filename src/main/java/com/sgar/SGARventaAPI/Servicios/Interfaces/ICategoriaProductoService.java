package com.sgar.SGARventaAPI.Servicios.Interfaces;

import com.sgar.SGARventaAPI.modelos.CategoriaProducto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICategoriaProductoService {
    
    CategoriaProducto crearCategoriaProducto(CategoriaProducto categoriaProducto);
    
    Page<CategoriaProducto> obtenerTodasCategorias(Pageable pageable);
    
    Optional<CategoriaProducto> obtenerCategoriaPorId(Integer id);
    
    CategoriaProducto actualizarCategoriaProducto(Integer id, CategoriaProducto categoriaProducto);
    
    void eliminarCategoriaProducto(Integer id);
    
    Page<CategoriaProducto> buscarPorNombre(String nombre, Pageable pageable);
}
