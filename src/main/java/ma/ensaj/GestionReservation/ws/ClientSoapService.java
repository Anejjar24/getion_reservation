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

import java.time.LocalDate;
import java.util.List;

@Component
@WebService(serviceName = "ClientWS")
public class ClientSoapService {
    @Autowired
    private ClientRepository clientRepository;

    @WebMethod
    public Client creerClient(
            @WebParam(name = "nom") String nom,
            @WebParam(name = "prenom") String prenom,
            @WebParam(name = "email") String email,
            @WebParam(name = "telephone") String telephone
    ) {
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setTelephone(telephone);
        return clientRepository.save(client);
    }

    @WebMethod
    public Client rechercherClientParEmail(@WebParam(name = "email") String email) {
        return clientRepository.findByEmail(email);
    }

    @WebMethod
    public List<Client> listerClients() {
        return clientRepository.findAll();
    }
}