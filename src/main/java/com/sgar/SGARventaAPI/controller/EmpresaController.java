package com.sgar.SGARventaAPI.controller;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IEmpresaService;
import com.sgar.SGARventaAPI.dto.Empresa.EmpresaRequets;
import com.sgar.SGARventaAPI.dto.Empresa.EmpresaResponse;
import com.sgar.SGARventaAPI.mapper.EmpresaMapper;
import com.sgar.SGARventaAPI.modelos.Empresa;

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
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
@Tag(name = "Empresas", description = "API para gestión de empresas")
public class EmpresaController {

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private EmpresaMapper empresaMapper;

    @PostMapping
    @Operation(summary = "Crear una nueva empresa", description = "Crea una nueva empresa en el sistema")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> crearEmpresa(@RequestBody EmpresaRequets empresaDTO) {
        try {
            Empresa empresa = empresaMapper.toEntity(empresaDTO);
            Empresa nuevaEmpresa = empresaService.crearEmpresa(empresa);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Empresa creada exitosamente");
            response.put("id", nuevaEmpresa.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
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
    @PreAuthorize("hasAuthority('ROLE_Asociado, ROLE_Administrador')")
    public ResponseEntity<?> obtenerTodasLasEmpresas(
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
            
            // Convertir entidades a DTOs
            List<EmpresaResponse> empresasDTO = empresasPage.getContent().stream()
                    .map(empresaMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasDTO);
            response.put("currentPage", empresasPage.getNumber() + 1); // Convertir a base 1
            response.put("totalItems", empresasPage.getTotalElements());
            response.put("totalPages", empresasPage.getTotalPages());
            response.put("pageSize", empresasPage.getSize());
            response.put("hasNext", empresasPage.hasNext());
            response.put("hasPrevious", empresasPage.hasPrevious());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener las empresas");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empresa por ID", description = "Retorna una empresa específica por su ID")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<EmpresaResponse> obtenerEmpresaPorId(@PathVariable("id") Long id) {
        return empresaService.obtenerEmpresaPorId(id)
                .map(empresaMapper::toDTO)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empresa", description = "Actualiza los datos de una empresa existente")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> actualizarEmpresa(
            @PathVariable("id") Long id, 
            @RequestBody EmpresaRequets empresaDTO) {
        try {
            Empresa empresaActualizada = empresaService.actualizarEmpresa(id, empresaMapper.toEntity(empresaDTO));
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Empresa actualizada exitosamente");
            response.put("id", empresaActualizada.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Empresa no encontrada");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar la empresa");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empresa", description = "Elimina una empresa del sistema")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> eliminarEmpresa(@PathVariable("id") Long id) {
        try {
            empresaService.eliminarEmpresa(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Empresa eliminada exitosamente");
            response.put("id", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Empresa no encontrada");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar la empresa");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar empresas por nombre", 
               description = "Busca empresas cuyo nombre contenga el texto especificado")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre de la empresa") 
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Empresa> empresasPage = empresaService.buscarEmpresasPorNombre(nombre, pageable);
            
            List<EmpresaResponse> empresasDTO = empresasPage.getContent().stream()
                    .map(empresaMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasDTO);
            response.put("currentPage", empresasPage.getNumber() + 1); // Convertir a base 1 para el usuario
            response.put("totalItems", empresasPage.getTotalElements());
            response.put("totalPages", empresasPage.getTotalPages());
            response.put("pageSize", empresasPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar empresas por nombre");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/asociado/{asociadoId}")
    @Operation(summary = "Buscar empresas por ID de asociado", 
               description = "Retorna todas las empresas asociadas a un ID específico")
    @PreAuthorize("hasAuthority('ROLE_Asociado')")
    public ResponseEntity<?> buscarPorAsociadoId(
            @PathVariable Long asociadoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Empresa> empresasPage = empresaService.buscarEmpresasPorAsociadoId(asociadoId, pageable);
            
            List<EmpresaResponse> empresasDTO = empresasPage.getContent().stream()
                    .map(empresaMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("empresas", empresasDTO);
            response.put("currentPage", empresasPage.getNumber() + 1); // Convertir a base 1 para el usuario
            response.put("totalItems", empresasPage.getTotalElements());
            response.put("totalPages", empresasPage.getTotalPages());
            response.put("pageSize", empresasPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar empresas por asociado");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
