package dao;

import modele.InscriptionCours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionCoursRepository extends JpaRepository<InscriptionCours, Integer> {
    default InscriptionCours getById(int id) { return findById(id).orElse(null); }
    default List<InscriptionCours> getAll() { return findAll(); }
    default void update(InscriptionCours inscription) { save(inscription); }

    List<InscriptionCours> findByEtudiantIdEtudiant(int idEtudiant);

    List<InscriptionCours> findByCoursIdCours(int idCours);

    default List<Integer> getIdEtudiantsByCours(int idCours) {
        return findByCoursIdCours(idCours).stream()
                .map(inscription -> inscription.getEtudiant().getIdEtudiant())
                .toList();
    }

    default List<InscriptionCours> getByEtudiant(int idEtudiant) {
        return findByEtudiantIdEtudiant(idEtudiant);
    }
}
