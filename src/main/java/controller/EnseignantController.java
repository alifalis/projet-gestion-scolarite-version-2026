package controller;

import modele.Enseignant;
import service.EnseignantService;
import java.util.List;

public class EnseignantController {
    private final EnseignantService service;

    public EnseignantController() {
        this.service = new EnseignantService();
    }

    public Enseignant ajouterEnseignant(Enseignant enseignant) {
        verifier(enseignant);
        return service.ajouter(enseignant);
    }

    public Enseignant rechercherEnseignant(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }
    public Enseignant rechercherEnseignant(String email) {
        return service.rechercherParEmail(email);
    }

    public List<Enseignant> afficherEnseignants() {
        return service.lister();
    }

    public Enseignant modifierEnseignant(Enseignant enseignant) {
        verifier(enseignant);
        verifierId(enseignant.getIdEnseignant());
        return service.modifier(enseignant);
    }

    public void supprimerEnseignant(int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(Enseignant enseignant) {
        if (enseignant == null || enseignant.getUtilisateur() == null)
            throw new IllegalArgumentException("L'enseignant et son utilisateur sont obligatoires.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}
