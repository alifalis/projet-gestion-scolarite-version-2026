package dao;

import modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    default Utilisateur getById(int id) { return findById(id).orElse(null); }
    default List<Utilisateur> getAll() { return findAll(); }
    default void update(Utilisateur utilisateur) { save(utilisateur); }
    Utilisateur getByEmail(String email);
}
