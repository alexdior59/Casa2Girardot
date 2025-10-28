package org.example.casa2girardot.Repositories;

import org.example.casa2girardot.Entities.Arrendatario;
import org.example.casa2girardot.Entities.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, Integer> {

    // Buscar por número de inmueble
    Optional<Inmueble> findByNumero(String numero);

    // Verificar si existe por número
    boolean existsByNumero(String numero);

    // Buscar inmuebles activos
    List<Inmueble> findByEstadoTrue();

    // Buscar inmuebles inactivos
    List<Inmueble> findByEstadoFalse();

    // Buscar inmuebles por arrendatario
    List<Inmueble> findByIdArrendatario(Arrendatario arrendatario);

    // Buscar inmuebles ocupados (con arrendatario)
    List<Inmueble> findByIdArrendatarioIsNotNull();

    // Buscar inmuebles disponibles (sin arrendatario y activos)
    List<Inmueble> findByIdArrendatarioIsNullAndEstadoTrue();

    // Buscar inmuebles desocupados (sin arrendatario)
    List<Inmueble> findByIdArrendatarioIsNull();
}