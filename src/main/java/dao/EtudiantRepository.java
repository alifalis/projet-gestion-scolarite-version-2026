package dao;

import modele.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
    default Etudiant getById(int id) { return findById(id).orElse(null); }
    default List<Etudiant> getAll() { return findAll(); }
    default void update(Etudiant etudiant) { save(etudiant); }

    Etudiant findByUtilisateurEmail(String email);

    default Etudiant getByEmail(String email) {
        return findByUtilisateurEmail(email);
    }
}
