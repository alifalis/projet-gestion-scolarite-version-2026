
package modele;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "inscriptions_cours",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_inscription_cours",
            columnNames = {"id_etudiant", "id_cours"}
        )
    }
)
public class InscriptionCours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscription", nullable = false)
    private int idInscription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cours", nullable = false)
    private Cours cours;

    @Column(name = "debut_cours", nullable = false)
    private LocalDateTime debutCours;

    @Column(name = "fin_cours", nullable = false)
    private LocalDateTime finCours;

    // Constructeur par défaut requis par JPA
    public InscriptionCours() {
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

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public LocalDateTime getDebutCours() {
        return debutCours;
    }

    public void setDebutCours(LocalDateTime debutCours) {
        this.debutCours = debutCours;
    }

    public LocalDateTime getFinCours() {
        return finCours;
    }

    public void setFinCours(LocalDateTime finCours) {
        this.finCours = finCours;
    }
}
