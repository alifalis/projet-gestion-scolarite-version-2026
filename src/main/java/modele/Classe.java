
package modele;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classes")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_classe")
    private int idClasse;

    @Column(name = "nom", nullable = false, unique = true, length = 15)
    private String nom;

    // Une classe peut contenir plusieurs étudiants
    @OneToMany(mappedBy = "classe")
    private Set<Etudiant> etudiants = new HashSet<>();

    // Constructeur vide obligatoire pour JPA
    public Classe() {
    }

    public Classe(String nom) {
        this.nom = nom;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }
}
