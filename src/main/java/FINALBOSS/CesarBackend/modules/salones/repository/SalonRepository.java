package FINALBOSS.CesarBackend.modules.salones.repository;

import FINALBOSS.CesarBackend.modules.salones.model.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonRepository extends JpaRepository<Salon, Long> {
}
