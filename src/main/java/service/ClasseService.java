package service;

import dao.ClasseRepository;
import modele.Classe;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Transactional
public class ClasseService {

    private final ClasseRepository repository;

    // Injection de dépendances par constructeur
    @Autowired
    public ClasseService(ClasseRepository repository) {
        this.repository = repository;
    }

    // Ajouter une classe
    public Classe ajouter(Classe classe) {

        valider(classe);

        if (repository.existsByNom(classe.getNom().trim())) {
            throw new IllegalArgumentException(
                    "Une classe avec ce nom existe déjà.");
        }

        classe.setNom(classe.getNom().trim());

        return repository.save(classe);
    }

    // Rechercher par ID
    @Transactional(readOnly = true)
    public Classe rechercherParId(int id) {

        verifierId(id);

        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Classe introuvable avec l'identifiant " + id));
    }

    // Rechercher par nom
    @Transactional(readOnly = true)
    public Classe rechercherParNom(String nom) {

        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom obligatoire.");
        }

        return repository.findByNom(nom.trim())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Classe introuvable : " + nom));
    }

    // Lister toutes les classes
    @Transactional(readOnly = true)
    public List<Classe> lister() {

        return repository.findAll();
    }

    // Modifier une classe
    public Classe modifier(Classe classe) {

        valider(classe);
        verifierId(classe.getIdClasse());

        Classe classeExistante = repository.findById(classe.getIdClasse())
                .orElseThrow(() ->
                        new IllegalArgumentException("Classe introuvable."));

        String nouveauNom = classe.getNom().trim();

        if (!classeExistante.getNom().equals(nouveauNom)
                && repository.existsByNom(nouveauNom)) {
            throw new IllegalArgumentException(
                    "Ce nom de classe est déjà utilisé.");
        }

        classeExistante.setNom(nouveauNom);

        return repository.save(classeExistante);
    }

    // Supprimer une classe
    public void supprimer(int id) {

        verifierId(id);

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Classe introuvable.");
        }

        repository.deleteById(id);
    }

    // Validation
    private void valider(Classe classe) {

        if (classe == null
                || classe.getNom() == null
                || classe.getNom().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La classe et son nom sont obligatoires.");
        }

        if (classe.getNom().trim().length() > 15) {
            throw new IllegalArgumentException(
                    "Le nom ne doit pas dépasser 15 caractères.");
        }
    }

    private void verifierId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant doit être positif.");
        }
    }
}