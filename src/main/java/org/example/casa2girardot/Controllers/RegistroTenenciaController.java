package org.example.casa2girardot.Controllers;

import jakarta.validation.Valid;
import org.example.casa2girardot.Dtos.RegistroTenenciaCreateDTO;
import org.example.casa2girardot.Dtos.RegistroTenenciaDTO;
import org.example.casa2girardot.Dtos.RegistroTenenciaUpdateDTO;
import org.example.casa2girardot.Services.RegistroTenenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/registro-tenencias")
@CrossOrigin(origins = "*")
public class RegistroTenenciaController {

    @Autowired
    private RegistroTenenciaService registroTenenciaService;

    // Obtener todos los registros
    @GetMapping
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerTodos() {
        return ResponseEntity.ok(registroTenenciaService.obtenerTodos());
    }

    // Obtener registros activos
    @GetMapping("/activos")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerActivos() {
        return ResponseEntity.ok(registroTenenciaService.obtenerActivos());
    }

    // Obtener registros finalizados
    @GetMapping("/finalizados")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerFinalizados() {
        return ResponseEntity.ok(registroTenenciaService.obtenerFinalizados());
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<RegistroTenenciaDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(registroTenenciaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener historial de un inmueble
    @GetMapping("/inmueble/{idInmueble}")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerHistorialInmueble(@PathVariable Integer idInmueble) {
        try {
            return ResponseEntity.ok(registroTenenciaService.obtenerHistorialInmueble(idInmueble));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener historial de un arrendatario
    @GetMapping("/arrendatario/{idArrendatario}")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerHistorialArrendatario(@PathVariable Integer idArrendatario) {
        try {
            return ResponseEntity.ok(registroTenenciaService.obtenerHistorialArrendatario(idArrendatario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener tenencia activa de un inmueble
    @GetMapping("/inmueble/{idInmueble}/activa")
    public ResponseEntity<?> obtenerTenenciaActivaInmueble(@PathVariable Integer idInmueble) {
        try {
            return ResponseEntity.ok(registroTenenciaService.obtenerTenenciaActivaInmueble(idInmueble));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener tenencias activas de un arrendatario
    @GetMapping("/arrendatario/{idArrendatario}/activas")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerTenenciasActivasArrendatario(@PathVariable Integer idArrendatario) {
        try {
            return ResponseEntity.ok(registroTenenciaService.obtenerTenenciasActivasArrendatario(idArrendatario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener por rango de fechas
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(registroTenenciaService.obtenerPorRangoFechas(inicio, fin));
    }

    // Crear registro manualmente
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody RegistroTenenciaCreateDTO dto) {
        try {
            RegistroTenenciaDTO creado = registroTenenciaService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar registro
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody RegistroTenenciaUpdateDTO dto) {
        try {
            RegistroTenenciaDTO actualizado = registroTenenciaService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Finalizar tenencia
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarTenencia(
            @PathVariable Integer id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal) {
        try {
            RegistroTenenciaDTO actualizado = registroTenenciaService.finalizarTenencia(id, fechaFinal);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar registro
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            registroTenenciaService.eliminar(id);
            return ResponseEntity.ok("Registro de tenencia eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}