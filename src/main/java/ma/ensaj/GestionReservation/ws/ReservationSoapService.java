package ma.ensaj.GestionReservation.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import ma.ensaj.GestionReservation.entities.Chambre;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Reservation;
import ma.ensaj.GestionReservation.repositories.ChambreRepository;
import ma.ensaj.GestionReservation.repositories.ClientRepository;
import ma.ensaj.GestionReservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.List;

@Component
@WebService(serviceName = "ReservationWS")
public class ReservationSoapService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ChambreRepository chambreRepository;



    @WebMethod
    public Reservation creerReservation(
            @WebParam(name = "clientId") Long clientId,
            @WebParam(name = "chambreId") Long chambreId,
            @WebParam(name = "dateDebut") String dateDebutStr,
            @WebParam(name = "dateFin") String dateFinStr,
            @WebParam(name = "preferences") String preferences) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Chambre chambre = chambreRepository.findById(chambreId)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));

        if (!chambre.getDisponible()) {
            throw new RuntimeException("La chambre n'est pas disponible");
        }

        // Convertir les chaînes de caractères en objets Date
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String dateDebut;
//        String dateFin;
//        try {
//            dateDebut = dateFormat.parse(dateDebutStr);
//            dateFin = dateFormat.parse(dateFinStr);
//        } catch (ParseException e) {
//            throw new RuntimeException("Format de date invalide, veuillez utiliser 'yyyy-MM-dd'", e);
//        }

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(dateDebutStr);
        reservation.setDateFin(dateFinStr);
        reservation.setPreferences(preferences);

        // Mettre à jour la disponibilité de la chambre
        chambre.setDisponible(false);
        chambreRepository.save(chambre);

        return reservationRepository.save(reservation);
    }



    @WebMethod
    public Reservation consulterReservation(@WebParam(name = "id") Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
    }

    @WebMethod
    public Reservation modifierReservation(
            @WebParam(name = "id") Long id,
            @WebParam(name = "dateDebut") String dateDebutStr,
            @WebParam(name = "dateFin") String dateFinStr,
            @WebParam(name = "preferences") String preferences) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        // Convertir les chaînes de caractères en objets Date
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date dateDebut;
//        Date dateFin;
//        try {
//            dateDebut = dateFormat.parse(dateDebutStr);
//            dateFin = dateFormat.parse(dateFinStr);
//        } catch (ParseException e) {
//            throw new RuntimeException("Format de date invalide, veuillez utiliser 'yyyy-MM-dd'", e);
//        }
//
//        // Validation des dates (optionnelle)
//        if (dateDebut.after(dateFin)) {
//            throw new RuntimeException("La date de début doit être antérieure à la date de fin");
//        }

        // Mise à jour des propriétés de la réservation
        reservation.setDateDebut(dateDebutStr);
        reservation.setDateFin(dateFinStr);
        reservation.setPreferences(preferences);

        return reservationRepository.save(reservation);
    }
    @WebMethod
    public boolean supprimerReservation(@WebParam(name = "id") Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        // Remettre la chambre comme disponible
        Chambre chambre = reservation.getChambre();
        chambre.setDisponible(true);
        chambreRepository.save(chambre);

        reservationRepository.delete(reservation);
        return true;
    }

    @WebMethod
    public List<Reservation> listerReservationsClient(@WebParam(name = "clientId") Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        return reservationRepository.findByClient(client);
    }
}