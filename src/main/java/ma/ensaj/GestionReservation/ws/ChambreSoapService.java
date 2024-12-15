package ma.ensaj.GestionReservation.ws;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import ma.ensaj.GestionReservation.entities.Chambre;
import ma.ensaj.GestionReservation.entities.Client;
import ma.ensaj.GestionReservation.entities.Reservation;
import ma.ensaj.GestionReservation.entities.TypeChambre;
import ma.ensaj.GestionReservation.repositories.ChambreRepository;
import ma.ensaj.GestionReservation.repositories.ClientRepository;
import ma.ensaj.GestionReservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
@Component
@WebService(serviceName = "ChambreWS")
public class ChambreSoapService {
    @Autowired
    private ChambreRepository chambreRepository;

    @WebMethod
    public Chambre creerChambre(
            @WebParam(name = "prix") Double prix,
            @WebParam(name = "type") TypeChambre type
    ) {
        Chambre chambre = new Chambre();
        chambre.setPrix(prix);
        chambre.setType(type);
        chambre.setDisponible(true);
        return chambreRepository.save(chambre);
    }

    @WebMethod
    public List<Chambre> listerChambresDisponibles() {
        return chambreRepository.findByDisponibleTrue();
    }

    @WebMethod
    public List<Chambre> listerChambreParType(@WebParam(name = "type") TypeChambre type) {
        return chambreRepository.findByType(TypeChambre.valueOf(type.name()));
    }
    @WebMethod
    public List<Chambre> listerChambres() {
        return chambreRepository.findAll();
    }
}