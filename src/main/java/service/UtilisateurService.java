
package service;

import dao.UtilisateurRepository;
import modele.Utilisateur;
import java.util.List;

public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

        public UtilisateurService() {
                this.utilisateurRepository = new UtilisateurRepository();
    }

    /**
     * Ajouter un utilisateur.
     */
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        verifierUtilisateur(utilisateur);

        String email = utilisateur.getEmail().trim();

        // Vérifier si l'email est déjà utilisé.
        Utilisateur existant =
                utilisateurRepository.getByEmail(email);

        if (existant != null) {
            throw new IllegalArgumentException(
                    "Cette adresse email est déjà utilisée."
            );
        }

        utilisateur.setEmail(email);

        utilisateurRepository.save(utilisateur);
        return utilisateur;
    }

     /**
     * Retourner la liste d'utilisateur.
     */
    public List<Utilisateur> afficher() {
         
        return utilisateurRepository.getAll();
        
        }       

    /**
     * Rechercher un utilisateur par son identifiant.
     */
    public Utilisateur rechercherParId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant doit être positif."
            );
        }

        Utilisateur utilisateur =
                utilisateurRepository.getById(id);

        if (utilisateur == null) {
            throw new IllegalArgumentException(
                    "Aucun utilisateur trouvé avec cet identifiant."
            );
        }

        return utilisateur;
    }

    /**
     * Rechercher un utilisateur par son email.
     */
    public Utilisateur rechercherParEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "L'adresse email est obligatoire."
            );
        }

        return utilisateurRepository.getByEmail(
                email.trim()
        );
    }

    /**
     * Modifier un utilisateur existant.
     */
    public Utilisateur modifierUtilisateur(Utilisateur utilisateur) {
        verifierUtilisateur(utilisateur);

        if (utilisateur.getIdUtilisateur() <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant de l'utilisateur est invalide."
            );
        }

        Utilisateur existant =
                utilisateurRepository.getById(
                        utilisateur.getIdUtilisateur()
                );

        if (existant == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur à modifier n'existe pas."
            );
        }

        String email = utilisateur.getEmail().trim();

        // Vérifier que l'email n'appartient pas à un autre utilisateur.
        Utilisateur utilisateurEmail =
                utilisateurRepository.getByEmail(email);

        if (utilisateurEmail != null
                && utilisateurEmail.getIdUtilisateur()
                != utilisateur.getIdUtilisateur()) {
            throw new IllegalArgumentException(
                    "Cette adresse email est déjà utilisée."
            );
        }

        utilisateur.setEmail(email);

        utilisateurRepository.update(utilisateur);
        return utilisateur;
    }

    /**
     * Supprimer un utilisateur par son identifiant.
     */
    public void supprimerUtilisateur(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant doit être positif."
            );
        }

        Utilisateur utilisateur =
                utilisateurRepository.getById(id);

        if (utilisateur == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur à supprimer n'existe pas."
            );
        }

        utilisateurRepository.deleteById(id);
    }

    /**
     * Vérifier les informations obligatoires.
     */
    private void verifierUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur ne doit pas être null."
            );
        }

        if (utilisateur.getNom() == null
                || utilisateur.getNom().isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom est obligatoire."
            );
        }

        if (utilisateur.getPrenom() == null
                || utilisateur.getPrenom().isBlank()) {
            throw new IllegalArgumentException(
                    "Le prénom est obligatoire."
            );
        }

        if (utilisateur.getEmail() == null
                || utilisateur.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "L'adresse email est obligatoire."
            );
        }

        String email = utilisateur.getEmail().trim();

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "L'adresse email n'est pas valide."
            );
        }

        if (utilisateur.getMotDePasse() == null
                || utilisateur.getMotDePasse().isBlank()) {
            throw new IllegalArgumentException(
                    "Le mot de passe est obligatoire."
            );
        }
    }
}
