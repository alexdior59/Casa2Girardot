package org.example.casa2girardot.Controllers;

import org.example.casa2girardot.Dtos.NovedadCreateDTO;
import org.example.casa2girardot.Dtos.NovedadDTO;
import org.example.casa2girardot.Services.NovedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/novedades")
@CrossOrigin(origins = "*")
public class NovedadController {

    @Autowired
    private NovedadService novedadService;

    @GetMapping
    public ResponseEntity<List<NovedadDTO>> obtenerTodas() {
        return ResponseEntity.ok(novedadService.obtenerTodas());
    }

    @GetMapping("/inmueble/{idInmueble}")
    public ResponseEntity<List<NovedadDTO>> obtenerPorInmueble(@PathVariable Integer idInmueble) {
        try {
            return ResponseEntity.ok(novedadService.obtenerPorInmueble(idInmueble));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody NovedadCreateDTO dto) {
        try {
            NovedadDTO creada = novedadService.registrarNovedad(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}