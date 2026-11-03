package controller;

import application.SpringContext;
import modele.InscriptionCours;
import service.InscriptionCoursService;
import service.EtudiantService;
import modele.Etudiant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;


@RestController
@RequestMapping("/api/inscriptions-cours")
public class InscriptionCoursController {
    private final InscriptionCoursService service;
    private final EtudiantService etudiantService;

    public InscriptionCoursController() {
        this(SpringContext.getBean(InscriptionCoursService.class),
                SpringContext.getBean(EtudiantService.class));
    }

    @Autowired
    public InscriptionCoursController(
            InscriptionCoursService service, EtudiantService etudiantService) {
        this.service = service;
        this.etudiantService = etudiantService;
    }

    @PostMapping
    public InscriptionCours ajouterInscription(@RequestBody InscriptionCours inscription) {
        verifier(inscription);
        return service.ajouter(inscription);
    }

    @GetMapping("/{id}")
    public InscriptionCours rechercherInscription(@PathVariable int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    @GetMapping("/etudiant/{idEtudiant}")
    public List<InscriptionCours> afficherInscriptions(@PathVariable int idEtudiant) {
        return service.rechercherParEtudiant(idEtudiant);
    }

    @GetMapping
    public List<InscriptionCours> afficherToutesInscriptions() {
        return service.lister();
    }


    @GetMapping("/cours/{idCours}/etudiants")
    public List<Etudiant> afficherCours(@PathVariable int idCours) {


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

    @PutMapping("/{id}")
    public InscriptionCours modifierInscription(
            @PathVariable int id, @RequestBody InscriptionCours inscription) {
        inscription.setIdInscription(id);
        verifier(inscription);
        verifierId(inscription.getIdInscription());
        return service.modifier(inscription);
    }

    @DeleteMapping("/{id}")
    public void supprimerInscription(@PathVariable int id) {
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
