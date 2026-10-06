package FINALBOSS.CesarBackend.modules.clientes.controller;

import FINALBOSS.CesarBackend.modules.clientes.model.dto.ClienteRequestDTO;
import FINALBOSS.CesarBackend.modules.clientes.model.dto.ClienteResponseDTO;
import FINALBOSS.CesarBackend.modules.clientes.service.ClienteService;
import FINALBOSS.CesarBackend.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    // POST - Guardar cliente
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> guardarCliente(
            @Valid @RequestBody ClienteRequestDTO dto) {
        log.info("Solicitud para guardar cliente");

        ClienteResponseDTO clienteGuardado = clienteService.guardarCliente(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Cliente registrado correctamente", clienteGuardado));
    }

    // GET - Listar clientes
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponseDTO>>> listarClientes() {
        log.info("Solicitud para listar clientes");

        List<ClienteResponseDTO> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(new ApiResponse<>(200, "Clientes obtenidos correctamente", clientes));
    }

    // GET - Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> buscarCliente(@PathVariable Long id) {
        log.info("Solicitud para buscar cliente con ID: {}", id);

        ClienteResponseDTO cliente = clienteService.buscarCliente(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cliente encontrado", cliente));
    }

    // PUT - Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        log.info("Solicitud para actualizar cliente con ID: {}", id);

        ClienteResponseDTO clienteActualizado = clienteService.actualizarCliente(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cliente actualizado correctamente", clienteActualizado));
    }

    // DELETE - Eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Long id) {
        log.info("Solicitud para eliminar cliente con ID: {}", id);

        clienteService.eliminarCliente(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cliente eliminado correctamente"));
    }
}
