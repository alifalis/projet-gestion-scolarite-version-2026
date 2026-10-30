package servlet.login.prof;

import controller.EnseignantController;
import controller.UtilisateurController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.Enseignant;
import modele.Role;
import modele.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet(name = "ConnexionProfServlet", urlPatterns = "/connexionProf")
public class ConnexionProfServlet extends HttpServlet {

    private static final String LOGIN_VIEW =
            "/login/prof/connexion_prof.jsp";
    private static final String RESULTS_URL = "/listeResultats";

    private transient EnseignantController enseignantController;
    private transient UtilisateurController utilisateurController;

    @Override
    public void init() throws ServletException {
        enseignantController = new EnseignantController();
        utilisateurController = new UtilisateurController();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email_enseignant");
        String motDePasse = request.getParameter("mot_de_passe");

        if (email == null || email.isBlank()
                || motDePasse == null || motDePasse.isEmpty()) {
            afficherErreur(request, response,
                    "L'adresse e-mail et le mot de passe sont obligatoires.");
            return;
        }

        try {
            Enseignant enseignant =
                    enseignantController.rechercherEnseignant(email.trim());
            if (enseignant == null || enseignant.getUtilisateur() == null
                    || !estEnseignant(enseignant.getUtilisateur())
                    || !verifierMotDePasse(motDePasse, enseignant.getUtilisateur())) {
                afficherErreur(request, response,
                        "Adresse e-mail ou mot de passe incorrect.");
                return;
            }

            HttpSession ancienneSession = request.getSession(false);
            if (ancienneSession != null) {
                ancienneSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("user", "prof");
            session.setAttribute(
                    "id_professeur", enseignant.getIdEnseignant());
            response.sendRedirect(request.getContextPath() + RESULTS_URL);
        } catch (RuntimeException e) {
            getServletContext().log(
                    "Erreur lors de la connexion de l'enseignant.", e);
            afficherErreur(request, response,
                    "La connexion est momentanément indisponible.");
        }
    }

    private boolean estEnseignant(Utilisateur utilisateur) {
        Role role = utilisateur.getRole();
        return role != null
                && "Enseignant".equalsIgnoreCase(role.getNomRole());
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
        request.setAttribute("error", message);
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }
}
