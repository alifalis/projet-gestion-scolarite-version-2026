package service;

import application.SpringContext;
import dao.SpecialiteRepository;
import modele.Specialite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Transactional
public class SpecialiteService {

    private final SpecialiteRepository repository;

    public SpecialiteService() {
        this(SpringContext.getBean(SpecialiteRepository.class));
    }

    @Autowired
    public SpecialiteService(SpecialiteRepository repository) {
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

    public Specialite rechercherParNom(String nom) {
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
