package com.sgar.SGARventaAPI.Servicios.Implementaciones;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IProductoService;
import com.sgar.SGARventaAPI.modelos.Producto;
import com.sgar.SGARventaAPI.repositorios.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional
    public Producto crearProducto(Producto producto) {
        // Validar que el plan de suscripción no haya alcanzado su límite de productos
        if (producto.getPlanDeSuscripcion() != null) {
            Integer planId = producto.getPlanDeSuscripcion().getId();
            Integer limite = producto.getPlanDeSuscripcion().getTipoSuscripcion().getLimite();
            
            // Contar productos actuales en el plan
            long productosActuales = productoRepository.countByPlanDeSuscripcionId(planId);
            
            if (productosActuales >= limite) {
                throw new RuntimeException("El plan de suscripción ha alcanzado su límite de " + limite + " productos. No se pueden agregar más productos.");
            }
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
                    // Si se está cambiando de plan, validar el límite del nuevo plan
                    if (producto.getPlanDeSuscripcion() != null && 
                        !producto.getPlanDeSuscripcion().getId().equals(productoExistente.getPlanDeSuscripcion().getId())) {
                        
                        Integer nuevoPlanId = producto.getPlanDeSuscripcion().getId();
                        Integer limite = producto.getPlanDeSuscripcion().getTipoSuscripcion().getLimite();
                        
                        // Contar productos actuales en el nuevo plan
                        long productosActuales = productoRepository.countByPlanDeSuscripcionId(nuevoPlanId);
                        
                        if (productosActuales >= limite) {
                            throw new RuntimeException("El plan de suscripción ha alcanzado su límite de " + limite + " productos. No se puede mover el producto a este plan.");
                        }
                    }
                    
                    productoExistente.setNombre(producto.getNombre());
                    productoExistente.setPrecio(producto.getPrecio());
                    productoExistente.setTipo(producto.getTipo());
                    productoExistente.setDescripcion(producto.getDescripcion());
                    productoExistente.setCategoriaProducto(producto.getCategoriaProducto());
                    productoExistente.setPlanDeSuscripcion(producto.getPlanDeSuscripcion());
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
    public Page<Producto> buscarPorNombre(String nombre, Pageable pageable) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> buscarPorCategoria(Integer categoriaId, Pageable pageable) {
        return productoRepository.findByCategoriaProductoId(categoriaId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> buscarPorPlan(Integer planId, Pageable pageable) {
        return productoRepository.findByPlanDeSuscripcionId(planId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> buscarPorTipo(String tipo, Pageable pageable) {
        return productoRepository.findByTipoContainingIgnoreCase(tipo, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> buscarPorRangoPrecio(BigDecimal minPrecio, BigDecimal maxPrecio, Pageable pageable) {
        return productoRepository.findByPrecioBetween(minPrecio, maxPrecio, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> buscarPorCategoriaYPlan(Integer categoriaId, Integer planId, Pageable pageable) {
        return productoRepository.findByCategoriaProductoIdAndPlanDeSuscripcionId(categoriaId, planId, pageable);
    }
}
