package controller;

import application.SpringContext;
import modele.InscriptionAnnee;
import service.InscriptionAnneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscriptions-annee")
public class InscriptionAnneeController {
    private final InscriptionAnneeService service;

    public InscriptionAnneeController() {
        this(SpringContext.getBean(InscriptionAnneeService.class));
    }

    @Autowired
    public InscriptionAnneeController(InscriptionAnneeService service) {
        this.service = service;
    }

    @PostMapping
    public InscriptionAnnee ajouterInscription(@RequestBody InscriptionAnnee inscription) {
        verifier(inscription);
        return service.ajouter(inscription);
    }

    @GetMapping("/{id}")
    public InscriptionAnnee rechercherInscription(@PathVariable int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    public InscriptionAnnee rechercherParEtudiantEtAnnee(
            @RequestParam int idEtudiant,
            @RequestParam String anneeScolaire
    ) {
        verifierId(idEtudiant);
        return service.rechercherParEtudiantEtAnnee(
                idEtudiant, anneeScolaire);
    }

    @GetMapping
    public List<InscriptionAnnee> afficherInscriptions() {
        return service.lister();
    }

    @PutMapping("/{id}")
    public InscriptionAnnee modifierInscription(
            @PathVariable int id, @RequestBody InscriptionAnnee inscription) {
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
