package controller;

import modele.Utilisateur;
import service.UtilisateurService;
import java.util.List;

public class UtilisateurController {
    private final UtilisateurService service;

    public UtilisateurController() {
        this.service = new UtilisateurService();
    }

    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        verifier(utilisateur);
        return service.ajouterUtilisateur(utilisateur);
    }

    public Utilisateur rechercherUtilisateur(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }
    public Utilisateur rechercherUtilisateur(String email) {

        return service.rechercherParEmail(email);
    }

    public Utilisateur rechercherParEmail(String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("L'adresse e-mail est obligatoire.");
        return service.rechercherParEmail(email);
    }

    public List<Utilisateur> afficherUtilisateurs() {
        return service.afficher();
    }

    public Utilisateur modifierUtilisateur(Utilisateur utilisateur) {
        verifier(utilisateur);
        verifierId(utilisateur.getIdUtilisateur());
        return service.modifierUtilisateur(utilisateur);
    }

    public void supprimerUtilisateur(int id) {
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
