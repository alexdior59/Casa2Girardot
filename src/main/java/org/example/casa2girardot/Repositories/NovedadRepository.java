package org.example.casa2girardot.Repositories;

import org.example.casa2girardot.Entities.Inmueble;
import org.example.casa2girardot.Entities.Novedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NovedadRepository extends JpaRepository<Novedad, Integer> {
    List<Novedad> findByIdInmuebleOrderByFechaDesc(Inmueble inmueble);

    List<Novedad> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}