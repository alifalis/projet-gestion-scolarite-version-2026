
package modele;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "specialites")
public class Specialite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_specialite")
    private int idSpecialite;

    @Column(name = "nom", nullable = false, unique = true, length = 100)
    private String nom;

    // Une spécialité peut être associée à plusieurs enseignants
    @OneToMany(mappedBy = "specialite")
    private Set<Enseignant> enseignants = new HashSet<>();

    // Constructeur vide obligatoire pour JPA
    public Specialite() {
    }

    public Specialite(String nom) {
        this.nom = nom;
    }

    public int getIdSpecialite() {
        return idSpecialite;
    }

    public void setIdSpecialite(int idSpecialite) {
        this.idSpecialite = idSpecialite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Enseignant> getEnseignants() {
        return enseignants;
    }

    public void setEnseignants(Set<Enseignant> enseignants) {
        this.enseignants = enseignants;
    }
}
