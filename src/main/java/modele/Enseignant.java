
package modele;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "enseignants")
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_enseignant", nullable = false)
    private int idEnseignant;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_naissance", nullable = false)
    private Date dateNaissance;

    @Column(name = "contact", length = 15)
    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_specialite")
    private Specialite specialite;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_utilisateur",
        referencedColumnName = "id_utilisateur",
        nullable = false,
        unique = true
    )
    private Utilisateur utilisateur;

    // Constructeur par défaut requis par JPA
    public Enseignant() {
    }

    // Getters et Setters
    public int getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(int idEnseignant) {
        this.idEnseignant = idEnseignant;
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

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Enseignant{" +
                "idEnseignant=" + idEnseignant +
                ", dateNaissance=" + dateNaissance +
                ", contact='" + contact + '\'' +
                ", specialite=" +
                (specialite != null ? specialite.getNom() : "null") +
                ", utilisateur=" +
                (utilisateur != null
                    ? utilisateur.getNom() + " " + utilisateur.getPrenom()
                    : "null") +
                '}';
    }
}
