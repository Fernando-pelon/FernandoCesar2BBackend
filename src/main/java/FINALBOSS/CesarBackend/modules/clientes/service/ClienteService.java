package FINALBOSS.CesarBackend.modules.clientes.service;

import FINALBOSS.CesarBackend.modules.clientes.model.dto.ClienteRequestDTO;
import FINALBOSS.CesarBackend.modules.clientes.model.dto.ClienteResponseDTO;
import FINALBOSS.CesarBackend.modules.clientes.model.entity.Cliente;
import FINALBOSS.CesarBackend.modules.clientes.repository.ClienteRepository;
import FINALBOSS.CesarBackend.modules.eventos.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EventoRepository eventoRepository;

    // POST - Guardar
    public ClienteResponseDTO guardarCliente(ClienteRequestDTO dto) {
        log.info("Guardando cliente: {}", dto.getNombre_cliente());

        validarEmailUnico(dto.getEmail_cliente(), null);

        Cliente cliente = convertirAEntity(dto);
        Cliente clienteGuardado = clienteRepository.save(cliente);

        log.info("Cliente guardado correctamente con ID: {}", clienteGuardado.getId_cliente());
        return convertirADTO(clienteGuardado);
    }

    // GET - Listar todos
    public List<ClienteResponseDTO> listarClientes() {
        log.info("Obteniendo todos los clientes");

        return clienteRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // GET - Buscar por ID
    public ClienteResponseDTO buscarCliente(Long id) {
        log.info("Buscando cliente con ID: {}", id);

        Cliente cliente = obtenerCliente(id);
        return convertirADTO(cliente);
    }

    // PUT - Actualizar
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO dto) {
        log.info("Actualizando cliente con ID: {}", id);

        Cliente cliente = obtenerCliente(id);
        validarEmailUnico(dto.getEmail_cliente(), id);

        cliente.setNombre_cliente(dto.getNombre_cliente().trim());
        cliente.setApellido_cliente(dto.getApellido_cliente().trim());
        cliente.setTelefono_cliente(dto.getTelefono_cliente().trim());
        cliente.setEmail_cliente(dto.getEmail_cliente().trim());
        cliente.setDireccion_cliente(obtenerDireccion(dto.getDireccion_cliente()));

        Cliente clienteActualizado = clienteRepository.save(cliente);
        log.info("Cliente actualizado correctamente");
        return convertirADTO(clienteActualizado);
    }

    // DELETE - Eliminar
    public void eliminarCliente(Long id) {
        log.info("Eliminando cliente con ID: {}", id);

        obtenerCliente(id);

        // No se puede eliminar un cliente que ya tiene eventos registrados (llave foránea)
        if (eventoRepository.contarPorCliente(id) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede eliminar el cliente porque tiene eventos registrados");
        }

        clienteRepository.deleteById(id);
        log.info("Cliente eliminado");
    }

    // Busca el cliente o lanza error 404
    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + id));
    }

    // El email no se puede repetir. Al actualizar se ignora el mismo cliente
    private void validarEmailUnico(String email, Long idActual) {
        List<Cliente> existentes = clienteRepository.buscarPorEmail(email.trim());

        for (Cliente existente : existentes) {
            if (idActual == null || !existente.getId_cliente().equals(idActual)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ya existe un cliente registrado con ese email");
            }
        }
    }

    // Si no escriben dirección se guarda "Sin Especificar"
    private String obtenerDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            return "Sin Especificar";
        }
        return direccion.trim();
    }

    // DTO -> Entity
    private Cliente convertirAEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre_cliente(dto.getNombre_cliente().trim());
        cliente.setApellido_cliente(dto.getApellido_cliente().trim());
        cliente.setTelefono_cliente(dto.getTelefono_cliente().trim());
        cliente.setEmail_cliente(dto.getEmail_cliente().trim());
        cliente.setDireccion_cliente(obtenerDireccion(dto.getDireccion_cliente()));
        return cliente;
    }

    // Entity -> DTO
    private ClienteResponseDTO convertirADTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId_cliente(cliente.getId_cliente());
        dto.setNombre_cliente(cliente.getNombre_cliente());
        dto.setApellido_cliente(cliente.getApellido_cliente());
        dto.setTelefono_cliente(cliente.getTelefono_cliente());
        dto.setEmail_cliente(cliente.getEmail_cliente());
        dto.setDireccion_cliente(cliente.getDireccion_cliente());
        return dto;
    }
}
