package ma.ensaj.GestionReservation.controllers;


import ma.ensaj.GestionReservation.entities.Chambre;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Reservation;
import ma.ensaj.GestionReservation.repositories.ChambreRepository;
import ma.ensaj.GestionReservation.repositories.ClientRepository;
import ma.ensaj.GestionReservation.repositories.ReservationRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ReservationGraphQLController {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ChambreRepository chambreRepository;

    public ReservationGraphQLController(
            ReservationRepository reservationRepository,
            ClientRepository clientRepository,
            ChambreRepository chambreRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.chambreRepository = chambreRepository;
    }

    @QueryMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @QueryMapping
    public Reservation getReservationById(@Argument Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation non trouvée"));
    }

    @QueryMapping
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    @QueryMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @MutationMapping
    public Client createClient(@Argument(name = "input") Client client) {
        return clientRepository.save(client);
    }

    @MutationMapping
    public Chambre createChambre(@Argument(name = "input") Chambre chambre) {
        return chambreRepository.save(chambre);
    }

//    @MutationMapping
//    public Reservation createReservation(@Argument(name = "input") Reservation reservation) {
//        // Vérifier que le client et la chambre existent
//        Client client = clientRepository.findById(reservation.getClient().getId())
//                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
//        Chambre chambre = chambreRepository.findById(reservation.getChambre().getId())
//                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
//
//        // Vérifier la disponibilité de la chambre
//        if (!chambre.getDisponible()) {
//            throw new RuntimeException("La chambre n'est pas disponible");
//        }
//
//        // Marquer la chambre comme indisponible
//        chambre.setDisponible(false);
//        chambreRepository.save(chambre);
//
//        // Sauvegarder la réservation
//        reservation.setClient(client);
//        reservation.setChambre(chambre);
//        return reservationRepository.save(reservation);
//    }

    @MutationMapping
    public Reservation updateReservation(@Argument Long id, @Argument(name = "input") Reservation reservationInput) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation non trouvée"));

        // Mettre à jour les champs de la réservation
        existingReservation.setDateDebut(reservationInput.getDateDebut());
        existingReservation.setDateFin(reservationInput.getDateFin());
        existingReservation.setPreferences(reservationInput.getPreferences());

        // Vérifier et mettre à jour le client si nécessaire
        if (reservationInput.getClient() != null) {
            Client client = clientRepository.findById(reservationInput.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
            existingReservation.setClient(client);
        }

        // Vérifier et mettre à jour la chambre si nécessaire
        if (reservationInput.getChambre() != null) {
            Chambre chambre = chambreRepository.findById(reservationInput.getChambre().getId())
                    .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));

            // Libérer l'ancienne chambre
            if (existingReservation.getChambre() != null) {
                Chambre oldChambre = existingReservation.getChambre();
                oldChambre.setDisponible(true);
                chambreRepository.save(oldChambre);
            }

            // Réserver la nouvelle chambre
            chambre.setDisponible(false);
            chambreRepository.save(chambre);
            existingReservation.setChambre(chambre);
        }

        return reservationRepository.save(existingReservation);
    }

    @MutationMapping
    public boolean deleteReservation(@Argument Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation non trouvée"));

        // Libérer la chambre
        Chambre chambre = reservation.getChambre();
        if (chambre != null) {
            chambre.setDisponible(true);
            chambreRepository.save(chambre);
        }

        reservationRepository.delete(reservation);
        return true;
    }


    @MutationMapping
    public Reservation createReservation(@Argument(name = "input") ReservationInput input) {
        // Rechercher le client
        Client client = clientRepository.findById(input.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // Rechercher la chambre
        Chambre chambre = chambreRepository.findById(input.getChambreId())
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));

        // Vérifier la disponibilité de la chambre
        if (!chambre.getDisponible()) {
            throw new RuntimeException("La chambre n'est pas disponible");
        }

        // Créer la nouvelle réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(input.getDateDebut());
        reservation.setDateFin(input.getDateFin());
        reservation.setPreferences(input.getPreferences());

        // Marquer la chambre comme indisponible
        chambre.setDisponible(false);
        chambreRepository.save(chambre);

        // Sauvegarder la réservation
        return reservationRepository.save(reservation);
    }

    // Classe interne pour l'input de réservation
    public static class ReservationInput {
        private Long clientId;
        private Long chambreId;
        private String dateDebut;
        private String dateFin;
        private String preferences;

        // Getters et setters
        public Long getClientId() { return clientId; }
        public void setClientId(Long clientId) { this.clientId = clientId; }

        public Long getChambreId() { return chambreId; }
        public void setChambreId(Long chambreId) { this.chambreId = chambreId; }

        public String getDateDebut() { return dateDebut; }
        public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }

        public String getDateFin() { return dateFin; }
        public void setDateFin(String dateFin) { this.dateFin = dateFin; }

        public String getPreferences() { return preferences; }
        public void setPreferences(String preferences) { this.preferences = preferences; }
    }
}