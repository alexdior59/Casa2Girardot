package org.example.casa2girardot.Services;

import org.example.casa2girardot.Dtos.RegistroTenenciaCreateDTO;
import org.example.casa2girardot.Dtos.RegistroTenenciaDTO;
import org.example.casa2girardot.Dtos.RegistroTenenciaUpdateDTO;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistroTenenciaService {

    @Autowired
    private RegistroTenenciaRepository registroTenenciaRepository;

    @Autowired
    private ArrendatarioRepository arrendatarioRepository;

    @Autowired
    private InmuebleRepository inmuebleRepository;

    // Convertir Entity a DTO
    private RegistroTenenciaDTO convertirADTO(RegistroTenencia registro) {
        Long diasTenencia = null;
        if (registro.getFechaFinal() != null) {
            diasTenencia = ChronoUnit.DAYS.between(registro.getFechaInicio(), registro.getFechaFinal());
        } else {
            diasTenencia = ChronoUnit.DAYS.between(registro.getFechaInicio(), LocalDate.now());
        }

        return RegistroTenenciaDTO.builder()
                .id(registro.getId())
                .idArrendatario(registro.getIdArrendatario().getId())
                .nombreArrendatario(registro.getIdArrendatario().getNombre())
                .cedulaArrendatario(registro.getIdArrendatario().getCedula())
                .idInmueble(registro.getIdInmueble().getId())
                .numeroInmueble(registro.getIdInmueble().getNumero())
                .fechaInicio(registro.getFechaInicio())
                .fechaFinal(registro.getFechaFinal())
                .activo(registro.getFechaFinal() == null)
                .diasTenencia(diasTenencia)
                .build();
    }

    // Obtener todos los registros
    public List<RegistroTenenciaDTO> obtenerTodos() {
        return registroTenenciaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener registros activos (sin fecha final)
    public List<RegistroTenenciaDTO> obtenerActivos() {
        return registroTenenciaRepository.findByFechaFinalIsNull().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener registros finalizados
    public List<RegistroTenenciaDTO> obtenerFinalizados() {
        return registroTenenciaRepository.findByFechaFinalIsNotNull().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    public RegistroTenenciaDTO obtenerPorId(Integer id) {
        RegistroTenencia registro = registroTenenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de tenencia no encontrado con ID: " + id));
        return convertirADTO(registro);
    }

    // Obtener historial de un inmueble
    public List<RegistroTenenciaDTO> obtenerHistorialInmueble(Integer idInmueble) {
        Inmueble inmueble = inmuebleRepository.findById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con ID: " + idInmueble));

        return registroTenenciaRepository.findByIdInmuebleOrderByFechaInicioDesc(inmueble).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener historial de un arrendatario
    public List<RegistroTenenciaDTO> obtenerHistorialArrendatario(Integer idArrendatario) {
        Arrendatario arrendatario = arrendatarioRepository.findById(idArrendatario)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con ID: " + idArrendatario));

        return registroTenenciaRepository.findByIdArrendatarioOrderByFechaInicioDesc(arrendatario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener tenencia activa de un inmueble
    public RegistroTenenciaDTO obtenerTenenciaActivaInmueble(Integer idInmueble) {
        Inmueble inmueble = inmuebleRepository.findById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con ID: " + idInmueble));

        List<RegistroTenencia> registrosActivos = registroTenenciaRepository
                .findByIdInmuebleAndFechaFinalIsNull(inmueble);

        if (registrosActivos.isEmpty()) {
            throw new RuntimeException("No hay tenencia activa para el inmueble: " + inmueble.getNumero());
        }

        return convertirADTO(registrosActivos.get(0));
    }

    // Obtener tenencias activas de un arrendatario
    public List<RegistroTenenciaDTO> obtenerTenenciasActivasArrendatario(Integer idArrendatario) {
        Arrendatario arrendatario = arrendatarioRepository.findById(idArrendatario)
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado con ID: " + idArrendatario));

        return registroTenenciaRepository.findByIdArrendatarioAndFechaFinalIsNull(arrendatario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Crear registro manualmente (normalmente se crea automáticamente desde InmuebleService)
    @Transactional
    public RegistroTenenciaDTO crear(RegistroTenenciaCreateDTO dto) {
        Arrendatario arrendatario = arrendatarioRepository.findById(dto.getIdArrendatario())
                .orElseThrow(() -> new RuntimeException("Arrendatario no encontrado"));

        Inmueble inmueble = inmuebleRepository.findById(dto.getIdInmueble())
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));

        // Validar que no haya registros activos para este inmueble
        List<RegistroTenencia> registrosActivos = registroTenenciaRepository
                .findByIdInmuebleAndFechaFinalIsNull(inmueble);

        if (!registrosActivos.isEmpty() && dto.getFechaFinal() == null) {
            throw new RuntimeException("Ya existe una tenencia activa para este inmueble");
        }

        RegistroTenencia registro = RegistroTenencia.builder()
                .idArrendatario(arrendatario)
                .idInmueble(inmueble)
                .fechaInicio(dto.getFechaInicio() != null ? dto.getFechaInicio() : LocalDate.now())
                .fechaFinal(dto.getFechaFinal())
                .build();

        RegistroTenencia guardado = registroTenenciaRepository.save(registro);
        return convertirADTO(guardado);
    }

    // Actualizar registro
    @Transactional
    public RegistroTenenciaDTO actualizar(Integer id, RegistroTenenciaUpdateDTO dto) {
        RegistroTenencia registro = registroTenenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de tenencia no encontrado con ID: " + id));

        if (dto.getFechaInicio() != null) {
            registro.setFechaInicio(dto.getFechaInicio());
        }

        if (dto.getFechaFinal() != null) {
            // Validar que fecha final sea después de fecha inicio
            if (dto.getFechaFinal().isBefore(registro.getFechaInicio())) {
                throw new RuntimeException("La fecha final no puede ser anterior a la fecha de inicio");
            }
            registro.setFechaFinal(dto.getFechaFinal());
        }

        RegistroTenencia actualizado = registroTenenciaRepository.save(registro);
        return convertirADTO(actualizado);
    }

    // Finalizar tenencia (establecer fecha final)
    @Transactional
    public RegistroTenenciaDTO finalizarTenencia(Integer id, LocalDate fechaFinal) {
        RegistroTenencia registro = registroTenenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de tenencia no encontrado con ID: " + id));

        if (registro.getFechaFinal() != null) {
            throw new RuntimeException("Esta tenencia ya está finalizada");
        }

        LocalDate fechaFin = fechaFinal != null ? fechaFinal : LocalDate.now();

        if (fechaFin.isBefore(registro.getFechaInicio())) {
            throw new RuntimeException("La fecha final no puede ser anterior a la fecha de inicio");
        }

        registro.setFechaFinal(fechaFin);
        RegistroTenencia actualizado = registroTenenciaRepository.save(registro);
        return convertirADTO(actualizado);
    }

    // Eliminar registro
    @Transactional
    public void eliminar(Integer id) {
        if (!registroTenenciaRepository.existsById(id)) {
            throw new RuntimeException("Registro de tenencia no encontrado con ID: " + id);
        }
        registroTenenciaRepository.deleteById(id);
    }

    // Obtener registros por rango de fechas
    public List<RegistroTenenciaDTO> obtenerPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return registroTenenciaRepository.findByFechaInicioBetween(inicio, fin).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
}