package org.example.casa2girardot.Controllers;

import org.example.casa2girardot.Dtos.FacturaDTO;
import org.example.casa2girardot.Dtos.PagoFacturaDTO;
import org.example.casa2girardot.Services.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> obtenerTodas() {
        return ResponseEntity.ok(facturaService.obtenerTodas());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<FacturaDTO>> obtenerPendientes() {
        return ResponseEntity.ok(facturaService.obtenerPendientes());
    }

    @PostMapping("/generar/{idInmueble}")
    public ResponseEntity<?> generarFactura(@PathVariable Integer idInmueble) {
        try {
            FacturaDTO factura = facturaService.generarFactura(idInmueble);
            return ResponseEntity.status(HttpStatus.CREATED).body(factura);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{idFactura}/pagar")
    public ResponseEntity<?> registrarPago(
            @PathVariable Integer idFactura,
            @RequestBody PagoFacturaDTO pagoDTO) {
        try {
            FacturaDTO factura = facturaService.registrarPago(idFactura, pagoDTO);
            return ResponseEntity.ok(factura);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}