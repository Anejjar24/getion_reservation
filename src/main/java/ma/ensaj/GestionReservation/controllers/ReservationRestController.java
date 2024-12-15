package ma.ensaj.GestionReservation.controllers;

import lombok.RequiredArgsConstructor;
import ma.ensaj.GestionReservation.entities.Chambre;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Reservation;
import ma.ensaj.GestionReservation.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api-rest/reservations")
@RequiredArgsConstructor
public class ReservationRestController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/")
    public ResponseEntity<Reservation> creerReservation(@RequestBody Reservation reservationRequest) {
        Reservation createdReservation = reservationService.createReservation(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> consulterReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> modifierReservation(@PathVariable Long id, @RequestBody Reservation updatedReservationRequest) {
        Reservation updatedReservation = reservationService.updateReservation(id, updatedReservationRequest);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    // Route modifiée pour éviter le conflit avec `/{id}`
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> listerReservationsClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(reservationService.getReservationsByClient(clientId));
    }

    @GetMapping("/")
    public ResponseEntity<List<Reservation>> listerToutesReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }
}

