package controller.web;

import jakarta.servlet.http.HttpServletRequest;
import modele.Enseignant;
import modele.Role;
import modele.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.EnseignantService;
import service.UtilisateurService;

@Controller
public class ProfessorLoginController {

    private final EnseignantService enseignants;
    private final UtilisateurService utilisateurs;

    public ProfessorLoginController(
            EnseignantService enseignants, UtilisateurService utilisateurs) {
        this.enseignants = enseignants;
        this.utilisateurs = utilisateurs;
    }

    @GetMapping("/connexionProf")
    public String page() {
        return "login/prof/connexion_prof";
    }

    @PostMapping("/connexionProf")
    public String connexion(
            @RequestParam(name = "email_enseignant", required = false) String email,
            @RequestParam(name = "mot_de_passe", required = false) String password,
            HttpServletRequest request) {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            return erreur(request, "L'adresse e-mail et le mot de passe sont obligatoires.");
        }
        try {
            Enseignant enseignant = enseignants.rechercherParEmail(email.trim());
            if (enseignant == null || enseignant.getUtilisateur() == null
                    || !professeur(enseignant.getUtilisateur())
                    || !motDePasseValide(password, enseignant.getUtilisateur())) {
                return erreur(request, "Adresse e-mail ou mot de passe incorrect.");
            }
            request.getSession().invalidate();
            var session = request.getSession(true);
            session.setAttribute("user", "prof");
            session.setAttribute("id_professeur", enseignant.getIdEnseignant());
            return "redirect:/listeResultats";
        } catch (RuntimeException exception) {
            return erreur(request, "La connexion est momentanément indisponible.");
        }
    }

    private boolean professeur(Utilisateur utilisateur) {
        Role role = utilisateur.getRole();
        return role != null && "Enseignant".equalsIgnoreCase(role.getNomRole());
    }

    private boolean motDePasseValide(String password, Utilisateur utilisateur) {
        String stored = utilisateur.getMotDePasse();
        if (stored == null) return false;
        if (stored.startsWith("$2")) return BCrypt.checkpw(password, stored);
        if (!stored.equals(password)) return false;
        utilisateur.setMotDePasse(BCrypt.hashpw(password, BCrypt.gensalt()));
        utilisateurs.modifierUtilisateur(utilisateur);
        return true;
    }

    private String erreur(HttpServletRequest request, String message) {
        request.setAttribute("error", message);
        return "login/prof/connexion_prof";
    }
}
