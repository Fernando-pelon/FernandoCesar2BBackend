package FINALBOSS.CesarBackend.modules.eventos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @ToString
@Entity
@Table(name = "EVENTOS")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EVENTO")
    private Long id_evento;

    @Column(name = "ID_CLIENTE", nullable = false)
    private Long id_cliente_evento;

    @Column(name = "ID_SALON", nullable = false)
    private Long id_salon_evento;

    @Column(name = "NOMBRE_EVENTO", nullable = false, length = 100)
    private String nombre_evento;

    @Column(name = "FECHA_EVENTO", nullable = false)
    private LocalDate fecha_evento;

    @Column(name = "CANTIDAD_PERSONAS", nullable = false)
    private Integer cantidad_personas_evento;

    @Column(name = "CANTIDAD_HORAS")
    private Integer cantidad_horas_evento;

    @Column(name = "ESTADO", length = 20)
    private String estado_evento;

    @Column(name = "TOTAL_PAGO", precision = 8, scale = 2)
    private BigDecimal total_pago_evento;
}
