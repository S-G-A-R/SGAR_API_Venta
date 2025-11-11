package com.sgar.SGARventaAPI.controller;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IEmpresaService;
import com.sgar.SGARventaAPI.modelos.Empresa;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
@Tag(name = "Empresas", description = "API para gestión de empresas")
public class EmpresaController {

    @Autowired
    private IEmpresaService empresaService;

    @PostMapping
    @Operation(summary = "Crear una nueva empresa", description = "Crea una nueva empresa en el sistema")
    public ResponseEntity<?> crearEmpresa(@RequestBody Empresa empresa) {
        try {
            Empresa nuevaEmpresa = empresaService.crearEmpresa(empresa);
            return new ResponseEntity<>(nuevaEmpresa, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear la empresa");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todas las empresas con paginación", 
               description = "Retorna una lista paginada de todas las empresas")
    public ResponseEntity<Map<String, Object>> obtenerTodasLasEmpresas(
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
            Page<Empresa> empresasPage = empresaService.obtenerTodasLasEmpresas(pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasPage.getContent());
            response.put("currentPage", empresasPage.getNumber());
            response.put("totalItems", empresasPage.getTotalElements());
            response.put("totalPages", empresasPage.getTotalPages());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empresa por ID", description = "Retorna una empresa específica por su ID")
    public ResponseEntity<Empresa> obtenerEmpresaPorId(@PathVariable("id") Long id) {
        return empresaService.obtenerEmpresaPorId(id)
                .map(empresa -> new ResponseEntity<>(empresa, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empresa", description = "Actualiza los datos de una empresa existente")
    public ResponseEntity<Empresa> actualizarEmpresa(
            @PathVariable("id") Long id, 
            @RequestBody Empresa empresa) {
        try {
            Empresa empresaActualizada = empresaService.actualizarEmpresa(id, empresa);
            return new ResponseEntity<>(empresaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empresa", description = "Elimina una empresa del sistema")
    public ResponseEntity<Map<String, String>> eliminarEmpresa(@PathVariable("id") Long id) {
        try {
            empresaService.eliminarEmpresa(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Empresa eliminada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar empresas por nombre", 
               description = "Busca empresas cuyo nombre contenga el texto especificado")
    public ResponseEntity<Map<String, Object>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre de la empresa") 
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Empresa> empresasPage = empresaService.buscarEmpresasPorNombre(nombre, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasPage.getContent());
            response.put("currentPage", empresasPage.getNumber());
            response.put("totalItems", empresasPage.getTotalElements());
            response.put("totalPages", empresasPage.getTotalPages());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/asociado/{asociadoId}")
    @Operation(summary = "Buscar empresas por ID de asociado", 
               description = "Retorna todas las empresas asociadas a un ID específico")
    public ResponseEntity<Map<String, Object>> buscarPorAsociadoId(
            @PathVariable Long asociadoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Empresa> empresasPage = empresaService.buscarEmpresasPorAsociadoId(asociadoId, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasPage.getContent());
            response.put("currentPage", empresasPage.getNumber());
            response.put("totalItems", empresasPage.getTotalElements());
            response.put("totalPages", empresasPage.getTotalPages());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
