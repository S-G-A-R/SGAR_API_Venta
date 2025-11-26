package com.sgar.SGARventaAPI.mapper;

import com.sgar.SGARventaAPI.dto.Producto.ProductoRequets;
import com.sgar.SGARventaAPI.dto.Producto.ProductoResponse;
import com.sgar.SGARventaAPI.modelos.CategoriaProducto;
import com.sgar.SGARventaAPI.modelos.Empresa;
import com.sgar.SGARventaAPI.modelos.Producto;
import com.sgar.SGARventaAPI.repositorios.CategoriaProductoRepository;
import com.sgar.SGARventaAPI.repositorios.EmpresaRepository;
import com.sgar.SGARventaAPI.repositorios.ImagenProductosRepository;
import com.sgar.SGARventaAPI.modelos.ImagenProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    @Autowired
    private CategoriaProductoMapper categoriaProductoMapper;

    @Autowired
    private EmpresaMapper empresaMapper;

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private ImagenProductosRepository imagenProductosRepository;

    public ProductoResponse toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        ProductoResponse dto = new ProductoResponse();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setTipo(producto.getTipo());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoriaProducto(categoriaProductoMapper.toDTO(producto.getCategoriaProducto()));
        dto.setEmpresa(empresaMapper.toDTO(producto.getEmpresa()));
        if (producto.getFoto() != null) {
            dto.setFotoId(producto.getFoto().getId());
        }
        
        return dto;
    }

    public Producto toEntity(ProductoRequets dto) {
        if (dto == null) {
            return null;
        }
        
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setTipo(dto.getTipo());
        producto.setDescripcion(dto.getDescripcion());
        
        // Buscar y asignar categoría
        if (dto.getCategoriaProductoId() != null) {
            CategoriaProducto categoria = categoriaProductoRepository.findById(dto.getCategoriaProductoId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getCategoriaProductoId()));
            producto.setCategoriaProducto(categoria);
        }
        
        // Buscar y asignar empresa
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + dto.getEmpresaId()));
            producto.setEmpresa(empresa);
        }

        // Asignar foto si se provee fotoId
        if (dto.getFotoId() != null) {
            ImagenProductos imagen = imagenProductosRepository.findById(dto.getFotoId())
                    .orElseThrow(() -> new RuntimeException("Imagen no encontrada con id: " + dto.getFotoId()));
            producto.setFoto(imagen);
        }
        
        return producto;
    }

    public void updateEntityFromDTO(Producto producto, ProductoRequets dto) {
        if (dto == null || producto == null) {
            return;
        }
        
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setTipo(dto.getTipo());
        producto.setDescripcion(dto.getDescripcion());
        
        // Actualizar categoría
        if (dto.getCategoriaProductoId() != null) {
            CategoriaProducto categoria = categoriaProductoRepository.findById(dto.getCategoriaProductoId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getCategoriaProductoId()));
            producto.setCategoriaProducto(categoria);
        }
        
        // Actualizar empresa
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + dto.getEmpresaId()));
            producto.setEmpresa(empresa);
        }

        // Actualizar foto si se proporciona fotoId (solo si no es null)
        if (dto.getFotoId() != null) {
            ImagenProductos imagen = imagenProductosRepository.findById(dto.getFotoId())
                    .orElseThrow(() -> new RuntimeException("Imagen no encontrada con id: " + dto.getFotoId()));
            producto.setFoto(imagen);
        }
    }
}
