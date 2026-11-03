package controller;

import application.SpringContext;
import modele.Etudiant;
import service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {
    private final EtudiantService service;

    public EtudiantController() {
        this(SpringContext.getBean(EtudiantService.class));
    }

    @Autowired
    public EtudiantController(EtudiantService service) {
        this.service = service;
    }

    @PostMapping
    public Etudiant ajouterEtudiant(@RequestBody Etudiant etudiant) {
        verifier(etudiant);
        return service.ajouter(etudiant);
    }

    @GetMapping("/{id}")
    public Etudiant rechercherEtudiant(@PathVariable int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }
    @GetMapping("/recherche")
    public Etudiant rechercherEtudiant(@RequestParam String email) {
        
        return service.rechercherParEmail(email);
    }


    @GetMapping
    public List<Etudiant> afficherEtudiants() {
        return service.lister();
    }

    @PutMapping("/{id}")
    public Etudiant modifierEtudiant(
            @PathVariable int id, @RequestBody Etudiant etudiant) {
        etudiant.setIdEtudiant(id);
        verifier(etudiant);
        verifierId(etudiant.getIdEtudiant());
        return service.modifier(etudiant);
    }

    public Etudiant modifierEtudiant(Etudiant etudiant) {
        return modifierEtudiant(etudiant.getIdEtudiant(), etudiant);
    }

    @DeleteMapping("/{id}")
    public void supprimerEtudiant(@PathVariable int id) {
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
