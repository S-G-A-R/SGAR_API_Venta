package com.sgar.SGARventaAPI.controller;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IProductoService;
import com.sgar.SGARventaAPI.dto.Producto.ProductoRequets;
import com.sgar.SGARventaAPI.dto.Producto.ProductoResponse;
import com.sgar.SGARventaAPI.mapper.ProductoMapper;
import com.sgar.SGARventaAPI.modelos.Producto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private ProductoMapper productoMapper;

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> crearProducto(@RequestBody ProductoRequets productoDTO) {
        try {
            Producto producto = productoMapper.toEntity(productoDTO);
            Producto nuevoProducto = productoService.crearProducto(producto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto creado exitosamente");
            response.put("id", nuevoProducto.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el producto");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/verificar-limite/{empresaId}")
    @Operation(summary = "Verificar límite de productos por empresa", description = "Verifica si la empresa puede agregar más productos según el plan del asociado")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> verificarLimite(@PathVariable("empresaId") Long empresaId) {
        try {
            boolean puede = productoService.puedeAgregarProducto(empresaId);
            Map<String, Object> resp = new HashMap<>();
            resp.put("empresaId", empresaId);
            resp.put("puedeAgregar", puede);
            resp.put("message", puede ? "Puede agregar más productos" : "Ha alcanzado el límite de productos");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al verificar límite de productos");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos con paginación", 
               description = "Retorna una lista paginada de todos los productos")
    @PreAuthorize("hasAnyAuthority('ROLE_Asociado','ROLE_Ciudadano', 'ROLE_Administrador')")
    public ResponseEntity<?> obtenerTodosProductos(
            @Parameter(description = "Número de página (inicia en 0)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") 
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo por el cual ordenar") 
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (asc/desc)") 
            @RequestParam(defaultValue = "asc") String direction) {
        
        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
            Page<Producto> productosPage = productoService.obtenerTodosProductos(pageable);
            
            List<ProductoResponse> productosDTO = productosPage.getContent().stream()
                    .map(productoMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("productos", productosDTO);
            response.put("currentPage", productosPage.getNumber() + 1);
            response.put("totalItems", productosPage.getTotalElements());
            response.put("totalPages", productosPage.getTotalPages());
            response.put("pageSize", productosPage.getSize());
            response.put("hasNext", productosPage.hasNext());
            response.put("hasPrevious", productosPage.hasPrevious());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener los productos");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico por su ID")
    @PreAuthorize("hasAnyAuthority('ROLE_Asociado','ROLE_Ciudadano', 'ROLE_Administrador')")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable("id") Integer id) {
        try {
            return productoService.obtenerProductoPorId(id)
                    .map(productoMapper::toDTO)
                    .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener el producto");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable("id") Integer id, 
            @RequestBody ProductoRequets productoDTO) {
        try {
            Producto producto = productoMapper.toEntity(productoDTO);
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto actualizado exitosamente");
            response.put("id", productoActualizado.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Producto no encontrado");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar el producto");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> eliminarProducto(@PathVariable("id") Integer id) {
        try {
            productoService.eliminarProducto(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto eliminado exitosamente");
            response.put("id", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Producto no encontrado");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar el producto");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos con filtros múltiples", 
               description = "Busca productos aplicando filtros opcionales: nombre, tipo, categoría, empresa y rango de precio")
             @PreAuthorize("hasAnyAuthority('ROLE_Asociado', 'ROLE_Administrador', 'ROLE_Ciudadano')")
    public ResponseEntity<?> buscarProductos(
            @Parameter(description = "Texto a buscar en el nombre del producto") 
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Texto a buscar en el tipo de producto") 
            @RequestParam(required = false) String tipo,
            @Parameter(description = "ID de la categoría") 
            @RequestParam(required = false) Integer categoriaId,
            @Parameter(description = "ID de la empresa") 
            @RequestParam(required = false) Long empresaId,
            @Parameter(description = "ID del asociado")
            @RequestParam(required = false) Long asociadoId,
            @Parameter(description = "Precio mínimo") 
            @RequestParam(required = false) BigDecimal minPrecio,
            @Parameter(description = "Precio máximo") 
            @RequestParam(required = false) BigDecimal maxPrecio,
            @Parameter(description = "Número de página (inicia en 0)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") 
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo por el cual ordenar") 
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (asc/desc)") 
            @RequestParam(defaultValue = "asc") String direction) {
        
        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
            
            Page<Producto> productosPage = productoService.buscarProductos(
                nombre, tipo, categoriaId, empresaId, asociadoId, minPrecio, maxPrecio, pageable
            );
            
            List<ProductoResponse> productosDTO = productosPage.getContent().stream()
                    .map(productoMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("productos", productosDTO);
            response.put("currentPage", productosPage.getNumber() + 1);
            response.put("totalItems", productosPage.getTotalElements());
            response.put("totalPages", productosPage.getTotalPages());
            response.put("pageSize", productosPage.getSize());
            response.put("hasNext", productosPage.hasNext());
            response.put("hasPrevious", productosPage.hasPrevious());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar productos");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
