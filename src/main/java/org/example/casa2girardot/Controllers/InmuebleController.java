package org.example.casa2girardot.Controllers;

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

    // Obtener todos los inmuebles
    @GetMapping
    public ResponseEntity<List<InmuebleDTO>> obtenerTodos() {
        return ResponseEntity.ok(inmuebleService.obtenerTodos());
    }

    // Obtener inmuebles activos
    @GetMapping("/activos")
    public ResponseEntity<List<InmuebleDTO>> obtenerActivos() {
        return ResponseEntity.ok(inmuebleService.obtenerActivos());
    }

    // Obtener inmuebles disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<InmuebleDTO>> obtenerDisponibles() {
        return ResponseEntity.ok(inmuebleService.obtenerDisponibles());
    }

    // Obtener inmuebles ocupados
    @GetMapping("/ocupados")
    public ResponseEntity<List<InmuebleDTO>> obtenerOcupados() {
        return ResponseEntity.ok(inmuebleService.obtenerOcupados());
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<InmuebleDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(inmuebleService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener por número
    @GetMapping("/numero/{numero}")
    public ResponseEntity<InmuebleDTO> obtenerPorNumero(@PathVariable String numero) {
        try {
            return ResponseEntity.ok(inmuebleService.obtenerPorNumero(numero));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener inmuebles de un arrendatario
    @GetMapping("/arrendatario/{idArrendatario}")
    public ResponseEntity<List<InmuebleDTO>> obtenerPorArrendatario(@PathVariable Integer idArrendatario) {
        try {
            return ResponseEntity.ok(inmuebleService.obtenerPorArrendatario(idArrendatario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Crear inmueble
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody InmuebleCreateDTO dto) {
        try {
            InmuebleDTO creado = inmuebleService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar inmueble
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody InmuebleUpdateDTO dto) {
        try {
            InmuebleDTO actualizado = inmuebleService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Asignar arrendatario
    @PostMapping("/{id}/asignar")
    public ResponseEntity<?> asignarArrendatario(
            @PathVariable Integer id,
            @RequestBody InmuebleAsignarDTO dto) {
        try {
            InmuebleDTO actualizado = inmuebleService.asignarArrendatario(id, dto.getIdArrendatario());
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Liberar inmueble
    @PostMapping("/{id}/liberar")
    public ResponseEntity<?> liberarInmueble(@PathVariable Integer id) {
        try {
            InmuebleDTO actualizado = inmuebleService.liberarInmueble(id);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cambiar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean estado) {
        try {
            InmuebleDTO actualizado = inmuebleService.cambiarEstado(id, estado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Desactivar
    @DeleteMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivar(@PathVariable Integer id) {
        try {
            inmuebleService.desactivar(id);
            return ResponseEntity.ok("Inmueble desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar permanentemente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            inmuebleService.eliminar(id);
            return ResponseEntity.ok("Inmueble eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}