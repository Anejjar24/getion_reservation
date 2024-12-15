package ma.ensaj.GestionReservation.repositories;
import ma.ensaj.GestionReservation.entities.Chambre;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    List<Chambre> findByDisponibleTrue();
    List<Chambre> findByType(TypeChambre type);
    List<Chambre> findByDisponibleAndType(Boolean disponible, TypeChambre type);

}