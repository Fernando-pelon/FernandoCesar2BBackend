package FINALBOSS.CesarBackend.modules.salones.controller;

import FINALBOSS.CesarBackend.modules.salones.model.dto.SalonResponseDTO;
import FINALBOSS.CesarBackend.modules.salones.service.SalonService;
import FINALBOSS.CesarBackend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Los salones se registran directamente en la base de datos, aquí solo se consultan
@RestController
@RequestMapping("/api/salones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class SalonController {

    private final SalonService salonService;

    // GET - Listar salones
    @GetMapping
    public ResponseEntity<ApiResponse<List<SalonResponseDTO>>> listarSalones() {
        log.info("Solicitud para listar salones");

        List<SalonResponseDTO> salones = salonService.listarSalones();
        return ResponseEntity.ok(new ApiResponse<>(200, "Salones obtenidos correctamente", salones));
    }

    // GET - Buscar salón por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalonResponseDTO>> buscarSalon(@PathVariable Long id) {
        log.info("Solicitud para buscar salón con ID: {}", id);

        SalonResponseDTO salon = salonService.buscarSalon(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Salón encontrado", salon));
    }
}
