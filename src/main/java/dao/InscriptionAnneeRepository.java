package dao;

import modele.InscriptionAnnee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionAnneeRepository extends JpaRepository<InscriptionAnnee, Integer> {
    default InscriptionAnnee getById(int id) { return findById(id).orElse(null); }
    default List<InscriptionAnnee> getAll() { return findAll(); }
    default void update(InscriptionAnnee inscription) { save(inscription); }

    InscriptionAnnee getByEtudiantIdEtudiantAndAnneeScolaire(
            int idEtudiant, String anneeScolaire);

    default InscriptionAnnee getByEtudiantAndAnnee(
            int idEtudiant, String anneeScolaire) {
        return getByEtudiantIdEtudiantAndAnneeScolaire(idEtudiant, anneeScolaire);
    }
}
