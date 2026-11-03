package dao;

import modele.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Integer> {
    default Enseignant getById(int id) { return findById(id).orElse(null); }
    default List<Enseignant> getAll() { return findAll(); }
    default void update(Enseignant enseignant) { save(enseignant); }

    Enseignant findByUtilisateurEmail(String email);

    default Enseignant getByEmail(String email) {
        return findByUtilisateurEmail(email);
    }

    default List<Enseignant> rechercher(String recherche) {
        return findByUtilisateurNomContainingIgnoreCaseOrUtilisateurPrenomContainingIgnoreCase(
                recherche, recherche);
    }

    List<Enseignant> findByUtilisateurNomContainingIgnoreCaseOrUtilisateurPrenomContainingIgnoreCase(
            String nom, String prenom);
}
