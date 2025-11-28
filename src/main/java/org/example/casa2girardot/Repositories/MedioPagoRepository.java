package org.example.casa2girardot.Repositories;

import org.example.casa2girardot.Entities.MedioPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPago, Integer> {
    Optional<MedioPago> findByNombre(String nombre);
}