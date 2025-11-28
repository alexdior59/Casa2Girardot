package org.example.casa2girardot.Repositories;

import org.example.casa2girardot.Entities.ConfiguracionSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Integer> {
    Optional<ConfiguracionSistema> findByClave(String clave);
}