package controller;

import application.SpringContext;
import modele.Enseignant;
import service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
public class EnseignantController {
    private final EnseignantService service;

    public EnseignantController() {
        this(SpringContext.getBean(EnseignantService.class));
    }

    @Autowired
    public EnseignantController(EnseignantService service) {
        this.service = service;
    }

    @PostMapping
    public Enseignant ajouterEnseignant(@RequestBody Enseignant enseignant) {
        verifier(enseignant);
        return service.ajouter(enseignant);
    }

    @GetMapping("/{id}")
    public Enseignant rechercherEnseignant(@PathVariable int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }
    @GetMapping("/recherche")
    public Enseignant rechercherEnseignant(@RequestParam String email) {
        return service.rechercherParEmail(email);
    }

    @GetMapping
    public List<Enseignant> afficherEnseignants() {
        return service.lister();
    }

    @PutMapping("/{id}")
    public Enseignant modifierEnseignant(
            @PathVariable int id, @RequestBody Enseignant enseignant) {
        enseignant.setIdEnseignant(id);
        verifier(enseignant);
        verifierId(enseignant.getIdEnseignant());
        return service.modifier(enseignant);
    }

    @DeleteMapping("/{id}")
    public void supprimerEnseignant(@PathVariable int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(Enseignant enseignant) {
        if (enseignant == null || enseignant.getUtilisateur() == null)
            throw new IllegalArgumentException("L'enseignant et son utilisateur sont obligatoires.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}
