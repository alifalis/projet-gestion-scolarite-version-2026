package controller;

import modele.InscriptionAnnee;
import service.InscriptionAnneeService;
import java.util.List;

public class InscriptionAnneeController {
    private final InscriptionAnneeService service;

    public InscriptionAnneeController() {
        this.service = new InscriptionAnneeService();
    }

    public InscriptionAnnee ajouterInscription(InscriptionAnnee inscription) {
        verifier(inscription);
        return service.ajouter(inscription);
    }

    public InscriptionAnnee rechercherInscription(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    public InscriptionAnnee rechercherParEtudiantEtAnnee(
            int idEtudiant,
            String anneeScolaire
    ) {
        verifierId(idEtudiant);
        return service.rechercherParEtudiantEtAnnee(
                idEtudiant, anneeScolaire);
    }

    public List<InscriptionAnnee> afficherInscriptions() {
        return service.lister();
    }

    public InscriptionAnnee modifierInscription(InscriptionAnnee inscription) {
        verifier(inscription);
        verifierId(inscription.getIdInscription());
        return service.modifier(inscription);
    }

    public void supprimerInscription(int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(InscriptionAnnee inscription) {
        if (inscription == null || inscription.getEtudiant() == null)
            throw new IllegalArgumentException("L'inscription et l'étudiant sont obligatoires.");
        if (inscription.getAnneeScolaire() == null || inscription.getAnneeScolaire().trim().isEmpty())
            throw new IllegalArgumentException("L'année scolaire est obligatoire.");
        if (inscription.getAnneeScolaire().length() > 9)
            throw new IllegalArgumentException("L'année scolaire ne doit pas dépasser 9 caractères.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}
