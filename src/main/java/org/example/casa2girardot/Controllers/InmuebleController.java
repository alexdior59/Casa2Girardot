package org.example.casa2girardot.Controllers;

import jakarta.validation.Valid;
import org.example.casa2girardot.Dtos.*;
import org.example.casa2girardot.Services.InmuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inmuebles")
@CrossOrigin(origins = "*")
public class InmuebleController {

    @Autowired
    private InmuebleService inmuebleService;

    @GetMapping
    public ResponseEntity<List<InmuebleDTO>> obtenerTodos() {
        return ResponseEntity.ok(inmuebleService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<InmuebleDTO>> obtenerActivos() {
        return ResponseEntity.ok(inmuebleService.obtenerActivos());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<InmuebleDTO>> obtenerDisponibles() {
        return ResponseEntity.ok(inmuebleService.obtenerDisponibles());
    }

    @GetMapping("/ocupados")
    public ResponseEntity<List<InmuebleDTO>> obtenerOcupados() {
        return ResponseEntity.ok(inmuebleService.obtenerOcupados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InmuebleDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(inmuebleService.obtenerPorId(id));
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<InmuebleDTO> obtenerPorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(inmuebleService.obtenerPorNumero(numero));
    }

    @GetMapping("/arrendatario/{idArrendatario}")
    public ResponseEntity<List<InmuebleDTO>> obtenerPorArrendatario(@PathVariable Integer idArrendatario) {
        return ResponseEntity.ok(inmuebleService.obtenerPorArrendatario(idArrendatario));
    }

    @PostMapping
    public ResponseEntity<InmuebleDTO> crear(@Valid @RequestBody InmuebleCreateDTO dto) {
        InmuebleDTO creado = inmuebleService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InmuebleDTO> actualizar(@PathVariable Integer id, @RequestBody InmuebleUpdateDTO dto) {
        return ResponseEntity.ok(inmuebleService.actualizar(id, dto));
    }

    @PostMapping("/{id}/asignar")
    public ResponseEntity<InmuebleDTO> asignarArrendatario(
            @PathVariable Integer id,
            @RequestBody InmuebleAsignarDTO dto) {
        return ResponseEntity.ok(inmuebleService.asignarArrendatario(id, dto.getIdArrendatario()));
    }

    @PostMapping("/{id}/liberar")
    public ResponseEntity<InmuebleDTO> liberarInmueble(@PathVariable Integer id) {
        return ResponseEntity.ok(inmuebleService.liberarInmueble(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<InmuebleDTO> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean estado) {
        return ResponseEntity.ok(inmuebleService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivar(@PathVariable Integer id) {
        inmuebleService.desactivar(id);
        return ResponseEntity.ok("Inmueble desactivado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        inmuebleService.eliminar(id);
        return ResponseEntity.ok("Inmueble eliminado correctamente");
    }
}