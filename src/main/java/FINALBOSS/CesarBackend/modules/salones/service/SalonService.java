package FINALBOSS.CesarBackend.modules.salones.service;

import FINALBOSS.CesarBackend.modules.salones.model.dto.SalonResponseDTO;
import FINALBOSS.CesarBackend.modules.salones.model.entity.Salon;
import FINALBOSS.CesarBackend.modules.salones.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalonService {

    private final SalonRepository salonRepository;

    // GET - Listar todos
    public List<SalonResponseDTO> listarSalones() {
        log.info("Obteniendo todos los salones");

        return salonRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // GET - Buscar por ID
    public SalonResponseDTO buscarSalon(Long id) {
        log.info("Buscando salón con ID: {}", id);

        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Salón no encontrado con ID: " + id));
        return convertirADTO(salon);
    }

    // Entity -> DTO
    private SalonResponseDTO convertirADTO(Salon salon) {
        SalonResponseDTO dto = new SalonResponseDTO();
        dto.setId_salon(salon.getId_salon());
        dto.setNombre_salon(salon.getNombre_salon());
        dto.setCapacidad_salon(salon.getCapacidad_salon());
        dto.setPrecio_renta_salon(salon.getPrecio_renta_salon());
        dto.setUbicacion_salon(salon.getUbicacion_salon());
        return dto;
    }
}
