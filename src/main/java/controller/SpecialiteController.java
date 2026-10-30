package controller;

import modele.Specialite;
import service.SpecialiteService;
import java.util.List;

public class SpecialiteController {
    private final SpecialiteService service;

    public SpecialiteController() {
        this.service = new SpecialiteService();
    }

    public Specialite ajouterSpecialite(Specialite specialite) {
        verifier(specialite);
        return service.ajouter(specialite);
    }

    public Specialite rechercherSpecialite(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    public Specialite rechercherSpecialiteParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) throw new IllegalArgumentException("Le nom de la spécialité est obligatoire.");
        return service.rechercherParNom(nom);
    }

    public List<Specialite> afficherSpecialites() {
        return service.lister();
    }

    public Specialite modifierSpecialite(Specialite specialite) {
        verifier(specialite);
        verifierId(specialite.getIdSpecialite());
        return service.modifier(specialite);
    }

    public void supprimerSpecialite(int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(Specialite specialite) {
        if (specialite == null || specialite.getNom() == null || specialite.getNom().trim().isEmpty())
            throw new IllegalArgumentException("Le nom de la spécialité est obligatoire.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}
