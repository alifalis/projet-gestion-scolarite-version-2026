package controller;

import application.SpringContext;
import modele.Resultat;
import service.ResultatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resultats")
public class ResultatController {
    private final ResultatService service;

    public ResultatController() {
        this(SpringContext.getBean(ResultatService.class));
    }

    @Autowired
    public ResultatController(ResultatService service) {
        this.service = service;
    }

    @PostMapping
    public Resultat ajouterResultat(@RequestBody Resultat resultat) {
        verifier(resultat);
        return service.ajouter(resultat);
    }

    @GetMapping("/{id}")
    public Resultat rechercherResultat(@PathVariable int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    @GetMapping
    public List<Resultat> afficherResultats() {
        return service.lister();
    }

    @GetMapping("/etudiant/{idEtudiant}")
    public List<Resultat> afficherResultatsParEtudiant(@PathVariable int idEtudiant) {
        verifierId(idEtudiant);
        return service.rechercherParEtudiant(idEtudiant);
    }

    @GetMapping("/enseignant/{idEnseignant}")
    public List<Resultat> afficherResultatsParEnseignant(@PathVariable int idEnseignant) {
        verifierId(idEnseignant);
        return service.rechercherParEnseignant(idEnseignant);
    }

    @PutMapping("/{id}")
    public Resultat modifierResultat(
            @PathVariable int id, @RequestBody Resultat resultat) {
        resultat.setIdResultat(id);
        verifier(resultat);
        verifierId(resultat.getIdResultat());
        return service.modifier(resultat);
    }

    public Resultat modifierResultat(Resultat resultat) {
        return modifierResultat(resultat.getIdResultat(), resultat);
    }

    @DeleteMapping("/{id}")
    public void supprimerResultat(@PathVariable int id) {
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
