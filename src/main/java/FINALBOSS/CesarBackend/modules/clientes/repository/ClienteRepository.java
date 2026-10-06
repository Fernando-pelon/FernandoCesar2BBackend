package FINALBOSS.CesarBackend.modules.clientes.repository;

import FINALBOSS.CesarBackend.modules.clientes.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Busca clientes con un email (sin importar mayúsculas), para controlar que no se repita
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.email_cliente) = LOWER(:email)")
    List<Cliente> buscarPorEmail(@Param("email") String email);
}
