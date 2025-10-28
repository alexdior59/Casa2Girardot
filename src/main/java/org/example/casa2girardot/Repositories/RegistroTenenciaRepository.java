
package org.example.casa2girardot.Repositories;

import org.example.casa2girardot.Entities.Arrendatario;
import org.example.casa2girardot.Entities.Inmueble;
import org.example.casa2girardot.Entities.RegistroTenencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroTenenciaRepository extends JpaRepository<RegistroTenencia, Integer> {

    // Buscar registros activos (sin fecha final) de un inmueble
    List<RegistroTenencia> findByIdInmuebleAndFechaFinalIsNull(Inmueble inmueble);

    // Buscar historial completo de un inmueble
    List<RegistroTenencia> findByIdInmuebleOrderByFechaInicioDesc(Inmueble inmueble);

    // Buscar historial de un arrendatario
    List<RegistroTenencia> findByIdArrendatarioOrderByFechaInicioDesc(Arrendatario arrendatario);

    // Buscar tenencias activas (sin fecha final)
    List<RegistroTenencia> findByFechaFinalIsNull();

    // Buscar tenencias finalizadas
    List<RegistroTenencia> findByFechaFinalIsNotNull();

    // Buscar registros por rango de fechas
    List<RegistroTenencia> findByFechaInicioBetween(LocalDate inicio, LocalDate fin);

    // Buscar tenencias activas de un arrendatario
    List<RegistroTenencia> findByIdArrendatarioAndFechaFinalIsNull(Arrendatario arrendatario);
}