package service;

import dao.CoursRepository;
import modele.Cours;

import java.util.List;

public class CoursService {

    private final CoursRepository repository;

    public CoursService() {
        this.repository = new CoursRepository();
    }

    public Cours ajouter(Cours cours) {
        valider(cours);
        repository.save(cours);
        return cours;
    }

    public Cours rechercherParId(int id) {
        verifierId(id);
        return repository.getById(id);
    }

    public List<Cours> lister() {
        return repository.getAll();
    }

    public Cours modifier(Cours cours) {
        valider(cours);
        repository.update(cours);
        return cours;
    }

    public void supprimer(int id) {
        verifierId(id);
        repository.deleteById(id);
    }

    private void valider(Cours c) {
        if (c == null) {
            throw new IllegalArgumentException("Cours obligatoire.");
        }
        if (c.getNomCours() == null || c.getNomCours().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du cours est obligatoire.");
        }
    }

    private void verifierId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif.");
        }
    }
}
