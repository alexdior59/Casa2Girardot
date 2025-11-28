package org.example.casa2girardot.Configuration;

import org.example.casa2girardot.Entities.Administrador;
import org.example.casa2girardot.Entities.ConfiguracionSistema;
import org.example.casa2girardot.Entities.EstadoFactura;
import org.example.casa2girardot.Entities.MedioPago;
import org.example.casa2girardot.Repositories.AdministradorRepository;
import org.example.casa2girardot.Repositories.ConfiguracionSistemaRepository;
import org.example.casa2girardot.Repositories.EstadoFacturaRepository;
import org.example.casa2girardot.Repositories.MedioPagoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            EstadoFacturaRepository estadoRepo,
            AdministradorRepository adminRepo,
            MedioPagoRepository medioPagoRepo,
            PasswordEncoder passwordEncoder,
            ConfiguracionSistemaRepository configRepo
    ) {
        return args -> {
            if (estadoRepo.count() == 0) {
                estadoRepo.saveAll(List.of(
                        EstadoFactura.builder()
                                .nombre("PENDIENTE")
                                .descripcion("Factura generada y pendiente de pago")
                                .activo(true)
                                .codigo("PEND")
                                .build(),
                        EstadoFactura.builder()
                                .nombre("PAGADA")
                                .descripcion("Factura cancelada totalmente")
                                .activo(true)
                                .codigo("PAG")
                                .build(),
                        EstadoFactura.builder()
                                .nombre("VENCIDA")
                                .descripcion("Factura con fecha límite superada")
                                .activo(true)
                                .codigo("VENC")
                                .build()
                ));
                System.out.println("✅ Estados de factura precargados.");
            }

            if (medioPagoRepo.count() == 0) {
                medioPagoRepo.saveAll(List.of(
                        MedioPago.builder()
                                .nombre("Efectivo")
                                .balance(BigDecimal.ZERO)
                                .descripcion("Caja menor / Efectivo en mano")
                                .activo(true)
                                .build(),
                        MedioPago.builder()
                                .nombre("Bancolombia")
                                .balance(BigDecimal.ZERO)
                                .descripcion("Cuenta de Ahorros Principal")
                                .activo(true)
                                .build(),
                        MedioPago.builder()
                                .nombre("Nequi")
                                .balance(BigDecimal.ZERO)
                                .descripcion("Cuenta móvil para pagos rápidos")
                                .activo(true)
                                .build()
                ));
                System.out.println("✅ Medios de pago precargados.");
            }

            // 3. Configuración del Sistema
            if (configRepo.findByClave("PORCENTAJE_MORA_DIARIA").isEmpty()) {
                configRepo.save(
                        ConfiguracionSistema.builder()
                                .clave("PORCENTAJE_MORA_DIARIA")
                                .valor("0.5")
                                .descripcion("Porcentaje de interés diario aplicado tras vencimiento")
                                .build()
                );
                System.out.println("✅ Configuración del sistema precargada.");
            }

            if (adminRepo.count() == 0) {
                adminRepo.save(
                        Administrador.builder()
                                .nombre("Admin Principal")
                                .email("admin@casa2.com")
                                .password(passwordEncoder.encode("123456"))
                                .celular("3001234567")
                                .build()
                );
                System.out.println("✅ Administrador por defecto creado (ID: 1)");
            }
        };
    }
}