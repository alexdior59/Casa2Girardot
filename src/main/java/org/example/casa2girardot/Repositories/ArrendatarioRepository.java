package org.example.casa2girardot.Repositories;

import org.example.casa2girardot.Entities.Arrendatario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArrendatarioRepository extends JpaRepository<Arrendatario, Integer> {

    // Buscar por email
    Optional<Arrendatario> findByEmail(String email);

    // Buscar por cédula
    Optional<Arrendatario> findByCedula(String cedula);

    // Buscar arrendatarios activos
    List<Arrendatario> findByEstadoTrue();

    // Buscar arrendatarios inactivos
    List<Arrendatario> findByEstadoFalse();

    // Verificar si existe por email
    boolean existsByEmail(String email);

    // Verificar si existe por cédula
    boolean existsByCedula(String cedula);

    // Buscar por nombre (contiene)
    List<Arrendatario> findByNombreContainingIgnoreCase(String nombre);
}