package FINALBOSS.CesarBackend.modules.salones.model.entity;

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

@Getter @Setter @ToString
@Entity
@Table(name = "SALONES")
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SALON")
    private Long id_salon;

    @Column(name = "NOMBRE_SALON", nullable = false, length = 100)
    private String nombre_salon;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad_salon;

    @Column(name = "PRECIO_RENTA", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_renta_salon;

    @Column(name = "UBICACION", length = 100)
    private String ubicacion_salon;
}
