package org.example.casa2girardot.Tasks;

import org.example.casa2girardot.Services.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TareasProgramadas {

    @Autowired
    private FacturaService facturaService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void calcularMorasDiarias() {
        System.out.println("⏰ Iniciando cálculo automático de moras...");
        facturaService.actualizarMoras();
        System.out.println("✅ Cálculo de moras finalizado.");
    }
}