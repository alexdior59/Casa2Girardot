package org.example.casa2girardot.Repositories;

import org.example.casa2girardot.Entities.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoFacturaRepository extends JpaRepository<EstadoFactura, Integer> {
    Optional<EstadoFactura> findByNombre(String nombre);
}