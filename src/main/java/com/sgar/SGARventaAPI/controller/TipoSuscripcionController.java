package com.sgar.SGARventaAPI.controller;

import com.sgar.SGARventaAPI.Servicios.Interfaces.ITipoSuscripcionService;
import com.sgar.SGARventaAPI.dto.TipoSuscripcion.TipoSuscripcionRequets;
import com.sgar.SGARventaAPI.dto.TipoSuscripcion.TipoSuscripcionResponse;
import com.sgar.SGARventaAPI.mapper.TipoSuscripcionMapper;
import com.sgar.SGARventaAPI.modelos.TipoSuscripcion;
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
@RequestMapping("/api/tipos-suscripcion")
@CrossOrigin(origins = "*")
@Tag(name = "Tipos de Suscripción", description = "API para gestión de tipos de suscripción")
public class TipoSuscripcionController {

    @Autowired
    private ITipoSuscripcionService tipoSuscripcionService;

    @Autowired
    private TipoSuscripcionMapper tipoSuscripcionMapper;

    @PostMapping
    @Operation(summary = "Crear un nuevo tipo de suscripción", description = "Crea un nuevo tipo de suscripción en el sistema")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<?> crearTipoSuscripcion(@RequestBody TipoSuscripcionRequets tipoDTO) {
        try {
            TipoSuscripcion tipo = tipoSuscripcionMapper.toEntity(tipoDTO);
            TipoSuscripcion nuevoTipo = tipoSuscripcionService.crearTipoSuscripcion(tipo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Tipo de suscripción creado exitosamente");
            response.put("id", nuevoTipo.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el tipo de suscripción");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los tipos de suscripción con paginación", 
               description = "Retorna una lista paginada de todos los tipos de suscripción")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<?> obtenerTodosTiposSuscripcion(
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
            Page<TipoSuscripcion> tiposPage = tipoSuscripcionService.obtenerTodosTiposSuscripcion(pageable);
            
            List<TipoSuscripcionResponse> tiposDTO = tiposPage.getContent().stream()
                    .map(tipoSuscripcionMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("tiposSuscripcion", tiposDTO);
            response.put("currentPage", tiposPage.getNumber() + 1);
            response.put("totalItems", tiposPage.getTotalElements());
            response.put("totalPages", tiposPage.getTotalPages());
            response.put("pageSize", tiposPage.getSize());
            response.put("hasNext", tiposPage.hasNext());
            response.put("hasPrevious", tiposPage.hasPrevious());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener los tipos de suscripción");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo de suscripción por ID", description = "Retorna un tipo de suscripción específico por su ID")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<?> obtenerTipoSuscripcionPorId(@PathVariable("id") Integer id) {
        try {
            return tipoSuscripcionService.obtenerTipoSuscripcionPorId(id)
                    .map(tipoSuscripcionMapper::toDTO)
                    .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener el tipo de suscripción");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de suscripción", description = "Actualiza los datos de un tipo de suscripción existente")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<?> actualizarTipoSuscripcion(
            @PathVariable("id") Integer id, 
            @RequestBody TipoSuscripcionRequets tipoDTO) {
        try {
            TipoSuscripcion tipo = tipoSuscripcionMapper.toEntity(tipoDTO);
            TipoSuscripcion tipoActualizado = tipoSuscripcionService.actualizarTipoSuscripcion(id, tipo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Tipo de suscripción actualizado exitosamente");
            response.put("id", tipoActualizado.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Tipo de suscripción no encontrado");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar el tipo de suscripción");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de suscripción", description = "Elimina un tipo de suscripción del sistema")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<?> eliminarTipoSuscripcion(@PathVariable("id") Integer id) {
        try {
            tipoSuscripcionService.eliminarTipoSuscripcion(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Tipo de suscripción eliminado exitosamente");
            response.put("id", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Tipo de suscripción no encontrado");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar el tipo de suscripción");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar tipos de suscripción por nombre", 
               description = "Busca tipos de suscripción cuyo nombre contenga el texto especificado")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<?> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre") 
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TipoSuscripcion> tiposPage = tipoSuscripcionService.buscarPorNombre(nombre, pageable);
            
            List<TipoSuscripcionResponse> tiposDTO = tiposPage.getContent().stream()
                    .map(tipoSuscripcionMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("tiposSuscripcion", tiposDTO);
            response.put("currentPage", tiposPage.getNumber() + 1);
            response.put("totalItems", tiposPage.getTotalElements());
            response.put("totalPages", tiposPage.getTotalPages());
            response.put("pageSize", tiposPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar tipos de suscripción por nombre");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/precio")
    @Operation(summary = "Buscar tipos de suscripción por rango de precio", 
               description = "Retorna tipos de suscripción dentro de un rango de precio")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<?> buscarPorRangoPrecio(
            @Parameter(description = "Precio mínimo") 
            @RequestParam BigDecimal minPrecio,
            @Parameter(description = "Precio máximo") 
            @RequestParam BigDecimal maxPrecio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TipoSuscripcion> tiposPage = tipoSuscripcionService.buscarPorRangoPrecio(minPrecio, maxPrecio, pageable);
            
            List<TipoSuscripcionResponse> tiposDTO = tiposPage.getContent().stream()
                    .map(tipoSuscripcionMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("tiposSuscripcion", tiposDTO);
            response.put("currentPage", tiposPage.getNumber() + 1);
            response.put("totalItems", tiposPage.getTotalElements());
            response.put("totalPages", tiposPage.getTotalPages());
            response.put("pageSize", tiposPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar tipos de suscripción por precio");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/limite/{limite}")
    @Operation(summary = "Buscar tipos de suscripción por límite mínimo", 
               description = "Retorna tipos de suscripción con límite mayor o igual al especificado")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<?> buscarPorLimite(
            @PathVariable Integer limite,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TipoSuscripcion> tiposPage = tipoSuscripcionService.buscarPorLimite(limite, pageable);
            
            List<TipoSuscripcionResponse> tiposDTO = tiposPage.getContent().stream()
                    .map(tipoSuscripcionMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("tiposSuscripcion", tiposDTO);
            response.put("currentPage", tiposPage.getNumber() + 1);
            response.put("totalItems", tiposPage.getTotalElements());
            response.put("totalPages", tiposPage.getTotalPages());
            response.put("pageSize", tiposPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar tipos de suscripción por límite");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
