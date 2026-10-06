package FINALBOSS.CesarBackend.modules.eventos.service;

import FINALBOSS.CesarBackend.modules.clientes.repository.ClienteRepository;
import FINALBOSS.CesarBackend.modules.eventos.model.dto.EventoRequestDTO;
import FINALBOSS.CesarBackend.modules.eventos.model.dto.EventoResponseDTO;
import FINALBOSS.CesarBackend.modules.eventos.model.entity.Evento;
import FINALBOSS.CesarBackend.modules.eventos.repository.EventoRepository;
import FINALBOSS.CesarBackend.modules.salones.model.entity.Salon;
import FINALBOSS.CesarBackend.modules.salones.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventoService {

    private static final List<String> ESTADOS_VALIDOS =
            List.of("PENDIENTE", "CONFIRMADO", "CANCELADO", "FINALIZADO");
    private static final String ESTADO_INICIAL = "CONFIRMADO";
    private static final BigDecimal TOTAL_MAXIMO = new BigDecimal("999999.99");

    private final EventoRepository eventoRepository;
    private final ClienteRepository clienteRepository;
    private final SalonRepository salonRepository;

    // POST - Guardar
    public EventoResponseDTO guardarEvento(EventoRequestDTO dto) {
        log.info("Guardando evento: {}", dto.getNombre_evento());

        Salon salon = validarDatos(dto, null);

        Evento evento = new Evento();
        copiarDatos(evento, dto);
        // Un evento nuevo siempre empieza con el estado inicial
        evento.setEstado_evento(ESTADO_INICIAL);
        evento.setTotal_pago_evento(calcularTotal(salon, dto.getCantidad_horas_evento()));

        Evento eventoGuardado = eventoRepository.save(evento);
        log.info("Evento guardado correctamente con ID: {}", eventoGuardado.getId_evento());
        return convertirADTO(eventoGuardado);
    }

    // GET - Listar todos
    public List<EventoResponseDTO> listarEventos() {
        log.info("Obteniendo todos los eventos");

        return eventoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // GET - Buscar por ID
    public EventoResponseDTO buscarEvento(Long id) {
        log.info("Buscando evento con ID: {}", id);

        return convertirADTO(obtenerEvento(id));
    }

    // PUT - Actualizar
    public EventoResponseDTO actualizarEvento(Long id, EventoRequestDTO dto) {
        log.info("Actualizando evento con ID: {}", id);

        Evento evento = obtenerEvento(id);
        Salon salon = validarDatos(dto, id);

        copiarDatos(evento, dto);

        // Si no mandan estado se queda el que ya tenía
        if (dto.getEstado_evento() != null && !dto.getEstado_evento().trim().isEmpty()) {
            String estado = dto.getEstado_evento().trim().toUpperCase();
            if (!ESTADOS_VALIDOS.contains(estado)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estado no válido. Los estados permitidos son: " + ESTADOS_VALIDOS);
            }
            evento.setEstado_evento(estado);
        }

        evento.setTotal_pago_evento(calcularTotal(salon, dto.getCantidad_horas_evento()));

        Evento eventoActualizado = eventoRepository.save(evento);
        log.info("Evento actualizado correctamente");
        return convertirADTO(eventoActualizado);
    }

    // DELETE - Eliminar
    public void eliminarEvento(Long id) {
        log.info("Eliminando evento con ID: {}", id);

        obtenerEvento(id);
        eventoRepository.deleteById(id);
        log.info("Evento eliminado");
    }

    // Busca el evento o lanza error 404
    private Evento obtenerEvento(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Evento no encontrado con ID: " + id));
    }

    // Valida cliente, salón, capacidad y que el salón esté libre. Devuelve el salón
    private Salon validarDatos(EventoRequestDTO dto, Long idActual) {
        if (!clienteRepository.existsById(dto.getId_cliente_evento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El cliente seleccionado no existe");
        }

        Salon salon = salonRepository.findById(dto.getId_salon_evento())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El salón seleccionado no existe"));

        if (dto.getCantidad_personas_evento() > salon.getCapacidad_salon()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La cantidad de personas (" + dto.getCantidad_personas_evento()
                            + ") supera la capacidad del salón (" + salon.getCapacidad_salon() + ")");
        }

        // No puede haber dos eventos activos en el mismo salón y fecha
        List<Evento> ocupados = eventoRepository.buscarActivosPorSalonYFecha(
                dto.getId_salon_evento(), dto.getFecha_evento());
        for (Evento ocupado : ocupados) {
            if (idActual == null || !ocupado.getId_evento().equals(idActual)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El salón ya tiene un evento registrado en esa fecha");
            }
        }

        return salon;
    }

    // Total = horas x precio de renta del salón
    private BigDecimal calcularTotal(Salon salon, Integer horas) {
        BigDecimal total = salon.getPrecio_renta_salon()
                .multiply(BigDecimal.valueOf(horas))
                .setScale(2, RoundingMode.HALF_UP);

        if (total.compareTo(TOTAL_MAXIMO) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El total a pagar es demasiado grande, reduce la cantidad de horas");
        }
        return total;
    }

    // Copia los datos del DTO al evento
    private void copiarDatos(Evento evento, EventoRequestDTO dto) {
        evento.setId_cliente_evento(dto.getId_cliente_evento());
        evento.setId_salon_evento(dto.getId_salon_evento());
        evento.setNombre_evento(dto.getNombre_evento().trim());
        evento.setFecha_evento(dto.getFecha_evento());
        evento.setCantidad_personas_evento(dto.getCantidad_personas_evento());
        evento.setCantidad_horas_evento(dto.getCantidad_horas_evento());
    }

    // Entity -> DTO
    private EventoResponseDTO convertirADTO(Evento evento) {
        EventoResponseDTO dto = new EventoResponseDTO();
        dto.setId_evento(evento.getId_evento());
        dto.setId_cliente_evento(evento.getId_cliente_evento());
        dto.setId_salon_evento(evento.getId_salon_evento());
        dto.setNombre_evento(evento.getNombre_evento());
        dto.setFecha_evento(evento.getFecha_evento());
        dto.setCantidad_personas_evento(evento.getCantidad_personas_evento());
        dto.setCantidad_horas_evento(evento.getCantidad_horas_evento());
        dto.setEstado_evento(evento.getEstado_evento());
        dto.setTotal_pago_evento(evento.getTotal_pago_evento());
        return dto;
    }
}
