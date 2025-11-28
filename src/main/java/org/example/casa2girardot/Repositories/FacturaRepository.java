package org.example.casa2girardot.Repositories;

import org.example.casa2girardot.Entities.Arrendatario;
import org.example.casa2girardot.Entities.Factura;
import org.example.casa2girardot.Entities.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    List<Factura> findByIdArrendatario(Arrendatario arrendatario);
    List<Factura> findByIdInmueble(Inmueble inmueble);
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    List<Factura> findByEstadoFactura_Nombre(String nombreEstado);
}