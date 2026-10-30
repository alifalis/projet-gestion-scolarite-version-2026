
package modele;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(
    name = "resultats",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_etudiant_cours",
            columnNames = {"id_etudiant", "id_cours"}
        )
    }
)
public class Resultat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultat")
    private int idResultat;

    // Plusieurs résultats peuvent appartenir au même étudiant
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Etudiant etudiant;

    // Plusieurs résultats peuvent concerner le même cours
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cours", nullable = false)
    private Cours cours;

    // Une note peut être absente (NULL)
    @Column(name = "note", precision = 4, scale = 2)
    private BigDecimal note;

    // Constructeur vide obligatoire pour JPA
    public Resultat() {
    }

    public Resultat(Etudiant etudiant, Cours cours, BigDecimal note) {
        this.etudiant = etudiant;
        this.cours = cours;
        this.note = note;
    }

    public int getIdResultat() {
        return idResultat;
    }

    public void setIdResultat(int idResultat) {
        this.idResultat = idResultat;
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

    public BigDecimal getNote() {
        return note;
    }

    public void setNote(BigDecimal note) {
        this.note = note;
    }
}
