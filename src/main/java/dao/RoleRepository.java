package dao;

import modele.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    default void add(Role role) { save(role); }
    default Role getById(int id) { return findById(id).orElse(null); }
    default List<Role> getAll() { return findAll(); }
    default void update(Role role) { save(role); }
    Role getByNomRole(String nomRole);
}
