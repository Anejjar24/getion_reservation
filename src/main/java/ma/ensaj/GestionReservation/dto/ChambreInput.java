package ma.ensaj.GestionReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChambreInput {
    private Long id;
    private String type;
    private Float prix;
    private Boolean disponible;
}