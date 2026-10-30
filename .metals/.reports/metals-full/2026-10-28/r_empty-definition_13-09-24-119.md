error id: file://<WORKSPACE>/src/main/java/service/SpecialiteService.java:_empty_/Specialite#
file://<WORKSPACE>/src/main/java/service/SpecialiteService.java
empty definition using pc, found symbol in pc: _empty_/Specialite#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 793
uri: file://<WORKSPACE>/src/main/java/service/SpecialiteService.java
text:
```scala
package service;

import dao.SpecialiteRepository;
import modele.Specialite;

import java.util.List;

public class SpecialiteService {

    private final SpecialiteRepository repository;

    public SpecialiteService() {
        repository = new SpecialiteRepository();
    }

    public SpecialiteService(SpecialiteRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository obligatoire.");
        }
        this.repository = repository;
    }

    public Specialite ajouter(Specialite specialite) {
        valider(specialite);
        repository.save(specialite);
        return specialite;
    }

    public Specialite rechercherParId(int id) {
        verifierId(id);
        return repository.getById(id);
    }

    public Spec@@ialite rechercherParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom obligatoire.");
        }
        return repository.getByNom(nom.trim());
    }

    public List<Specialite> lister() {
        return repository.getAll();
    }

    public Specialite modifier(Specialite specialite) {
        valider(specialite);
        repository.update(specialite);
        return specialite;
    }

    public void supprimer(int id) {
        verifierId(id);
        repository.deleteById(id);
    }

    private void valider(Specialite s) {
        if (s == null || s.getNom() == null || s.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité et son nom sont obligatoires.");
        }
    }

    private void verifierId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif.");
        }
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/Specialite#