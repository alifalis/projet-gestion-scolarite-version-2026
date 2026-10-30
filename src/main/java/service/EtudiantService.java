package service;

import dao.EtudiantRepository;
import modele.Etudiant;

import java.util.List;

public class EtudiantService {

    private final EtudiantRepository repository;

    public EtudiantService() {
        this.repository = new EtudiantRepository();
    }

    public Etudiant ajouter(Etudiant etudiant) {
        valider(etudiant);
        String email = etudiant.getUtilisateur().getEmail();
        if (repository.getByEmail(email) != null)
            throw new IllegalArgumentException("Cette adresse e-mail est déjà utilisée.");
        repository.save(etudiant);
        return etudiant;
    }

    public Etudiant rechercherParId(int id) {
        verifierId(id);
        return repository.getById(id);
    }

    public Etudiant rechercherParEmail(String email) {
        verifierEmail(email);
        return repository.getByEmail(email.trim());
    }

    public List<Etudiant> lister() {
        return repository.getAll();
    }

    public Etudiant modifier(Etudiant etudiant) {
        valider(etudiant);
        repository.update(etudiant);
        return etudiant;
    }

    public void supprimer(int id) {
        verifierId(id);
        repository.deleteById(id);
    }

    private void valider(Etudiant e) {
        if (e == null) {
            throw new IllegalArgumentException("Étudiant obligatoire.");
        }
        if (e.getUtilisateur() == null) {
            throw new IllegalArgumentException("Utilisateur associé obligatoire.");
        }
        verifierEmail(e.getUtilisateur().getEmail());
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
