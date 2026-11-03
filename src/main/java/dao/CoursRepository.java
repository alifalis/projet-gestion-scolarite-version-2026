package dao;

import modele.Cours;
import modele.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Integer> {
    default void saveLegacy(Cours cours) { save(cours); }
    default Cours getById(int id) { return findById(id).orElse(null); }
    default List<Cours> getAll() { return findAll(); }
    default void update(Cours cours) { save(cours); }

    Enseignant findEnseignantByNomCoursAndEnseignantUtilisateurEmail(
            String nom, String email);

    default Enseignant getEnseignantByNomCoursAndProfesseur(String nom, String email) {
        return findEnseignantByNomCoursAndEnseignantUtilisateurEmail(nom, email);
    }
}
