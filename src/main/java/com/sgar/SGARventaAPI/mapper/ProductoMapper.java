package com.sgar.SGARventaAPI.mapper;

import com.sgar.SGARventaAPI.dto.Producto.ProductoRequets;
import com.sgar.SGARventaAPI.dto.Producto.ProductoResponse;
import com.sgar.SGARventaAPI.modelos.CategoriaProducto;
import com.sgar.SGARventaAPI.modelos.PlanDeSuscripcion;
import com.sgar.SGARventaAPI.modelos.Producto;
import com.sgar.SGARventaAPI.repositorios.CategoriaProductoRepository;
import com.sgar.SGARventaAPI.repositorios.PlanSuscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    @Autowired
    private CategoriaProductoMapper categoriaProductoMapper;

    @Autowired
    private PlanDeSuscripcionMapper planDeSuscripcionMapper;

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Autowired
    private PlanSuscripcionRepository planSuscripcionRepository;

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
        dto.setPlanDeSuscripcion(planDeSuscripcionMapper.toDTO(producto.getPlanDeSuscripcion()));
        
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
        
        // Buscar y asignar plan
        if (dto.getPlanDeSuscripcionId() != null) {
            PlanDeSuscripcion plan = planSuscripcionRepository.findById(dto.getPlanDeSuscripcionId())
                    .orElseThrow(() -> new RuntimeException("Plan de suscripción no encontrado con id: " + dto.getPlanDeSuscripcionId()));
            producto.setPlanDeSuscripcion(plan);
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
        
        // Actualizar plan
        if (dto.getPlanDeSuscripcionId() != null) {
            PlanDeSuscripcion plan = planSuscripcionRepository.findById(dto.getPlanDeSuscripcionId())
                    .orElseThrow(() -> new RuntimeException("Plan de suscripción no encontrado con id: " + dto.getPlanDeSuscripcionId()));
            producto.setPlanDeSuscripcion(plan);
        }
    }
}
