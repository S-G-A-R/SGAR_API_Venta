package com.sgar.SGARventaAPI.controller;

import com.sgar.SGARventaAPI.Servicios.Interfaces.IPlanSuscripcionService;
import com.sgar.SGARventaAPI.dto.PlanSuscripcion.PlanDeSuscripcionRequets;
import com.sgar.SGARventaAPI.dto.PlanSuscripcion.PlanDeSuscripcionResponse;
import com.sgar.SGARventaAPI.mapper.PlanDeSuscripcionMapper;
import com.sgar.SGARventaAPI.modelos.PlanDeSuscripcion;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/planes-suscripcion")
@CrossOrigin(origins = "*")
@Tag(name = "Planes de Suscripción", description = "API para gestión de planes de suscripción")
public class PlanSuscripcionController {

    @Autowired
    private IPlanSuscripcionService planService;

    @Autowired
    private PlanDeSuscripcionMapper planMapper;

    @PostMapping
    @Operation(summary = "Crear un nuevo plan de suscripción", description = "Crea un nuevo plan de suscripción en el sistema")
    public ResponseEntity<?> crearPlan(@RequestBody PlanDeSuscripcionRequets planDTO) {
        try {
            PlanDeSuscripcion plan = planMapper.toEntity(planDTO);
            PlanDeSuscripcion nuevoPlan = planService.crearPlan(plan);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Plan de suscripción creado exitosamente");
            response.put("id", nuevoPlan.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el plan de suscripción");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los planes con paginación", 
               description = "Retorna una lista paginada de todos los planes de suscripción")
    public ResponseEntity<?> obtenerTodosLosPlanes(
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
            Page<PlanDeSuscripcion> planesPage = planService.obtenerTodosLosPlanes(pageable);
            
            List<PlanDeSuscripcionResponse> planesDTO = planesPage.getContent().stream()
                    .map(planMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("planes", planesDTO);
            response.put("currentPage", planesPage.getNumber() + 1);
            response.put("totalItems", planesPage.getTotalElements());
            response.put("totalPages", planesPage.getTotalPages());
            response.put("pageSize", planesPage.getSize());
            response.put("hasNext", planesPage.hasNext());
            response.put("hasPrevious", planesPage.hasPrevious());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener los planes");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener plan por ID", description = "Retorna un plan de suscripción específico por su ID")
    public ResponseEntity<?> obtenerPlanPorId(@PathVariable("id") Integer id) {
        try {
            return planService.obtenerPlanPorId(id)
                    .map(planMapper::toDTO)
                    .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener el plan");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar plan", description = "Actualiza los datos de un plan de suscripción existente")
    public ResponseEntity<?> actualizarPlan(
            @PathVariable("id") Integer id, 
            @RequestBody PlanDeSuscripcionRequets planDTO) {
        try {
            PlanDeSuscripcion plan = planMapper.toEntity(planDTO);
            PlanDeSuscripcion planActualizado = planService.actualizarPlan(id, plan);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Plan de suscripción actualizado exitosamente");
            response.put("id", planActualizado.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Plan no encontrado");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar el plan");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar plan", description = "Elimina un plan de suscripción del sistema")
    public ResponseEntity<?> eliminarPlan(@PathVariable("id") Integer id) {
        try {
            planService.eliminarPlan(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Plan de suscripción eliminado exitosamente");
            response.put("id", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Plan no encontrado");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar el plan");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/empresa/{empresaId}")
    @Operation(summary = "Buscar planes por empresa", 
               description = "Retorna todos los planes de suscripción de una empresa específica")
    public ResponseEntity<?> buscarPorEmpresa(
            @PathVariable Long empresaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PlanDeSuscripcion> planesPage = planService.buscarPlanesPorEmpresa(empresaId, pageable);
            
            List<PlanDeSuscripcionResponse> planesDTO = planesPage.getContent().stream()
                    .map(planMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("planes", planesDTO);
            response.put("currentPage", planesPage.getNumber() + 1);
            response.put("totalItems", planesPage.getTotalElements());
            response.put("totalPages", planesPage.getTotalPages());
            response.put("pageSize", planesPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar planes por empresa");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint comentado hasta agregar la relación planSuscripcionPadre en el modelo
    /*
    @GetMapping("/buscar/plan-padre/{planPadreId}")
    @Operation(summary = "Buscar planes hijos", 
               description = "Retorna todos los planes de suscripción hijos de un plan padre")
    public ResponseEntity<?> buscarPorPlanPadre(
            @PathVariable Integer planPadreId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PlanDeSuscripcion> planesPage = planService.buscarPlanesPorPlanPadre(planPadreId, pageable);
            
            List<PlanDeSuscripcionDTO> planesDTO = planesPage.getContent().stream()
                    .map(planMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("planes", planesDTO);
            response.put("currentPage", planesPage.getNumber() + 1);
            response.put("totalItems", planesPage.getTotalElements());
            response.put("totalPages", planesPage.getTotalPages());
            response.put("pageSize", planesPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar planes hijos");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    @GetMapping("/buscar/tipo-suscripcion/{tipoSuscripcionId}")
    @Operation(summary = "Buscar planes por tipo de suscripción", 
               description = "Retorna todos los planes de un tipo de suscripción específico")
    public ResponseEntity<?> buscarPorTipoSuscripcion(
            @PathVariable Integer tipoSuscripcionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PlanDeSuscripcion> planesPage = planService.buscarPlanesPorTipoSuscripcion(tipoSuscripcionId, pageable);
            
            List<PlanDeSuscripcionResponse> planesDTO = planesPage.getContent().stream()
                    .map(planMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("planes", planesDTO);
            response.put("currentPage", planesPage.getNumber() + 1);
            response.put("totalItems", planesPage.getTotalElements());
            response.put("totalPages", planesPage.getTotalPages());
            response.put("pageSize", planesPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar planes por tipo de suscripción");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/empresa-y-tipo")
    @Operation(summary = "Buscar planes por empresa y tipo de suscripción", 
               description = "Retorna todos los planes filtrados por empresa y tipo de suscripción")
    public ResponseEntity<?> buscarPorEmpresaYTipo(
            @RequestParam Long empresaId,
            @RequestParam Integer tipoSuscripcionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PlanDeSuscripcion> planesPage = planService.buscarPlanesPorEmpresaYTipo(empresaId, tipoSuscripcionId, pageable);
            
            List<PlanDeSuscripcionResponse> planesDTO = planesPage.getContent().stream()
                    .map(planMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("planes", planesDTO);
            response.put("currentPage", planesPage.getNumber() + 1);
            response.put("totalItems", planesPage.getTotalElements());
            response.put("totalPages", planesPage.getTotalPages());
            response.put("pageSize", planesPage.getSize());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar planes por empresa y tipo");
            error.put("message", e.getMessage());
            error.put("details", e.getClass().getSimpleName());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
