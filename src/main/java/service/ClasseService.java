package service;

import dao.ClasseRepository;
import modele.Classe;

import java.util.List;

public class ClasseService {

    private final ClasseRepository repository;

    public ClasseService() {
        this.repository = new ClasseRepository();
    }

    public Classe ajouter(Classe classe) {
        valider(classe);
        repository.save(classe);
        return classe;
    }

    public Classe rechercherParId(int id) {
        verifierId(id);
        return repository.getById(id);
    }

    public Classe rechercherParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom obligatoire.");
        }
        return repository.getByNom(nom.trim());
    }

    public List<Classe> lister() {
        return repository.getAll();
    }

    public Classe modifier(Classe classe) {
        valider(classe);
        repository.update(classe);
        return classe;
    }

    public void supprimer(int id) {
        verifierId(id);
        repository.deleteById(id);
    }

    private void valider(Classe c) {
        if (c == null || c.getNom() == null || c.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("La classe et son nom sont obligatoires.");
        }
    }

    private void verifierId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif.");
        }
    }
}
