package org.example.casa2girardot.Services;

import org.example.casa2girardot.Dtos.ArrendatarioCreateDTO;
import org.example.casa2girardot.Dtos.ArrendatarioDTO;
import org.example.casa2girardot.Dtos.ArrendatarioUpdateDTO;
import org.example.casa2girardot.Entities.Arrendatario;
import org.example.casa2girardot.Repositories.ArrendatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArrendatarioService {

    @Autowired
    private ArrendatarioRepository arrendatarioRepository;

    // Convertir Entity a DTO
    private ArrendatarioDTO convertirADTO(Arrendatario arrendatario) {
        return ArrendatarioDTO.builder()
                .id(arrendatario.getId())
                .nombre(arrendatario.getNombre())
                .celular(arrendatario.getCelular())
                .cedula(arrendatario.getCedula())
                .email(arrendatario.getEmail())
                .estado(arrendatario.getEstado())
                .build();
    }

    // Obtener todos los arrendatarios
    public List<ArrendatarioDTO> obtenerTodos() {
        return arrendatarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener arrendatarios activos
    public List<ArrendatarioDTO> obtenerActivos() {
        return arrendatarioRepository.findByEstadoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    public ArrendatarioDTO obtenerPorId(Integer id) {
        Arrendatario arrendatario = arrendatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con ID: " + id));
        return convertirADTO(arrendatario);
    }

    // Obtener por email
    public ArrendatarioDTO obtenerPorEmail(String email) {
        Arrendatario arrendatario = arrendatarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con email: " + email));
        return convertirADTO(arrendatario);
    }

    // Obtener por cédula
    public ArrendatarioDTO obtenerPorCedula(String cedula) {
        Arrendatario arrendatario = arrendatarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con cédula: " + cedula));
        return convertirADTO(arrendatario);
    }

    // Buscar por nombre
    public List<ArrendatarioDTO> buscarPorNombre(String nombre) {
        return arrendatarioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Crear arrendatario
    @Transactional
    public ArrendatarioDTO crear(ArrendatarioCreateDTO dto) {
        // Validar que no exista el email
        if (arrendatarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un arrendatario con el email: " + dto.getEmail());
        }

        // Validar que no exista la cédula
        if (arrendatarioRepository.existsByCedula(dto.getCedula())) {
            throw new RuntimeException("Ya existe un arrendatario con la cédula: " + dto.getCedula());
        }

        Arrendatario arrendatario = Arrendatario.builder()
                .nombre(dto.getNombre())
                .celular(dto.getCelular())
                .cedula(dto.getCedula())
                .email(dto.getEmail())
                .password(dto.getPassword()) // TODO: Hashear la contraseña con BCrypt
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .build();

        Arrendatario guardado = arrendatarioRepository.save(arrendatario);
        return convertirADTO(guardado);
    }

    // Actualizar arrendatario
    @Transactional
    public ArrendatarioDTO actualizar(Integer id, ArrendatarioUpdateDTO dto) {
        Arrendatario arrendatario = arrendatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con ID: " + id));

        // Validar email si cambió
        if (dto.getEmail() != null && !dto.getEmail().equals(arrendatario.getEmail())) {
            if (arrendatarioRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Ya existe un arrendatario con el email: " + dto.getEmail());
            }
            arrendatario.setEmail(dto.getEmail());
        }

        // Actualizar campos
        if (dto.getNombre() != null) arrendatario.setNombre(dto.getNombre());
        if (dto.getCelular() != null) arrendatario.setCelular(dto.getCelular());
        if (dto.getEstado() != null) arrendatario.setEstado(dto.getEstado());

        Arrendatario actualizado = arrendatarioRepository.save(arrendatario);
        return convertirADTO(actualizado);
    }

    // Cambiar estado (activar/desactivar)
    @Transactional
    public ArrendatarioDTO cambiarEstado(Integer id, Boolean nuevoEstado) {
        Arrendatario arrendatario = arrendatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con ID: " + id));

        arrendatario.setEstado(nuevoEstado);
        Arrendatario actualizado = arrendatarioRepository.save(arrendatario);
        return convertirADTO(actualizado);
    }

    // Eliminar arrendatario (soft delete - cambiar estado a false)
    @Transactional
    public void desactivar(Integer id) {
        cambiarEstado(id, false);
    }

    // Eliminar arrendatario (hard delete - eliminar de BD)
    @Transactional
    public void eliminar(Integer id) {
        if (!arrendatarioRepository.existsById(id)) {
            throw new RuntimeException("Arrendatario no encontrado con ID: " + id);
        }
        arrendatarioRepository.deleteById(id);
    }

    // Cambiar contraseña
    @Transactional
    public void cambiarPassword(Integer id, String nuevaPassword) {
        Arrendatario arrendatario = arrendatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con ID: " + id));

        arrendatario.setPassword(nuevaPassword); // TODO: Hashear con BCrypt
        arrendatarioRepository.save(arrendatario);
    }
}