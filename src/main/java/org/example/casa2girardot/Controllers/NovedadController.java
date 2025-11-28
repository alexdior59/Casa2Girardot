package org.example.casa2girardot.Controllers;

import jakarta.validation.Valid;
import org.example.casa2girardot.Dtos.NovedadCreateDTO;
import org.example.casa2girardot.Dtos.NovedadDTO;
import org.example.casa2girardot.Services.NovedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        return ResponseEntity.ok(novedadService.obtenerPorInmueble(idInmueble));
    }

    @PostMapping
    public ResponseEntity<NovedadDTO> registrar(@Valid @RequestBody NovedadCreateDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        NovedadDTO creada = novedadService.registrarNovedad(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
}