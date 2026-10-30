package controller;

import modele.Etudiant;
import service.EtudiantService;
import java.util.List;

public class EtudiantController {
    private final EtudiantService service;

    public EtudiantController() {
        this.service = new EtudiantService();
    }

    public Etudiant ajouterEtudiant(Etudiant etudiant) {
        verifier(etudiant);
        return service.ajouter(etudiant);
    }

    public Etudiant rechercherEtudiant(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }
    public Etudiant rechercherEtudiant(String email) {
        
        return service.rechercherParEmail(email);
    }


    public List<Etudiant> afficherEtudiants() {
        return service.lister();
    }

    public Etudiant modifierEtudiant(Etudiant etudiant) {
        verifier(etudiant);
        verifierId(etudiant.getIdEtudiant());
        return service.modifier(etudiant);
    }

    public void supprimerEtudiant(int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(Etudiant etudiant) {
        if (etudiant == null || etudiant.getUtilisateur() == null)
            throw new IllegalArgumentException("L'étudiant et son utilisateur sont obligatoires.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }

}
