error id: file://<WORKSPACE>/src/main/java/controller/ClasseController.java:_empty_/ClasseService#lister#
file://<WORKSPACE>/src/main/java/controller/ClasseController.java
empty definition using pc, found symbol in pc: _empty_/ClasseService#lister#
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 796
uri: file://<WORKSPACE>/src/main/java/controller/ClasseController.java
text:
```scala
package controller;

import modele.Classe;
import service.ClasseService;
import java.util.List;

public class ClasseController {
    private final ClasseService service;

    public ClasseController() {
        this.service = new ClasseService();
    }

    public Classe ajouterClasse(Classe classe) {
        verifier(classe);
        return service.ajouter(classe);
    }

    public Classe rechercherClasse(int id) {
        verifierId(id);
        return service.rechercherParId(id);
    }

    public Classe rechercherClasseParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) throw new IllegalArgumentException("Le nom de la classe est obligatoire.");
        return service.rechercherParNom(nom);
    }

    public List<Classe> afficherClasses() {
        return service.@@lister();
    }

    public Classe modifierClasse(Classe classe) {
        verifier(classe);
        verifierId(classe.getIdClasse());
        return service.modifier(classe);
    }

    public void supprimerClasse(int id) {
        verifierId(id);
        service.supprimer(id);
    }

    private void verifier(Classe classe) {
        if (classe == null || classe.getNom() == null || classe.getNom().trim().isEmpty())
            throw new IllegalArgumentException("Le nom de la classe est obligatoire.");
    }

    private void verifierId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/ClasseService#lister#