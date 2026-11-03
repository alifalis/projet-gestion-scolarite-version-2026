package controller;

import application.SpringContext;
import modele.Utilisateur;
import service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    private final UtilisateurService service;

    public UtilisateurController() {
        this(SpringContext.getBean(UtilisateurService.class));
    }

    @Autowired
    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    @PostMapping
    public Utilisateur ajouterUtilisateur(@RequestBody Utilisateur utilisateur) {
        verifier(utilisateur);
        return service.ajouterUtilisateur(utilisateur);
    }

    @GetMapping("/{id}")
    public Utilisateur rechercherUtilisateur(@PathVariable int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }
    @GetMapping("/recherche")
    public Utilisateur rechercherUtilisateur(@RequestParam String email) {

        return service.rechercherParEmail(email);
    }

    public Utilisateur rechercherParEmail(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("L'adresse e-mail est obligatoire.");
        return service.rechercherParEmail(email);
    }

    @GetMapping
    public List<Utilisateur> afficherUtilisateurs() {
        return service.afficher();
    }

    @PutMapping("/{id}")
    public Utilisateur modifierUtilisateur(
            @PathVariable int id, @RequestBody Utilisateur utilisateur) {
        utilisateur.setIdUtilisateur(id);
        verifier(utilisateur);
        verifierId(utilisateur.getIdUtilisateur());
        return service.modifierUtilisateur(utilisateur);
    }

    public Utilisateur modifierUtilisateur(Utilisateur utilisateur) {
        return modifierUtilisateur(utilisateur.getIdUtilisateur(), utilisateur);
    }

    @DeleteMapping("/{id}")
    public void supprimerUtilisateur(@PathVariable int id) {
        verifierId(id);
        service.supprimerUtilisateur(id);
    }

    private void verifier(Utilisateur utilisateur) {
        if (utilisateur == null) throw new IllegalArgumentException("L'utilisateur est obligatoire.");
        if (utilisateur.getNom() == null || utilisateur.getNom().trim().isEmpty()
                || utilisateur.getPrenom() == null || utilisateur.getPrenom().trim().isEmpty())
            throw new IllegalArgumentException("Le nom et le prénom sont obligatoires.");
        if (utilisateur.getEmail() == null || utilisateur.getEmail().trim().isEmpty())
            throw new IllegalArgumentException("L'adresse e-mail est obligatoire.");
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().trim().isEmpty())
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}
