package org.example.casa2girardot.Services;

import org.example.casa2girardot.Dtos.*;
import org.example.casa2girardot.Entities.Arrendatario;
import org.example.casa2girardot.Entities.Inmueble;
import org.example.casa2girardot.Entities.RegistroTenencia;
import org.example.casa2girardot.Repositories.ArrendatarioRepository;
import org.example.casa2girardot.Repositories.InmuebleRepository;
import org.example.casa2girardot.Repositories.RegistroTenenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InmuebleService {

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private ArrendatarioRepository arrendatarioRepository;

    @Autowired
    private RegistroTenenciaRepository registroTenenciaRepository;

    // Convertir Entity a DTO
    private InmuebleDTO convertirADTO(Inmueble inmueble) {
        return InmuebleDTO.builder()
                .id(inmueble.getId())
                .numero(inmueble.getNumero())
                .valorArriendo(inmueble.getValorArriendo())
                .idArrendatario(inmueble.getIdArrendatario() != null ?
                        inmueble.getIdArrendatario().getId() : null)
                .nombreArrendatario(inmueble.getIdArrendatario() != null ?
                        inmueble.getIdArrendatario().getNombre() : null)
                .estado(inmueble.getEstado())
                .ocupado(inmueble.getIdArrendatario() != null)
                .build();
    }

    // Obtener todos los inmuebles
    public List<InmuebleDTO> obtenerTodos() {
        return inmuebleRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener inmuebles activos
    public List<InmuebleDTO> obtenerActivos() {
        return inmuebleRepository.findByEstadoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener inmuebles disponibles (sin arrendatario y activos)
    public List<InmuebleDTO> obtenerDisponibles() {
        return inmuebleRepository.findByIdArrendatarioIsNullAndEstadoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener inmuebles ocupados
    public List<InmuebleDTO> obtenerOcupados() {
        return inmuebleRepository.findByIdArrendatarioIsNotNull().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    public InmuebleDTO obtenerPorId(Integer id) {
        Inmueble inmueble = inmuebleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con ID: " + id));
        return convertirADTO(inmueble);
    }

    // Obtener por número
    public InmuebleDTO obtenerPorNumero(String numero) {
        Inmueble inmueble = inmuebleRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con número: " + numero));
        return convertirADTO(inmueble);
    }

    // Obtener inmuebles de un arrendatario
    public List<InmuebleDTO> obtenerPorArrendatario(Integer idArrendatario) {
        Arrendatario arrendatario = arrendatarioRepository.findById(idArrendatario)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con ID: " + idArrendatario));

        return inmuebleRepository.findByIdArrendatario(arrendatario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Crear inmueble
    @Transactional
    public InmuebleDTO crear(InmuebleCreateDTO dto) {
        // Validar que no exista el número
        if (inmuebleRepository.existsByNumero(dto.getNumero())) {
            throw new RuntimeException("Ya existe un inmueble con el número: " + dto.getNumero());
        }

        Inmueble inmueble = Inmueble.builder()
                .numero(dto.getNumero())
                .valorArriendo(dto.getValorArriendo())
                .idArrendatario(null) // Inicia sin arrendatario
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .build();

        Inmueble guardado = inmuebleRepository.save(inmueble);
        return convertirADTO(guardado);
    }

    // Actualizar inmueble
    @Transactional
    public InmuebleDTO actualizar(Integer id, InmuebleUpdateDTO dto) {
        Inmueble inmueble = inmuebleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con ID: " + id));

        // Validar número si cambió
        if (dto.getNumero() != null && !dto.getNumero().equals(inmueble.getNumero())) {
            if (inmuebleRepository.existsByNumero(dto.getNumero())) {
                throw new RuntimeException("Ya existe un inmueble con el número: " + dto.getNumero());
            }
            inmueble.setNumero(dto.getNumero());
        }

        // Actualizar campos
        if (dto.getValorArriendo() != null) inmueble.setValorArriendo(dto.getValorArriendo());
        if (dto.getEstado() != null) inmueble.setEstado(dto.getEstado());

        Inmueble actualizado = inmuebleRepository.save(inmueble);
        return convertirADTO(actualizado);
    }

    // Asignar arrendatario al inmueble
    @Transactional
    public InmuebleDTO asignarArrendatario(Integer idInmueble, Integer idArrendatario) {
        Inmueble inmueble = inmuebleRepository.findById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con ID: " + idInmueble));

        Arrendatario arrendatario = arrendatarioRepository.findById(idArrendatario)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con ID: " + idArrendatario));

        // Verificar que el arrendatario esté activo
        if (!arrendatario.getEstado()) {
            throw new RuntimeException("El arrendatario no está activo");
        }

        // Si el inmueble ya tiene arrendatario, cerrar el registro anterior
        if (inmueble.getIdArrendatario() != null) {
            cerrarRegistroTenenciaActual(inmueble);
        }

        // Asignar nuevo arrendatario
        inmueble.setIdArrendatario(arrendatario);
        Inmueble actualizado = inmuebleRepository.save(inmueble);

        // Crear registro de tenencia
        RegistroTenencia registro = RegistroTenencia.builder()
                .idArrendatario(arrendatario)
                .idInmueble(inmueble)
                .fechaInicio(LocalDate.now())
                .fechaFinal(null)
                .build();
        registroTenenciaRepository.save(registro);

        return convertirADTO(actualizado);
    }

    // Liberar inmueble (quitar arrendatario)
    @Transactional
    public InmuebleDTO liberarInmueble(Integer idInmueble) {
        Inmueble inmueble = inmuebleRepository.findById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con ID: " + idInmueble));

        if (inmueble.getIdArrendatario() == null) {
            throw new RuntimeException("El inmueble ya está desocupado");
        }

        // Cerrar registro de tenencia actual
        cerrarRegistroTenenciaActual(inmueble);

        // Liberar el inmueble
        inmueble.setIdArrendatario(null);
        Inmueble actualizado = inmuebleRepository.save(inmueble);

        return convertirADTO(actualizado);
    }

    // Método auxiliar para cerrar registro de tenencia
    private void cerrarRegistroTenenciaActual(Inmueble inmueble) {
        List<RegistroTenencia> registrosActivos = registroTenenciaRepository
                .findByIdInmuebleAndFechaFinalIsNull(inmueble);

        for (RegistroTenencia registro : registrosActivos) {
            registro.setFechaFinal(LocalDate.now());
            registroTenenciaRepository.save(registro);
        }
    }

    // Cambiar estado
    @Transactional
    public InmuebleDTO cambiarEstado(Integer id, Boolean nuevoEstado) {
        Inmueble inmueble = inmuebleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con ID: " + id));

        inmueble.setEstado(nuevoEstado);
        Inmueble actualizado = inmuebleRepository.save(inmueble);
        return convertirADTO(actualizado);
    }

    // Desactivar (soft delete)
    @Transactional
    public void desactivar(Integer id) {
        cambiarEstado(id, false);
    }

    // Eliminar permanentemente
    @Transactional
    public void eliminar(Integer id) {
        Inmueble inmueble = inmuebleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con ID: " + id));

        // Verificar que no esté ocupado
        if (inmueble.getIdArrendatario() != null) {
            throw new RuntimeException("No se puede eliminar un inmueble ocupado. Libérelo primero.");
        }

        inmuebleRepository.deleteById(id);
    }
}