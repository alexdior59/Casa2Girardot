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

    @GetMapping
    public ResponseEntity<List<ArrendatarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(arrendatarioService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ArrendatarioDTO>> obtenerActivos() {
        return ResponseEntity.ok(arrendatarioService.obtenerActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArrendatarioDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(arrendatarioService.obtenerPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ArrendatarioDTO> obtenerPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(arrendatarioService.obtenerPorEmail(email));
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<ArrendatarioDTO> obtenerPorCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(arrendatarioService.obtenerPorCedula(cedula));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ArrendatarioDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(arrendatarioService.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<ArrendatarioDTO> crear(@Valid @RequestBody ArrendatarioCreateDTO dto) {
        ArrendatarioDTO creado = arrendatarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArrendatarioDTO> actualizar(@PathVariable Integer id, @RequestBody ArrendatarioUpdateDTO dto) {
        return ResponseEntity.ok(arrendatarioService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ArrendatarioDTO> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean estado) {
        return ResponseEntity.ok(arrendatarioService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivar(@PathVariable Integer id) {
        arrendatarioService.desactivar(id);
        return ResponseEntity.ok("Arrendatario desactivado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        arrendatarioService.eliminar(id);
        return ResponseEntity.ok("Arrendatario eliminado correctamente");
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> cambiarPassword(@PathVariable Integer id, @RequestParam String nuevaPassword) {
        arrendatarioService.cambiarPassword(id, nuevaPassword);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}