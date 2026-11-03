
package modele;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "etudiants")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etudiant", nullable = false)
    private int idEtudiant;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_naissance", nullable = false)
    private Date dateNaissance;

    @Column(name = "contact", length = 15)
    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe")
    private Classe classe;

    @Column(name = "filiere", length = 50)
    private String filiere;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_utilisateur",
        referencedColumnName = "id_utilisateur",
        nullable = false,
        unique = true
    )
    private Utilisateur utilisateur;

    // Constructeur par défaut requis par JPA
    public Etudiant() {
    }

    // Getters et Setters
    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public String getFiliere() {
    return filiere;
}

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "idEtudiant=" + idEtudiant +
                ", dateNaissance=" + dateNaissance +
                ", contact='" + contact + '\'' +
                ", classe=" + (classe != null ? classe.getNom() : "null") +
                ", filiere=" + filiere +
                ", utilisateur=" +
                (utilisateur != null
                    ? utilisateur.getNom() + " " + utilisateur.getPrenom()
                    : "null") +
                '}';
    }
}
