package ma.ensaj.GestionReservation.controllers;

import ma.ensaj.GestionReservation.dto.ReservationInput;
import ma.ensaj.GestionReservation.entities.Chambre;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Reservation;
import ma.ensaj.GestionReservation.repositories.ChambreRepository;
import ma.ensaj.GestionReservation.repositories.ClientRepository;
import ma.ensaj.GestionReservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


import lombok.AllArgsConstructor;


import java.util.List;

@Controller
@AllArgsConstructor
public class ReservationGraphQLController {
    @Autowired
    private  ClientRepository clientRepository;
    @Autowired

    private  ChambreRepository chambreRepository;
    @Autowired

    private  ReservationRepository reservationRepository;

    // Query to fetch all clients
    @QueryMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Query to fetch all chambres
    @QueryMapping
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    // Query to fetch all reservations
    @QueryMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Query to fetch a reservation by ID
    @QueryMapping
    public Reservation getReservationById(@Argument Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Reservation %s not found", id)));
    }

    // Mutation to add a new client
    @MutationMapping
    public Client addClient(@Argument(name = "client") Client clientInput) {
        Client client = new Client();
        client.setNom(clientInput.getNom());
        client.setPrenom(clientInput.getPrenom());
        client.setEmail(clientInput.getEmail());
        client.setTelephone(clientInput.getTelephone());
        return clientRepository.save(client);
    }

    // Mutation to add a new chambre
    @MutationMapping
    public Chambre addChambre(@Argument(name = "chambre") Chambre chambreInput) {
        Chambre chambre = new Chambre();
        chambre.setType(chambreInput.getType());
        chambre.setPrix(chambreInput.getPrix());
        chambre.setDisponible(chambreInput.getDisponible());
        return chambreRepository.save(chambre);
    }

    // Mutation to add a new reservation
    @MutationMapping
    public Reservation addReservation(@Argument(name = "reservation") Reservation reservationInput) {
        Client client = clientRepository.findById(reservationInput.getClientId())
                .orElseThrow(() -> new RuntimeException(String.format("Client %s not found", reservationInput.getClientId())));

        Chambre chambre = chambreRepository.findById(reservationInput.getChambre().getId())
                .orElseThrow(() -> new RuntimeException(String.format("Chambre %s not found", reservationInput.getChambre().getId())));

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(reservationInput.getDateDebut());
        reservation.setDateFin(reservationInput.getDateFin());
        reservation.setPreferences(reservationInput.getPreferences());

        return reservationRepository.save(reservation);
    }
}