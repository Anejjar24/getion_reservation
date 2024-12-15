package ma.ensaj.GestionReservation.dto;

import ma.ensaj.GestionReservation.entities.TypeChambre;

public class ChambreInput {
    private Double prix;
    private Boolean disponible;
    private TypeChambre type;

    // Getters et setters
    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public TypeChambre getType() {
        return type;
    }

    public void setType(TypeChambre type) {
        this.type = type;
    }
}
