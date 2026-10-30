package controller;

import modele.Resultat;
import service.ResultatService;
import java.util.List;

public class ResultatController {
    private final ResultatService service;

    public ResultatController() {
        this.service = new ResultatService();
    }

    public Resultat ajouterResultat(Resultat resultat) {
        verifier(resultat);
        return service.ajouter(resultat);
    }

    public Resultat rechercherResultat(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    public List<Resultat> afficherResultats() {
        return service.lister();
    }

    public List<Resultat> afficherResultatsParEtudiant(int idEtudiant) {
        verifierId(idEtudiant);
        return service.rechercherParEtudiant(idEtudiant);
    }

    public List<Resultat> afficherResultatsParEnseignant(int idEnseignant) {
        verifierId(idEnseignant);
        return service.rechercherParEnseignant(idEnseignant);
    }

    public Resultat modifierResultat(Resultat resultat) {
        verifier(resultat);
        verifierId(resultat.getIdResultat());
        return service.modifier(resultat);
    }

    public void supprimerResultat(int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(Resultat resultat) {
        if (resultat == null) throw new IllegalArgumentException("Le résultat est obligatoire.");
        if (resultat.getEtudiant() == null || resultat.getCours() == null)
            throw new IllegalArgumentException("L'étudiant et le cours sont obligatoires.");
        if (resultat.getNote() != null && (resultat.getNote().doubleValue() < 0 || resultat.getNote().doubleValue() > 20))
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}
