package FINALBOSS.CesarBackend.modules.salones.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

// Los salones se registran directamente en la base de datos (SQL Developer),
// por eso la API solo los consulta. Este DTO queda listo por si se necesita registrar salones.
@Getter @Setter @ToString
public class SalonRequestDTO {

    @NotBlank(message = "El nombre del salón es obligatorio")
    @Size(max = 100, message = "El nombre del salón no puede tener más de 100 caracteres")
    private String nombre_salon;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser un número entero mayor a 0")
    private Integer capacidad_salon;

    @NotNull(message = "El precio de renta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de renta debe ser mayor a 0")
    private BigDecimal precio_renta_salon;

    @Size(max = 100, message = "La ubicación no puede tener más de 100 caracteres")
    private String ubicacion_salon;
}
