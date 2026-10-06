package FINALBOSS.CesarBackend.modules.eventos.controller;

import FINALBOSS.CesarBackend.modules.eventos.model.dto.EventoRequestDTO;
import FINALBOSS.CesarBackend.modules.eventos.model.dto.EventoResponseDTO;
import FINALBOSS.CesarBackend.modules.eventos.service.EventoService;
import FINALBOSS.CesarBackend.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class EventoController {

    private final EventoService eventoService;

    // POST - Guardar evento
    @PostMapping
    public ResponseEntity<ApiResponse<EventoResponseDTO>> guardarEvento(
            @Valid @RequestBody EventoRequestDTO dto) {
        log.info("Solicitud para guardar evento");

        EventoResponseDTO eventoGuardado = eventoService.guardarEvento(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Evento registrado correctamente", eventoGuardado));
    }

    // GET - Listar eventos
    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoResponseDTO>>> listarEventos() {
        log.info("Solicitud para listar eventos");

        List<EventoResponseDTO> eventos = eventoService.listarEventos();
        return ResponseEntity.ok(new ApiResponse<>(200, "Eventos obtenidos correctamente", eventos));
    }

    // GET - Buscar evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> buscarEvento(@PathVariable Long id) {
        log.info("Solicitud para buscar evento con ID: {}", id);

        EventoResponseDTO evento = eventoService.buscarEvento(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Evento encontrado", evento));
    }

    // PUT - Actualizar evento
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> actualizarEvento(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequestDTO dto) {
        log.info("Solicitud para actualizar evento con ID: {}", id);

        EventoResponseDTO eventoActualizado = eventoService.actualizarEvento(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Evento actualizado correctamente", eventoActualizado));
    }

    // DELETE - Eliminar evento
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEvento(@PathVariable Long id) {
        log.info("Solicitud para eliminar evento con ID: {}", id);

        eventoService.eliminarEvento(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Evento eliminado correctamente"));
    }
}
