
package modele;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "inscriptions_annee",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_inscription_annee",
            columnNames = {"id_etudiant", "annee_scolaire"}
        )
    }
)
public class InscriptionAnnee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscription", nullable = false)
    private int idInscription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Etudiant etudiant;

    @Column(name = "annee_scolaire", nullable = false, length = 9)
    private String anneeScolaire;

    @Column(
        name = "date_inscription",
        nullable = false,
        updatable = false
    )
    private LocalDateTime dateInscription;

    // Constructeur par défaut requis par JPA
    public InscriptionAnnee() {
    }

    // Constructeur avec les informations principales
    public InscriptionAnnee(Etudiant etudiant, String anneeScolaire) {
        this.etudiant = etudiant;
        this.anneeScolaire = anneeScolaire;
    }

    // Getters et Setters
    public int getIdInscription() {
        return idInscription;
    }

    public void setIdInscription(int idInscription) {
        this.idInscription = idInscription;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    @Override
    public String toString() {
        return "InscriptionAnnee{" +
                "idInscription=" + idInscription +
                ", idEtudiant=" +
                (etudiant != null ? etudiant.getIdEtudiant() : null) +
                ", anneeScolaire='" + anneeScolaire + '\'' +
                ", dateInscription=" + dateInscription +
                '}';
    }
}
