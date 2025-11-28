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

    @GetMapping
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerTodos() {
        return ResponseEntity.ok(registroTenenciaService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerActivos() {
        return ResponseEntity.ok(registroTenenciaService.obtenerActivos());
    }

    @GetMapping("/finalizados")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerFinalizados() {
        return ResponseEntity.ok(registroTenenciaService.obtenerFinalizados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroTenenciaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(registroTenenciaService.obtenerPorId(id));
    }

    @GetMapping("/inmueble/{idInmueble}")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerHistorialInmueble(@PathVariable Integer idInmueble) {
        return ResponseEntity.ok(registroTenenciaService.obtenerHistorialInmueble(idInmueble));
    }

    @GetMapping("/arrendatario/{idArrendatario}")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerHistorialArrendatario(@PathVariable Integer idArrendatario) {
        return ResponseEntity.ok(registroTenenciaService.obtenerHistorialArrendatario(idArrendatario));
    }

    @GetMapping("/inmueble/{idInmueble}/activa")
    public ResponseEntity<RegistroTenenciaDTO> obtenerTenenciaActivaInmueble(@PathVariable Integer idInmueble) {
        return ResponseEntity.ok(registroTenenciaService.obtenerTenenciaActivaInmueble(idInmueble));
    }

    @GetMapping("/arrendatario/{idArrendatario}/activas")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerTenenciasActivasArrendatario(@PathVariable Integer idArrendatario) {
        return ResponseEntity.ok(registroTenenciaService.obtenerTenenciasActivasArrendatario(idArrendatario));
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<RegistroTenenciaDTO>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(registroTenenciaService.obtenerPorRangoFechas(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<RegistroTenenciaDTO> crear(@Valid @RequestBody RegistroTenenciaCreateDTO dto) {
        RegistroTenenciaDTO creado = registroTenenciaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroTenenciaDTO> actualizar(@PathVariable Integer id, @RequestBody RegistroTenenciaUpdateDTO dto) {
        return ResponseEntity.ok(registroTenenciaService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<RegistroTenenciaDTO> finalizarTenencia(
            @PathVariable Integer id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal) {
        return ResponseEntity.ok(registroTenenciaService.finalizarTenencia(id, fechaFinal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        registroTenenciaService.eliminar(id);
        return ResponseEntity.ok("Registro de tenencia eliminado correctamente");
    }
}