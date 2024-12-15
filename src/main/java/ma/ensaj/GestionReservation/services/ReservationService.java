package ma.ensaj.GestionReservation.services;

import lombok.RequiredArgsConstructor;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Reservation;
import ma.ensaj.GestionReservation.repositories.ChambreRepository;
import ma.ensaj.GestionReservation.repositories.ClientRepository;
import ma.ensaj.GestionReservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    @Autowired

    private  ReservationRepository reservationRepository;
    @Autowired

    private  ClientRepository clientRepository;
    @Autowired

    private  ChambreRepository chambreRepository;

    public Reservation createReservation(Reservation reservation) {
        if (!clientRepository.existsById(reservation.getClient().getId())) {
            throw new IllegalArgumentException("Client not found");
        }
        if (!chambreRepository.existsById(reservation.getChambre().getId())) {
            throw new IllegalArgumentException("Chambre not found");
        }

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        existingReservation.setClient(updatedReservation.getClient());
        existingReservation.setChambre(updatedReservation.getChambre());
        existingReservation.setDateDebut(updatedReservation.getDateDebut());
        existingReservation.setDateFin(updatedReservation.getDateFin());
        existingReservation.setPreferences(updatedReservation.getPreferences());

        return reservationRepository.save(existingReservation);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation not found");
        }
        reservationRepository.deleteById(id);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    public List<Reservation> getReservationsByClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        return reservationRepository.findByClient(client);
    }

    public List<Reservation> getReservationsByDateRange(String startDate, String endDate) {
        return reservationRepository.findByDateDebutBetween(startDate, endDate);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
