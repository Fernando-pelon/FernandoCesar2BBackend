package FINALBOSS.CesarBackend.modules.salones.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter @Setter @ToString
public class SalonResponseDTO {

    private Long id_salon;
    private String nombre_salon;
    private Integer capacidad_salon;
    private BigDecimal precio_renta_salon;
    private String ubicacion_salon;
}
