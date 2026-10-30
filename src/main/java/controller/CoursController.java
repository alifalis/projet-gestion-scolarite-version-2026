package controller;

import modele.Cours;
import service.CoursService;
import java.util.List;

public class CoursController {
    private final CoursService service;

    public CoursController() {
        this.service = new CoursService();
    }

    public Cours ajouterCours(Cours cours) {
        verifier(cours);
        return service.ajouter(cours);
    }

    public Cours rechercherCours(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    public List<Cours> afficherCours() {
        return service.lister();
    }

    public Cours modifierCours(Cours cours) {
        verifier(cours);
        verifierId(cours.getIdCours());
        return service.modifier(cours);
    }

    public void supprimerCours(int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(Cours cours) {
        if (cours == null || cours.getNomCours() == null || cours.getNomCours().trim().isEmpty())
            throw new IllegalArgumentException("Le nom du cours est obligatoire.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}
