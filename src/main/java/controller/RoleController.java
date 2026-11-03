
package controller;

import application.SpringContext;
import modele.Role;
import service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController() {
        this(SpringContext.getBean(RoleService.class));
    }

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Ajouter un rôle
    @PostMapping
    public void ajouterRole(@RequestBody Role role) {
        roleService.ajouterRole(role);
    }

    // Rechercher un rôle par identifiant
    @GetMapping("/{id}")
    public Role rechercherRole(@PathVariable int id) {
        return roleService.rechercherRole(id);
    }

    // Récupérer tous les rôles
    @GetMapping
    public List<Role> afficherRoles() {
        return roleService.getAll();
    }

    // Rechercher un rôle par nom
    @GetMapping("/recherche")
    public Role rechercherRoleParNom(@RequestParam String nomRole) {
        return roleService.rechercherRoleParNom(nomRole);
    }

    // Modifier un rôle
    @PutMapping("/{id}")
    public void modifierRole(@PathVariable int id, @RequestBody Role role) {
        role.setIdRole(id);
        roleService.modifierRole(role);
    }

    // Supprimer un rôle
    @DeleteMapping("/{id}")
    public void supprimerRole(@PathVariable int id) {
        roleService.supprimerRole(id);
    }
}
