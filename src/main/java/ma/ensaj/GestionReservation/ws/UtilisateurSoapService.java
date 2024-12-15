package ma.ensaj.GestionReservation.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import ma.ensaj.GestionReservation.entities.Chambre;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Reservation;
import ma.ensaj.GestionReservation.entities.Utilisateur;
import ma.ensaj.GestionReservation.repositories.ChambreRepository;
import ma.ensaj.GestionReservation.repositories.ClientRepository;
import ma.ensaj.GestionReservation.repositories.ReservationRepository;
import ma.ensaj.GestionReservation.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
@Component
@WebService(serviceName = "UtilisateurWS")
public class UtilisateurSoapService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @WebMethod
    public Utilisateur creerUtilisateur(
            @WebParam(name = "nomUtilisateur") String nomUtilisateur,
            @WebParam(name = "motDePasse") String motDePasse,
            @WebParam(name = "role") String role
    ) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur(nomUtilisateur);
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setRole(role);
        return utilisateurRepository.save(utilisateur);
    }

    @WebMethod
    public Utilisateur authentifier(
            @WebParam(name = "nomUtilisateur") String nomUtilisateur,
            @WebParam(name = "motDePasse") String motDePasse
    ) {
        Utilisateur utilisateur = utilisateurRepository.findByNomUtilisateur(nomUtilisateur);
        if (utilisateur != null && utilisateur.getMotDePasse().equals(motDePasse)) {
            return utilisateur;
        }
        throw new RuntimeException("Authentification échouée");
    }
}