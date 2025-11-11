package com.sgar.SGARventaAPI.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // Permitir CORS desde cualquier origen
@Tag(name = "Status", description = "Endpoints para verificar el estado de la API")
public class StatusController {

    @GetMapping("/")
    @Operation(summary = "Redirigir a Swagger", description = "Redirecciona a la documentación de Swagger")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping("/api/status")
    @Operation(summary = "Verificar estado de la API", description = "Endpoint público para verificar que la API está funcionando correctamente")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "OK");
        status.put("message", "SGAR Venta API está funcionando correctamente");
        status.put("timestamp", LocalDateTime.now());
        status.put("version", "1.0.0");
        
        return ResponseEntity.ok(status);
    }

}
