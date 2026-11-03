package dao;

import modele.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultatRepository extends JpaRepository<Resultat, Integer> {
    default Resultat getById(int id) { return findById(id).orElse(null); }
    default List<Resultat> getAll() { return findAll(); }
    default void update(Resultat resultat) { save(resultat); }
    List<Resultat> findByEtudiantIdEtudiant(int idEtudiant);
    List<Resultat> findByCoursEnseignantIdEnseignant(int idEnseignant);
    Resultat findByCoursIdCoursAndEtudiantIdEtudiant(int idCours, int idEtudiant);

    default List<Resultat> getByEtudiant(int id) { return findByEtudiantIdEtudiant(id); }
    default List<Resultat> getByEnseignant(int id) {
        return findByCoursEnseignantIdEnseignant(id);
    }
    default Resultat getByCoursAndEtudiant(int cours, int etudiant) {
        return findByCoursIdCoursAndEtudiantIdEtudiant(cours, etudiant);
    }
}
