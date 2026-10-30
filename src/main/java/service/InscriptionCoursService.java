package service;

import dao.InscriptionCoursRepository;
import modele.InscriptionCours;

import java.time.LocalDateTime;
import java.util.List;

public class InscriptionCoursService {

    private final InscriptionCoursRepository repository;

    public InscriptionCoursService() {
        this.repository = new InscriptionCoursRepository();
    }

    public InscriptionCours ajouter(InscriptionCours inscription) {
        valider(inscription);
        repository.save(inscription);
        return inscription;
    }

    public InscriptionCours rechercherParId(int id) {
        verifierId(id);
        return repository.getById(id);
    }

    public List<InscriptionCours> lister() {
        return repository.getAll();
    }

    public List<InscriptionCours> rechercherParEtudiant(int idEtudiant) {
        verifierId(idEtudiant);
        return repository.getByEtudiant(idEtudiant);
    }

    public List<Integer> listeEtudiantsInscritAuCourt(int idCours) {
        verifierId(idCours);
        return repository.getIdEtudiantsByCours(idCours);
    }

    public InscriptionCours modifier(InscriptionCours inscription) {
        valider(inscription);
        repository.update(inscription);
        return inscription;
    }

    public void supprimer(int id) {
        verifierId(id);
        repository.deleteById(id);
    }

    private void valider(InscriptionCours i) {
        if (i == null) {
            throw new IllegalArgumentException("Inscription obligatoire.");
        }
        if (i.getEtudiant() == null) {
            throw new IllegalArgumentException("Étudiant obligatoire.");
        }
        if (i.getCours() == null) {
            throw new IllegalArgumentException("Cours obligatoire.");
        }

        LocalDateTime debut = i.getDebutCours();
        LocalDateTime fin = i.getFinCours();
        if (debut != null && fin != null && fin.isBefore(debut)) {
            throw new IllegalArgumentException("La fin ne peut pas précéder le début.");
        }
    }

    private void verifierId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif.");
        }
    }
}
