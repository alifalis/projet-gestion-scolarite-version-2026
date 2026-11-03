package dao;

import modele.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Integer> {

    // Rechercher une classe par son nom
    Optional<Classe> findByNom(String nom);

    // Vérifier si une classe existe avec ce nom
    boolean existsByNom(String nom);
}
