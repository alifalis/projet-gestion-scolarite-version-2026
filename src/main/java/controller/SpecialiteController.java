package controller;

import application.SpringContext;
import modele.Specialite;
import service.SpecialiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/specialites")
public class SpecialiteController {
    private final SpecialiteService service;

    public SpecialiteController() {
        this(SpringContext.getBean(SpecialiteService.class));
    }

    @Autowired
    public SpecialiteController(SpecialiteService service) {
        this.service = service;
    }

    @PostMapping
    public Specialite ajouterSpecialite(@RequestBody Specialite specialite) {
        verifier(specialite);
        return service.ajouter(specialite);
    }

    @GetMapping("/{id}")
    public Specialite rechercherSpecialite(@PathVariable int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    @GetMapping("/recherche")
    public Specialite rechercherSpecialiteParNom(@RequestParam String nom) {
        if (nom == null || nom.trim().isEmpty()) throw new IllegalArgumentException("Le nom de la spécialité est obligatoire.");
        return service.rechercherParNom(nom);
    }

    @GetMapping
    public List<Specialite> afficherSpecialites() {
        return service.lister();
    }

    @PutMapping("/{id}")
    public Specialite modifierSpecialite(
            @PathVariable int id, @RequestBody Specialite specialite) {
        specialite.setIdSpecialite(id);
        verifier(specialite);
        verifierId(specialite.getIdSpecialite());
        return service.modifier(specialite);
    }

    @DeleteMapping("/{id}")
    public void supprimerSpecialite(@PathVariable int id) {
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
