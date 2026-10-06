package FINALBOSS.CesarBackend.modules.eventos.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

// El total a pagar NO viene aquí: lo calcula el backend (horas x precio de renta del salón)
@Getter @Setter @ToString
public class EventoRequestDTO {

    @NotNull(message = "Debe seleccionar un cliente")
    private Long id_cliente_evento;

    @NotNull(message = "Debe seleccionar un salón")
    private Long id_salon_evento;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(max = 100, message = "El nombre del evento no puede tener más de 100 caracteres")
    private String nombre_evento;

    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDate fecha_evento;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "La cantidad de personas debe ser mayor a 0")
    private Integer cantidad_personas_evento;

    @NotNull(message = "La cantidad de horas es obligatoria")
    @Min(value = 1, message = "La cantidad de horas debe ser mayor a 0")
    private Integer cantidad_horas_evento;

    // Solo se usa al editar. Un evento nuevo siempre se crea con el estado inicial
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    private String estado_evento;
}
