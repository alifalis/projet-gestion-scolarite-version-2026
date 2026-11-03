package dao;

import modele.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Integer> {
    default Specialite getById(int id) { return findById(id).orElse(null); }
    default List<Specialite> getAll() { return findAll(); }
    default void update(Specialite specialite) { save(specialite); }
    Specialite getByNom(String nom);
}
