package controller;

import application.SpringContext;
import modele.Classe;
import service.ClasseService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    private final ClasseService service;

    public ClasseController() {
        this(SpringContext.getBean(ClasseService.class));
    }

    @Autowired
    public ClasseController(ClasseService service) {
        this.service = service;
    }

    // Ajouter une classe
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Classe ajouterClasse(@RequestBody Classe classe) {

        return service.ajouter(classe);
    }

    // Rechercher par ID
    @GetMapping("/{id}")
    public Classe rechercherClasse(@PathVariable int id) {

        return service.rechercherParId(id);
    }

    // Rechercher par nom
    @GetMapping("/recherche")
    public Classe rechercherClasseParNom(@RequestParam String nom) {

        return service.rechercherParNom(nom);
    }

    // Afficher toutes les classes
    @GetMapping
    public List<Classe> afficherClasses() {

        return service.lister();
    }

    // Modifier une classes
    @PutMapping("/{id}")
    public Classe modifierClasse(
            @PathVariable int id,
            @RequestBody Classe classe) {

        classe.setIdClasse(id);

        return service.modifier(classe);
    }

    // Supprimer une classe
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerClasse(@PathVariable int id) {

        service.supprimer(id);
    }
}
