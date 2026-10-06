package FINALBOSS.CesarBackend.modules.eventos.repository;

import FINALBOSS.CesarBackend.modules.eventos.model.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

        // Cuenta cuántos eventos tiene un cliente (para no eliminar clientes con eventos)
        @Query("SELECT COUNT(e) FROM Evento e WHERE e.id_cliente_evento = :idCliente")
        long contarPorCliente(@Param("idCliente") Long idCliente);

        // Eventos activos (PENDIENTE o CONFIRMADO) de un salón en una fecha
        @Query("SELECT e FROM Evento e WHERE e.id_salon_evento = :idSalon AND e.fecha_evento = :fecha "
                + "AND e.estado_evento IN ('PENDIENTE', 'CONFIRMADO')")
        List<Evento> buscarActivosPorSalonYFecha(@Param("idSalon") Long idSalon, @Param("fecha") LocalDate fecha);
}
