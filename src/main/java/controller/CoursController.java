package controller;

import application.SpringContext;
import modele.Cours;
import service.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cours")
public class CoursController {
    private final CoursService service;

    public CoursController() {
        this(SpringContext.getBean(CoursService.class));
    }

    @Autowired
    public CoursController(CoursService service) {
        this.service = service;
    }

    @PostMapping
    public Cours ajouterCours(@RequestBody Cours cours) {
        verifier(cours);
        return service.ajouter(cours);
    }

    @GetMapping("/{id}")
    public Cours rechercherCours(@PathVariable int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    @GetMapping
    public List<Cours> afficherCours() {
        return service.lister();
    }

    @PutMapping("/{id}")
    public Cours modifierCours(@PathVariable int id, @RequestBody Cours cours) {
        cours.setIdCours(id);
        verifier(cours);
        verifierId(cours.getIdCours());
        return service.modifier(cours);
    }

    @DeleteMapping("/{id}")
    public void supprimerCours(@PathVariable int id) {
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
