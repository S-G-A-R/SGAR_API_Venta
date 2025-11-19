package com.sgar.SGARventaAPI.Servicios.Implementaciones;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IProductoService;
import com.sgar.SGARventaAPI.modelos.Producto;
import com.sgar.SGARventaAPI.repositorios.EmpresaRepository;
import com.sgar.SGARventaAPI.repositorios.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    @Transactional
    public Producto crearProducto(Producto producto) {
        // Validar que la empresa no haya alcanzado su límite de productos según su plan de suscripción
        if (producto.getEmpresa() == null || producto.getEmpresa().getId() == null) {
            throw new RuntimeException("Debe especificar una empresa para el producto.");
        }
        
        Long empresaId = producto.getEmpresa().getId();
        
        // Verificar que la empresa existe
        if (!empresaRepository.existsById(empresaId)) {
            throw new RuntimeException("Empresa no encontrada con id: " + empresaId);
        }
        
        // Obtener el límite de productos directamente sin cargar colecciones
        Integer limite = empresaRepository.findLimiteProductosByEmpresaId(empresaId)
            .orElseThrow(() -> new RuntimeException("La empresa no tiene un plan de suscripción asignado. No se puede crear el producto."));
        
        // Contar productos actuales de la empresa
        long productosActuales = productoRepository.countByEmpresaId(empresaId);
        
        if (productosActuales >= limite) {
            throw new RuntimeException("La empresa ha alcanzado su límite de " + limite + 
                " productos según su plan de suscripción. No se pueden agregar más productos.");
        }
        
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> obtenerTodosProductos(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    @Transactional
    public Producto actualizarProducto(Integer id, Producto producto) {
        return productoRepository.findById(id)
                .map(productoExistente -> {
                    // Si se está cambiando de empresa, validar el límite de la nueva empresa
                    if (producto.getEmpresa() != null && 
                        !producto.getEmpresa().getId().equals(productoExistente.getEmpresa().getId())) {
                        
                        Long nuevaEmpresaId = producto.getEmpresa().getId();
                        
                        // Verificar que la nueva empresa existe
                        if (!empresaRepository.existsById(nuevaEmpresaId)) {
                            throw new RuntimeException("Empresa no encontrada con id: " + nuevaEmpresaId);
                        }
                        
                        // Obtener el límite de productos directamente
                        Integer limite = empresaRepository.findLimiteProductosByEmpresaId(nuevaEmpresaId)
                            .orElseThrow(() -> new RuntimeException("La empresa no tiene un plan de suscripción asignado."));
                        
                        // Contar productos actuales de la nueva empresa
                        long productosActuales = productoRepository.countByEmpresaId(nuevaEmpresaId);
                        
                        if (productosActuales >= limite) {
                            throw new RuntimeException("La empresa ha alcanzado su límite de " + limite + 
                                " productos según su plan de suscripción. No se puede mover el producto a esta empresa.");
                        }
                    }
                    
                    productoExistente.setNombre(producto.getNombre());
                    productoExistente.setPrecio(producto.getPrecio());
                    productoExistente.setTipo(producto.getTipo());
                    productoExistente.setDescripcion(producto.getDescripcion());
                    productoExistente.setCategoriaProducto(producto.getCategoriaProducto());
                    productoExistente.setEmpresa(producto.getEmpresa());
                    
                    return productoRepository.save(productoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public void eliminarProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> buscarProductos(String nombre, String tipo, Integer categoriaId, Long empresaId,
                                          BigDecimal minPrecio, BigDecimal maxPrecio, Pageable pageable) {
        Specification<Producto> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Filtro por nombre
            if (nombre != null && !nombre.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nombre")), 
                    "%" + nombre.toLowerCase() + "%"
                ));
            }
            
            // Filtro por tipo
            if (tipo != null && !tipo.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("tipo")), 
                    "%" + tipo.toLowerCase() + "%"
                ));
            }
            
            // Filtro por categoría
            if (categoriaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoriaProducto").get("id"), categoriaId));
            }
            
            // Filtro por empresa
            if (empresaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("empresa").get("id"), empresaId));
            }
            
            // Filtro por rango de precio
            if (minPrecio != null && maxPrecio != null) {
                predicates.add(criteriaBuilder.between(root.get("precio"), minPrecio, maxPrecio));
            } else if (minPrecio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("precio"), minPrecio));
            } else if (maxPrecio != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("precio"), maxPrecio));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return productoRepository.findAll(spec, pageable);
    }
}
