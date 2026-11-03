package service;

import application.SpringContext;
import dao.EnseignantRepository;
import modele.Enseignant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Transactional
public class EnseignantService {

    private final EnseignantRepository repository;

    public EnseignantService() {
        this(SpringContext.getBean(EnseignantRepository.class));
    }

    @Autowired
    public EnseignantService(EnseignantRepository repository) {
        this.repository = repository;
    }

    public Enseignant ajouter(Enseignant enseignant) {
        valider(enseignant);
        if (repository.getByEmail(enseignant.getUtilisateur().getEmail()) != null)
            throw new IllegalArgumentException("Cette adresse e-mail est déjà utilisée.");
        repository.save(enseignant);
        return enseignant;
    }

    public Enseignant rechercherParId(int id) {
        verifierId(id);
        return repository.getById(id);
    }

    public Enseignant rechercherParEmail(String email) {
        verifierEmail(email);
        return repository.getByEmail(email.trim());
    }

    public List<Enseignant> lister() {
        return repository.getAll();
    }

    public Enseignant modifier(Enseignant enseignant) {
        valider(enseignant);
        repository.update(enseignant);
        return enseignant;
    }

    public void supprimer(int id) {
        verifierId(id);
        repository.deleteById(id);
    }

    private void valider(Enseignant e) {
        if (e == null) {
            throw new IllegalArgumentException("Enseignant obligatoire.");
        }
        if (e.getUtilisateur() == null) {
            throw new IllegalArgumentException("Utilisateur associé obligatoire.");
        }
        verifierEmail(e.getUtilisateur().getEmail());
        // La spécialité reste facultative si la base autorise NULL.
    }

    private void verifierId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif.");
        }
    }

    private void verifierEmail(String email) {
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Adresse e-mail invalide.");
        }
    }
}
