
package modele;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cours")
public class Cours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cours", nullable = false)
    private int idCours;

    @Column(name = "nom_cours", nullable = false, length = 100)
    private String nomCours;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_enseignant")
    private Enseignant enseignant;

    // Constructeur sans argument requis par JPA
    public Cours() {
    }

    // Getters et setters
    public int getIdCours() {
        return idCours;
    }

    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public String getNomCours() {
        return nomCours;
    }

    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "idCours=" + idCours +
                ", nomCours='" + nomCours + '\'' +
                ", description='" + description + '\'' +
                ", enseignant=" +
                (enseignant != null ? enseignant.getIdEnseignant() : null) +
                '}';
    }
}
