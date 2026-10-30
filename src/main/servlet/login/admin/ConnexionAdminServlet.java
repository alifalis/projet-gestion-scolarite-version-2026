package login.admin;

import controller.UtilisateurController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.Role;
import modele.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet(name = "ConnexionAdminServlet", urlPatterns = "/connexionAdmin")
public class ConnexionAdminServlet extends HttpServlet {

    private static final String LOGIN_VIEW =
            "/login/admin/pagedeconnexionAdmin.jsp";
    private static final String ADMIN_VIEW = "/admin/pageAdmin.jsp";

    private transient UtilisateurController utilisateurController;

    @Override
    public void init() throws ServletException {
        utilisateurController = new UtilisateurController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String emailAdmin = request.getParameter("email_admin");
        String motDePasse = request.getParameter("mot_de_passe");

        if (emailAdmin == null || emailAdmin.isBlank()
                || motDePasse == null || motDePasse.isEmpty()) {
            afficherErreur(request, response,
                    "L'adresse e-mail et le mot de passe sont obligatoires.");
            return;
        }

        try {
            Utilisateur utilisateur = utilisateurController
                    .rechercherParEmail(emailAdmin.trim());
            if (utilisateur == null || !estAdministrateur(utilisateur)
                    || !verifierMotDePasse(motDePasse, utilisateur)) {
                afficherErreur(request, response,
                        "Adresse e-mail ou mot de passe incorrect.");
                return;
            }

            HttpSession ancienneSession = request.getSession(false);
            if (ancienneSession != null) {
                ancienneSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("user", "admin");
            session.setAttribute("id_admin", utilisateur.getIdUtilisateur());
            response.sendRedirect(request.getContextPath() + ADMIN_VIEW);
        } catch (RuntimeException e) {
            getServletContext().log(
                    "Erreur lors de la connexion administrateur.", e);
            afficherErreur(request, response,
                    "La connexion est momentanément indisponible.");
        }
    }

    private boolean estAdministrateur(Utilisateur utilisateur) {
        Role role = utilisateur.getRole();
        return role != null
                && "Administrateur".equalsIgnoreCase(role.getNomRole());
    }

    private boolean verifierMotDePasse(
            String motDePasseSaisi,
            Utilisateur utilisateur
    ) {
        String motDePasseStocke = utilisateur.getMotDePasse();
        if (motDePasseStocke == null) {
            return false;
        }

        if (motDePasseStocke.startsWith("$2")) {
            try {
                return BCrypt.checkpw(motDePasseSaisi, motDePasseStocke);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        if (!motDePasseStocke.equals(motDePasseSaisi)) {
            return false;
        }

        utilisateur.setMotDePasse(
                BCrypt.hashpw(motDePasseSaisi, BCrypt.gensalt())
        );
        utilisateurController.modifierUtilisateur(utilisateur);
        return true;
    }

    private void afficherErreur(
            HttpServletRequest request,
            HttpServletResponse response,
            String message
    ) throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }
}
