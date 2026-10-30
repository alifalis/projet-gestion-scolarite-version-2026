package service;

import dao.ResultatRepository;
import modele.Resultat;

import java.math.BigDecimal;
import java.util.List;

public class ResultatService {

    private final ResultatRepository repository;

    public ResultatService() {
        this.repository = new ResultatRepository();
    }

    public Resultat ajouter(Resultat resultat) {
        valider(resultat);
        repository.save(resultat);
        return resultat;
    }

    public Resultat rechercherParId(int id) {
        verifierId(id);
        return repository.getById(id);
    }

    public List<Resultat> lister() {
        return repository.getAll();
    }

    public List<Resultat> rechercherParEtudiant(int idEtudiant) {
        verifierId(idEtudiant);
        return repository.getByEtudiant(idEtudiant);
    }

    public List<Resultat> rechercherParEnseignant(int idEnseignant) {
        verifierId(idEnseignant);
        return repository.getByEnseignant(idEnseignant);
    }

    public Resultat modifier(Resultat resultat) {
        valider(resultat);
        repository.update(resultat);
        return resultat;
    }

    public void supprimer(int id) {
        verifierId(id);
        repository.deleteById(id);
    }

    private void valider(Resultat r) {
        if (r == null) {
            throw new IllegalArgumentException("Résultat obligatoire.");
        }
        if (r.getEtudiant() == null) {
            throw new IllegalArgumentException("Étudiant obligatoire.");
        }
        if (r.getCours() == null) {
            throw new IllegalArgumentException("Cours obligatoire.");
        }

        BigDecimal note = r.getNote();
        if (note != null && (note.compareTo(BigDecimal.ZERO) < 0 ||
                note.compareTo(BigDecimal.valueOf(20)) > 0)) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
        }
    }

    private void verifierId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif.");
        }
    }
}
