package FINALBOSS.CesarBackend.modules.clientes.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "CLIENTES")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    private Long id_cliente;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre_cliente;

    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido_cliente;

    @Column(name = "TELEFONO", nullable = false, length = 15)
    private String telefono_cliente;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email_cliente;

    @Column(name = "DIRECCION", length = 200)
    private String direccion_cliente;
}
