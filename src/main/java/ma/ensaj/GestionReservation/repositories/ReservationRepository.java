package ma.ensaj.GestionReservation.repositories;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClient(Client client);
    List<Reservation> findByDateDebutBetween(String start, String end);
}