error id: file://<WORKSPACE>/src/main/java/service/InscriptionAnneeService.java:dao/InscriptionAnneeRepository#
file://<WORKSPACE>/src/main/java/service/InscriptionAnneeService.java
empty definition using pc, found symbol in pc: dao/InscriptionAnneeRepository#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 29
uri: file://<WORKSPACE>/src/main/java/service/InscriptionAnneeService.java
text:
```scala
package service;

import dao.@@InscriptionAnneeRepository;
import modele.InscriptionAnnee;

import java.util.List;

public class InscriptionAnneeService {

    private final InscriptionAnneeRepository repository;

    public InscriptionAnneeService() {
        this.repository = new InscriptionAnneeRepository();
    }

    public InscriptionAnnee ajouter(InscriptionAnnee inscription) {
        valider(inscription);
        repository.save(inscription);
        return inscription;
    }

    public InscriptionAnnee rechercherParId(int id) {
        verifierId(id);
        return repository.getById(id);
    }

    public List<InscriptionAnnee> lister() {
        return repository.getAll();
    }

    public InscriptionAnnee modifier(InscriptionAnnee inscription) {
        valider(inscription);
        repository.update(inscription);
        return inscription;
    }

    public void supprimer(int id) {
        verifierId(id);
        repository.deleteById(id);
    }

    private void valider(InscriptionAnnee i) {
        if (i == null) {
            throw new IllegalArgumentException("Inscription obligatoire.");
        }
        if (i.getEtudiant() == null) {
            throw new IllegalArgumentException("Étudiant obligatoire.");
        }

        String annee = i.getAnneeScolaire();
        if (annee == null || !annee.matches("^\\d{4}-\\d{4}$")) {
            throw new IllegalArgumentException("Format d'année scolaire attendu : AAAA-AAAA.");
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

empty definition using pc, found symbol in pc: dao/InscriptionAnneeRepository#