package service;

import application.SpringContext;
import dao.InscriptionAnneeRepository;
import modele.InscriptionAnnee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Transactional
public class InscriptionAnneeService {

    private final InscriptionAnneeRepository repository;

    public InscriptionAnneeService() {
        this(SpringContext.getBean(InscriptionAnneeRepository.class));
    }

    @Autowired
    public InscriptionAnneeService(InscriptionAnneeRepository repository) {
        this.repository = repository;
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

    public InscriptionAnnee rechercherParEtudiantEtAnnee(
            int idEtudiant,
            String anneeScolaire
    ) {
        verifierId(idEtudiant);
        if (anneeScolaire == null || !anneeScolaire.matches("^\\d{4}-\\d{4}$")) {
            throw new IllegalArgumentException(
                    "Format d'année scolaire attendu : AAAA-AAAA."
            );
        }
        return repository.getByEtudiantAndAnnee(idEtudiant, anneeScolaire);
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
