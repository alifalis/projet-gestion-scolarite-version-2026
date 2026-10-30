package controller;

import modele.InscriptionCours;
import service.InscriptionCoursService;
import service.EtudiantService;
import modele.Etudiant;
import java.util.List;
import java.util.ArrayList;


public class InscriptionCoursController {
    private final InscriptionCoursService service;
    private final EtudiantService etudiantService;

    public InscriptionCoursController() {
        this.service = new InscriptionCoursService();
        this.etudiantService = new EtudiantService();
    }

    public InscriptionCours ajouterInscription(InscriptionCours inscription) {
        verifier(inscription);
        return service.ajouter(inscription);
    }

    public InscriptionCours rechercherInscription(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    public List<InscriptionCours> afficherInscriptions(int idEtudiant) {
        return service.rechercherParEtudiant(idEtudiant);
    }

    public List<InscriptionCours> afficherToutesInscriptions() {
        return service.lister();
    }


    public List<Etudiant> afficherCours(int idCours) {


        // Récupérer les identifiants des étudiants inscrits au cours
        List<Integer> ids = service.listeEtudiantsInscritAuCourt(idCours);

        // Créer une liste pour stocker les étudiants
        List<Etudiant> etudiants = new ArrayList<>();

        // Rechercher chaque étudiant à partir de son identifiant
        for (Integer idEtudiant : ids) {
            Etudiant etudiant = etudiantService.rechercherParId(idEtudiant);

            if (etudiant != null) {
                etudiants.add(etudiant);
            }
        }

        return etudiants;
    }

    public InscriptionCours modifierInscription(InscriptionCours inscription) {
        verifier(inscription);
        verifierId(inscription.getIdInscription());
        return service.modifier(inscription);
    }

    public void supprimerInscription(int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(InscriptionCours inscription) {
        if (inscription == null || inscription.getEtudiant() == null || inscription.getCours() == null)
            throw new IllegalArgumentException("L'inscription, l'étudiant et le cours sont obligatoires.");
        if (inscription.getDebutCours() != null && inscription.getFinCours() != null
                && inscription.getFinCours().isBefore(inscription.getDebutCours()))
            throw new IllegalArgumentException("La fin du cours doit être postérieure au début.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}
