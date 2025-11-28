package org.example.casa2girardot.Controllers;

import jakarta.validation.Valid;
import org.example.casa2girardot.Dtos.ArrendatarioCreateDTO;
import org.example.casa2girardot.Dtos.ArrendatarioDTO;
import org.example.casa2girardot.Dtos.ArrendatarioUpdateDTO;
import org.example.casa2girardot.Services.ArrendatarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arrendatarios")
@CrossOrigin(origins = "*")
public class ArrendatarioController {

    @Autowired
    private ArrendatarioService arrendatarioService;

    // Obtener todos los arrendatarios
    @GetMapping
    public ResponseEntity<List<ArrendatarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(arrendatarioService.obtenerTodos());
    }

    // Obtener arrendatarios activos
    @GetMapping("/activos")
    public ResponseEntity<List<ArrendatarioDTO>> obtenerActivos() {
        return ResponseEntity.ok(arrendatarioService.obtenerActivos());
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<ArrendatarioDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(arrendatarioService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener por email
    @GetMapping("/email/{email}")
    public ResponseEntity<ArrendatarioDTO> obtenerPorEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(arrendatarioService.obtenerPorEmail(email));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener por cédula
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<ArrendatarioDTO> obtenerPorCedula(@PathVariable String cedula) {
        try {
            return ResponseEntity.ok(arrendatarioService.obtenerPorCedula(cedula));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<ArrendatarioDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(arrendatarioService.buscarPorNombre(nombre));
    }

    // Crear arrendatario
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ArrendatarioCreateDTO dto) {
        try {
            ArrendatarioDTO creado = arrendatarioService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar arrendatario
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody ArrendatarioUpdateDTO dto) {
        try {
            ArrendatarioDTO actualizado = arrendatarioService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cambiar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean estado) {
        try {
            ArrendatarioDTO actualizado = arrendatarioService.cambiarEstado(id, estado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Desactivar (soft delete)
    @DeleteMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivar(@PathVariable Integer id) {
        try {
            arrendatarioService.desactivar(id);
            return ResponseEntity.ok("Arrendatario desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar permanentemente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            arrendatarioService.eliminar(id);
            return ResponseEntity.ok("Arrendatario eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cambiar contraseña
    @PatchMapping("/{id}/password")
    public ResponseEntity<String> cambiarPassword(
            @PathVariable Integer id,
            @RequestParam String nuevaPassword) {
        try {
            arrendatarioService.cambiarPassword(id, nuevaPassword);
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}