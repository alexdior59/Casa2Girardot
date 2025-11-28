package org.example.casa2girardot.Services;

import org.example.casa2girardot.Dtos.FacturaDTO;
import org.example.casa2girardot.Dtos.PagoFacturaDTO;
import org.example.casa2girardot.Entities.*;
import org.example.casa2girardot.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private InmuebleRepository inmuebleRepository;
    @Autowired
    private EstadoFacturaRepository estadoFacturaRepository;
    @Autowired
    private MedioPagoRepository medioPagoRepository;
    @Autowired
    private ConfiguracionSistemaRepository configRepository;

    private FacturaDTO convertirADTO(Factura factura) {
        return FacturaDTO.builder()
                .id(factura.getId())
                .numeroFactura(factura.getNumeroFactura())
                .monto(factura.getMonto())
                .mora(factura.getMora())
                .total(factura.getTotal())
                .fechaGeneracion(factura.getFechaGeneracion().toLocalDate())
                .fechaVencimiento(factura.getFechaVencimiento())
                .fechaPago(factura.getFechaPago())
                .nombreArrendatario(factura.getIdArrendatario().getNombre())
                .numeroInmueble(factura.getIdInmueble().getNumero())
                .estado(factura.getEstadoFactura().getNombre())
                .medioPagoNombre(factura.getMedioPago() != null ? factura.getMedioPago().getNombre() : "N/A")
                .comprobantePago(factura.getComprobantePago())
                .observaciones(factura.getObservaciones())
                .build();
    }

    @Transactional
    public FacturaDTO generarFactura(Integer idInmueble) {
        Inmueble inmueble = inmuebleRepository.findById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));

        if (inmueble.getIdArrendatario() == null) {
            throw new RuntimeException("No se puede generar factura a un inmueble desocupado");
        }

        EstadoFactura estadoPendiente = estadoFacturaRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado 'PENDIENTE' no configurado en BD"));

        LocalDate hoy = LocalDate.now();
        LocalDate vencimiento = hoy.plusDays(5);

        Factura factura = Factura.builder()
                .numeroFactura("FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .monto(inmueble.getValorArriendo())
                .mora(BigDecimal.ZERO)
                .total(inmueble.getValorArriendo())
                .fechaGeneracion(LocalDateTime.now())
                .fechaCobro(hoy)
                .fechaVencimiento(vencimiento)
                .idArrendatario(inmueble.getIdArrendatario())
                .idInmueble(inmueble)
                .estadoFactura(estadoPendiente)
                .mes(hoy.getMonthValue())
                .anio(hoy.getYear())
                .build();

        return convertirADTO(facturaRepository.save(factura));
    }

    @Transactional
    public FacturaDTO registrarPago(Integer idFactura, PagoFacturaDTO pagoDTO) {
        Factura factura = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        if ("PAGADA".equalsIgnoreCase(factura.getEstadoFactura().getNombre())) {
            throw new RuntimeException("La factura ya está pagada");
        }

        MedioPago medioPago = medioPagoRepository.findById(pagoDTO.getIdMedioPago())
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        EstadoFactura estadoPagada = estadoFacturaRepository.findByNombre("PAGADA")
                .orElseThrow(() -> new RuntimeException("Estado 'PAGADA' no configurado en BD"));

        factura.setEstadoFactura(estadoPagada);
        factura.setFechaPago(pagoDTO.getFechaPago() != null ? pagoDTO.getFechaPago() : LocalDate.now());
        factura.setMedioPago(medioPago);
        factura.setComprobantePago(pagoDTO.getComprobante());
        factura.setObservaciones(pagoDTO.getObservaciones());

        medioPago.setBalance(medioPago.getBalance().add(factura.getTotal()));
        medioPagoRepository.save(medioPago);

        Factura facturaGuardada = facturaRepository.save(factura);
        return convertirADTO(facturaGuardada);
    }

    public List<FacturaDTO> obtenerTodas() {
        return facturaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<FacturaDTO> obtenerPendientes() {
        return facturaRepository.findByEstadoFactura_Nombre("PENDIENTE").stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void actualizarMoras() {
        String valorMora = configRepository.findByClave("PORCENTAJE_MORA_DIARIA")
                .orElseThrow(() -> new RuntimeException("Configuración de mora no encontrada"))
                .getValor();
        BigDecimal porcentajeDiario = new BigDecimal(valorMora).divide(BigDecimal.valueOf(100));

        List<Factura> facturasVencidas = facturaRepository.findAll().stream()
                .filter(f -> "PENDIENTE".equals(f.getEstadoFactura().getNombre()))
                .filter(f -> f.getFechaVencimiento().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        for (Factura factura : facturasVencidas) {
            long diasRetraso = ChronoUnit.DAYS.between(factura.getFechaVencimiento(), LocalDate.now());

            if (diasRetraso > 0) {
                BigDecimal moraCalculada = factura.getMonto()
                        .multiply(porcentajeDiario)
                        .multiply(BigDecimal.valueOf(diasRetraso));

                factura.setMora(moraCalculada);
                factura.setTotal(factura.getMonto().add(moraCalculada));

                EstadoFactura estadoVencida = estadoFacturaRepository.findByNombre("VENCIDA").orElse(null);
                if (estadoVencida != null) {
                    factura.setEstadoFactura(estadoVencida);
                }
            }
        }
        facturaRepository.saveAll(facturasVencidas);
    }
}