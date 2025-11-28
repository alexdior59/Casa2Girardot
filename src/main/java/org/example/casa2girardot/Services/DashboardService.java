package org.example.casa2girardot.Services;

import org.example.casa2girardot.Dtos.DashboardDTO;
import org.example.casa2girardot.Entities.Factura;
import org.example.casa2girardot.Entities.MedioPago;
import org.example.casa2girardot.Repositories.FacturaRepository;
import org.example.casa2girardot.Repositories.MedioPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private MedioPagoRepository medioPagoRepo;
    @Autowired
    private FacturaRepository facturaRepo;

    public DashboardDTO obtenerResumen() {
        List<MedioPago> medios = medioPagoRepo.findAll();
        List<Factura> facturas = facturaRepo.findAll();

        BigDecimal saldoTotal = medios.stream()
                .map(MedioPago::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> porBanco = new HashMap<>();
        medios.forEach(m -> porBanco.put(m.getNombre(), m.getBalance()));

        BigDecimal porCobrar = facturas.stream()
                .filter(f -> "PENDIENTE".equals(f.getEstadoFactura().getNombre()) || "VENCIDA".equals(f.getEstadoFactura().getNombre()))
                .map(Factura::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardDTO.builder()
                .saldoTotalBancos(saldoTotal)
                .desglosePorBanco(porBanco)
                .totalPorCobrar(porCobrar)
                .totalIngresosMes(BigDecimal.ZERO)
                .totalEgresosMes(BigDecimal.ZERO)
                .build();
    }
}