
package controller;

import modele.Role;
import service.RoleService;

import java.util.List;

public class RoleController {

    private final RoleService roleService;

    public RoleController() {
        this.roleService = new RoleService();
    }

    // Ajouter un rôle
    public void ajouterRole(Role role) {
        roleService.ajouterRole(role);
    }

    // Rechercher un rôle par identifiant
    public Role rechercherRole(int id) {
        return roleService.rechercherRole(id);
    }

    // Récupérer tous les rôles
    public List<Role> afficherRoles() {
        return roleService.getAll();
    }

    // Rechercher un rôle par nom
    public Role rechercherRoleParNom(String nomRole) {
        return roleService.rechercherRoleParNom(nomRole);
    }

    // Modifier un rôle
    public void modifierRole(Role role) {
        roleService.modifierRole(role);
    }

    // Supprimer un rôle
    public void supprimerRole(int id) {
        roleService.supprimerRole(id);
    }
}
