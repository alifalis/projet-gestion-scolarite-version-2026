package modele;

import java.io.Serializable;

import jakarta.persistence.*;



@Entity 
@Table (name = "roles")

public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_role", nullable = false)
    private int idRole;

    @Column (name = "nom_role",nullable = false,length = 100)
    private String nomRole;

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getNomRole() {
        return nomRole;
    }

    public void setNomRole(String nomRole) {
        this.nomRole = nomRole;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "idRole=" + idRole +
                ", nomRole='" + nomRole + '\'' +
                '}';
    }

}
