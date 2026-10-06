package FINALBOSS.CesarBackend.modules.clientes.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ClienteResponseDTO {

    private Long id_cliente;
    private String nombre_cliente;
    private String apellido_cliente;
    private String telefono_cliente;
    private String email_cliente;
    private String direccion_cliente;
}
