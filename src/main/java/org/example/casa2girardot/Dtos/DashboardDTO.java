package org.example.casa2girardot.Dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class DashboardDTO {
    private BigDecimal saldoTotalBancos;
    private BigDecimal totalPorCobrar;
    private BigDecimal totalIngresosMes;
    private BigDecimal totalEgresosMes;
    private Map<String, BigDecimal> desglosePorBanco;
}