package org.example.casa2girardot.Services;

import org.example.casa2girardot.Dtos.NovedadCreateDTO;
import org.example.casa2girardot.Dtos.NovedadDTO;
import org.example.casa2girardot.Entities.Administrador;
import org.example.casa2girardot.Entities.Inmueble;
import org.example.casa2girardot.Entities.MedioPago;
import org.example.casa2girardot.Entities.Novedad;
import org.example.casa2girardot.Repositories.AdministradorRepository;
import org.example.casa2girardot.Repositories.InmuebleRepository;
import org.example.casa2girardot.Repositories.MedioPagoRepository;
import org.example.casa2girardot.Repositories.NovedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NovedadService {

    @Autowired
    private NovedadRepository novedadRepository;
    @Autowired
    private MedioPagoRepository medioPagoRepository;
    @Autowired
    private InmuebleRepository inmuebleRepository;
    @Autowired
    private AdministradorRepository administradorRepository;

    private NovedadDTO convertirADTO(Novedad novedad) {
        return NovedadDTO.builder()
                .id(novedad.getId())
                .tipoMovimiento(novedad.getTipoMovimiento())
                .monto(novedad.getMonto())
                .medioPagoNombre(novedad.getMedioPago().getNombre())
                .numeroInmueble(novedad.getIdInmueble() != null ? novedad.getIdInmueble().getNumero() : "General")
                .nombreAdministrador(novedad.getIdAdministrador().getNombre())
                .concepto(novedad.getConcepto())
                .descripcion(novedad.getDescripcion())
                .fecha(novedad.getFecha())
                .numeroReferencia(novedad.getNumeroReferencia())
                .build();
    }

    @Transactional
    public NovedadDTO registrarNovedad(NovedadCreateDTO dto, String emailAdminLogueado) {

        MedioPago medioPago = medioPagoRepository.findById(dto.getIdMedioPago())
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        Administrador admin = administradorRepository.findByEmail(emailAdminLogueado)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado (Error de seguridad)"));

        Inmueble inmueble = null;
        if (dto.getIdInmueble() != null) {
            inmueble = inmuebleRepository.findById(dto.getIdInmueble())
                    .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));
        }

        if ("EGRESO".equalsIgnoreCase(dto.getTipoMovimiento())) {
            if (medioPago.getBalance().compareTo(dto.getMonto()) < 0) {
                throw new RuntimeException("Fondos insuficientes en " + medioPago.getNombre() + " para realizar este egreso.");
            }
            medioPago.setBalance(medioPago.getBalance().subtract(dto.getMonto()));
        } else if ("INGRESO".equalsIgnoreCase(dto.getTipoMovimiento())) {
            medioPago.setBalance(medioPago.getBalance().add(dto.getMonto()));
        } else {
            throw new RuntimeException("Tipo de movimiento inválido. Use 'INGRESO' o 'EGRESO'");
        }

        medioPagoRepository.save(medioPago);

        Novedad novedad = Novedad.builder()
                .tipoMovimiento(dto.getTipoMovimiento().toUpperCase())
                .monto(dto.getMonto())
                .medioPago(medioPago)
                .idInmueble(inmueble)
                .idAdministrador(admin)
                .concepto(dto.getConcepto())
                .descripcion(dto.getDescripcion())
                .fecha(LocalDateTime.now())
                .numeroReferencia(dto.getNumeroReferencia())
                .idAdministrador(admin)
                .build();

        return convertirADTO(novedadRepository.save(novedad));
    }

    public List<NovedadDTO> obtenerTodas() {
        return novedadRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<NovedadDTO> obtenerPorInmueble(Integer idInmueble) {
        Inmueble inmueble = inmuebleRepository.findById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));

        return novedadRepository.findByIdInmuebleOrderByFechaDesc(inmueble).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
}