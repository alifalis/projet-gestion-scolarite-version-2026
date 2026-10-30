
package service;

import dao.RoleRepository;
import modele.Role;

import java.util.List;

public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService() {
        this.roleRepository = new RoleRepository();
    }

    // Ajouter un rôle
    public void ajouterRole(Role role) {
        verifierRole(role);

        if (role.getNomRole().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Le nom du rôle est obligatoire."
            );
        }

        Role existant = roleRepository.getByNomRole(
                role.getNomRole().trim()
        );

        if (existant != null) {
            throw new IllegalArgumentException(
                    "Ce rôle existe déjà."
            );
        }

        roleRepository.add(role);
    }

    // Rechercher un rôle par identifiant
    public Role rechercherRole(int id) {
        verifierId(id);

        Role role = roleRepository.getById(id);

        if (role == null) {
            throw new IllegalArgumentException(
                    "Aucun rôle trouvé avec l'identifiant " + id
            );
        }

        return role;
    }

    // Récupérer tous les rôles
    public List<Role> getAll() {
        return roleRepository.getAll();
    }

    // Rechercher un rôle par nom
    public Role rechercherRoleParNom(String nomRole) {
        if (nomRole == null || nomRole.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Le nom du rôle est obligatoire."
            );
        }

        return roleRepository.getByNomRole(nomRole.trim());
    }

    // Modifier un rôle
    public void modifierRole(Role role) {
        verifierRole(role);
        verifierId(role.getIdRole());

        if (role.getNomRole().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Le nom du rôle est obligatoire."
            );
        }

        Role existant = roleRepository.getByNomRole(
                role.getNomRole().trim()
        );

        if (existant != null
                && existant.getIdRole() != role.getIdRole()) {
            throw new IllegalArgumentException(
                    "Un autre rôle possède déjà ce nom."
            );
        }

        roleRepository.update(role);
    }

    // Supprimer un rôle
    public void supprimerRole(int id) {
        verifierId(id);

        // Vérifier que le rôle existe
        rechercherRole(id);

        roleRepository.deleteById(id);
    }

    // Vérifier le rôle
    private void verifierRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException(
                    "Le rôle ne peut pas être null."
            );
        }

        if (role.getNomRole() == null) {
            throw new IllegalArgumentException(
                    "Le nom du rôle est obligatoire."
            );
        }
    }

    // Vérifier l'identifiant
    private void verifierId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant doit être positif."
            );
        }
    }
}
