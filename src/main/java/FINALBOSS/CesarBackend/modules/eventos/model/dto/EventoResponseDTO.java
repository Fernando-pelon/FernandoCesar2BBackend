package FINALBOSS.CesarBackend.modules.eventos.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @ToString
public class EventoResponseDTO {

    private Long id_evento;
    private Long id_cliente_evento;
    private Long id_salon_evento;
    private String nombre_evento;
    private LocalDate fecha_evento;
    private Integer cantidad_personas_evento;
    private Integer cantidad_horas_evento;
    private String estado_evento;
    private BigDecimal total_pago_evento;
}
