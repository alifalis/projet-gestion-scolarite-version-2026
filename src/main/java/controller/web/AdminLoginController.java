package controller.web;

import modele.Role;
import modele.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.UtilisateurService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminLoginController {

    private final UtilisateurService utilisateurs;

    public AdminLoginController(UtilisateurService utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @GetMapping("/connexionAdmin")
    public String page() {
        return "login/admin/pagedeconnexionAdmin";
    }

    @GetMapping("/admin/pageAdmin.jsp")
    public String tableauDeBord() {
        return "admin/pageAdmin";
    }

    @PostMapping("/connexionAdmin")
    public String connexion(
            @RequestParam(name = "email_admin", required = false) String email,
            @RequestParam(name = "mot_de_passe", required = false) String password,
            HttpServletRequest request) {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            return erreur(request, "L'adresse e-mail et le mot de passe sont obligatoires.");
        }
        try {
            Utilisateur utilisateur = utilisateurs.rechercherParEmail(email.trim());
            if (utilisateur == null || !admin(utilisateur) || !motDePasseValide(password, utilisateur)) {
                return erreur(request, "Adresse e-mail ou mot de passe incorrect.");
            }
            request.getSession().invalidate();
            var session = request.getSession(true);
            session.setAttribute("user", "admin");
            session.setAttribute("id_admin", utilisateur.getIdUtilisateur());
            return "redirect:/admin/pageAdmin.jsp";
        } catch (RuntimeException exception) {
            request.setAttribute("message", "La connexion est momentanément indisponible.");
            return "login/admin/pagedeconnexionAdmin";
        }
    }

    private boolean admin(Utilisateur utilisateur) {
        Role role = utilisateur.getRole();
        return role != null && "Administrateur".equalsIgnoreCase(role.getNomRole());
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
        request.setAttribute("message", message);
        return "login/admin/pagedeconnexionAdmin";
    }
}
