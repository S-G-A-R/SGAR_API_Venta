package com.sgar.SGARventaAPI.controller;

import com.sgar.SGARventaAPI.Servicios.Interfaces.ICategoriaProductoService;
import com.sgar.SGARventaAPI.dto.CatefgoriaProducto.CategoriaProductoRequets;
import com.sgar.SGARventaAPI.dto.CatefgoriaProducto.CategoriaProductoResponse;
import com.sgar.SGARventaAPI.mapper.CategoriaProductoMapper;
import com.sgar.SGARventaAPI.modelos.CategoriaProducto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias-productos")
@CrossOrigin(origins = "*")
@Tag(name = "Categorías de Productos", description = "API para gestión de categorías de productos")
public class CategoriaProductoController {

    @Autowired
    private ICategoriaProductoService categoriaProductoService;

    @Autowired
    private CategoriaProductoMapper categoriaProductoMapper;

    @PostMapping
    @Operation(summary = "Crear una nueva categoría de producto", description = "Crea una nueva categoría de producto en el sistema")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador')")
    public ResponseEntity<?> crearCategoriaProducto(@RequestBody CategoriaProductoRequets categoriaDTO) {
        try {
            CategoriaProducto categoria = categoriaProductoMapper.toEntity(categoriaDTO);
            CategoriaProducto nuevaCategoria = categoriaProductoService.crearCategoriaProducto(categoria);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Categoría de producto creada exitosamente");
            response.put("id", nuevaCategoria.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear la categoría de producto");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todas las categorías con paginación", 
               description = "Retorna una lista paginada de todas las categorías de productos")
    @PreAuthorize("hasAnyAuthority('ROLE_Asociado','ROLE_Ciudadano', 'ROLE_Administrador')")
    public ResponseEntity<?> obtenerTodasCategorias(
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
            Page<CategoriaProducto> categoriasPage = categoriaProductoService.obtenerTodasCategorias(pageable);
            
            List<CategoriaProductoResponse> categoriasDTO = categoriasPage.getContent().stream()
                    .map(categoriaProductoMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("categorias", categoriasDTO);
            response.put("currentPage", categoriasPage.getNumber() + 1);
            response.put("totalItems", categoriasPage.getTotalElements());
            response.put("totalPages", categoriasPage.getTotalPages());
            response.put("pageSize", categoriasPage.getSize());
            response.put("hasNext", categoriasPage.hasNext());
            response.put("hasPrevious", categoriasPage.hasPrevious());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener las categorías");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Retorna una categoría de producto específica por su ID")
    @PreAuthorize("hasAnyAuthority('ROLE_Asociado','ROLE_Ciudadano', 'ROLE_Administrador')")
    public ResponseEntity<?> obtenerCategoriaPorId(@PathVariable("id") Integer id) {
        try {
            return categoriaProductoService.obtenerCategoriaPorId(id)
                    .map(categoriaProductoMapper::toDTO)
                    .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener la categoría");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza los datos de una categoría de producto existente")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador')")
    public ResponseEntity<?> actualizarCategoriaProducto(
            @PathVariable("id") Integer id, 
            @RequestBody CategoriaProductoRequets categoriaDTO) {
        try {
            CategoriaProducto categoria = categoriaProductoMapper.toEntity(categoriaDTO);
            CategoriaProducto categoriaActualizada = categoriaProductoService.actualizarCategoriaProducto(id, categoria);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Categoría de producto actualizada exitosamente");
            response.put("id", categoriaActualizada.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Categoría no encontrada");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar la categoría");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría de producto del sistema")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador')")
    public ResponseEntity<?> eliminarCategoriaProducto(@PathVariable("id") Integer id) {
        try {
            categoriaProductoService.eliminarCategoriaProducto(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Categoría de producto eliminada exitosamente");
            response.put("id", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Categoría no encontrada");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar la categoría");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar categorías por nombre", 
               description = "Busca categorías cuyo nombre contenga el texto especificado")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador','ROLE_Asociado', 'ROLE_Ciudadano')")
    public ResponseEntity<?> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre") 
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CategoriaProducto> categoriasPage = categoriaProductoService.buscarPorNombre(nombre, pageable);
            
            List<CategoriaProductoResponse> categoriasDTO = categoriasPage.getContent().stream()
                    .map(categoriaProductoMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("categorias", categoriasDTO);
            response.put("currentPage", categoriasPage.getNumber() + 1);
            response.put("totalItems", categoriasPage.getTotalElements());
            response.put("totalPages", categoriasPage.getTotalPages());
            response.put("pageSize", categoriasPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar categorías por nombre");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
