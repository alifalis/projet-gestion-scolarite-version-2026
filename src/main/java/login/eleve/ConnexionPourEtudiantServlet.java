package login.eleve;

import controller.EtudiantController;
import controller.InscriptionCoursController;
import controller.ResultatController;
import controller.UtilisateurController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.Etudiant;
import modele.InscriptionCours;
import modele.Resultat;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ConnexionPourEtudiantServlet", value = "/loginController")
public class ConnexionPourEtudiantServlet extends HttpServlet {

    private static final String LOGIN_VIEW = "/login/etudiant/login.jsp";
    private static final String NOTES_VIEW = "/etudiant/notes.jsp";
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private transient EtudiantController etudiantController;
    private transient InscriptionCoursController inscriptionCoursController;
    private transient ResultatController resultatController;
    private transient UtilisateurController utilisateurController;

    @Override
    public void init() throws ServletException {
        etudiantController = new EtudiantController();
        inscriptionCoursController = new InscriptionCoursController();
        resultatController = new ResultatController();
        utilisateurController = new UtilisateurController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email_etudiant");
        String motDePasseSaisi = request.getParameter("mot_de_passe");

        if (email == null || motDePasseSaisi == null
                || email.isBlank() || motDePasseSaisi.isEmpty()) {
            afficherErreur(request, response,
                    "L'adresse e-mail et le mot de passe sont obligatoires.");
            return;
        }

        try {
            Etudiant etudiant =
                    etudiantController.rechercherEtudiant(email.trim());
            if (etudiant == null || etudiant.getUtilisateur() == null
                || !verifierMotDePasse(
                        motDePasseSaisi,
                        etudiant.getUtilisateur().getMotDePasse(),
                        etudiant)) {
                afficherErreur(request, response,
                        "Adresse e-mail ou mot de passe incorrect.");
                return;
            }

            List<Resultat> resultats = resultatController
                    .afficherResultatsParEtudiant(etudiant.getIdEtudiant());
            List<InscriptionCours> inscriptions = inscriptionCoursController
                    .afficherInscriptions(etudiant.getIdEtudiant());

            HttpSession ancienneSession = request.getSession(false);
            if (ancienneSession != null) {
                ancienneSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("user", "etudiant");
            session.setAttribute(
                    "id_etudiant", etudiant.getIdEtudiant());
            request.setAttribute("nomEtudiant",
                    etudiant.getUtilisateur().getPrenom() + " "
                            + etudiant.getUtilisateur().getNom());
            request.setAttribute("resultats", resultats);
            request.setAttribute("calendrier", creerCalendrier(inscriptions));
            request.setAttribute("moyenne", calculerMoyenne(resultats));
            request.getRequestDispatcher(NOTES_VIEW).forward(request, response);
        } catch (RuntimeException e) {
            getServletContext().log(
                    "Erreur lors de la connexion de l'étudiant.", e);
            afficherErreur(request, response,
                    "La connexion est momentanément indisponible.");
        }
    }

    private boolean verifierMotDePasse(
            String saisi,
            String hash,
            Etudiant etudiant
    ) {
        if (hash == null) {
            return false;
        }
        if (!hash.startsWith("$2")) {
            if (!hash.equals(saisi)) {
                return false;
            }
            etudiant.getUtilisateur().setMotDePasse(
                    BCrypt.hashpw(saisi, BCrypt.gensalt())
            );
            utilisateurController.modifierUtilisateur(
                    etudiant.getUtilisateur());
            return true;
        }
        try {
            return BCrypt.checkpw(saisi, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private BigDecimal calculerMoyenne(List<Resultat> resultats) {
        BigDecimal total = BigDecimal.ZERO;
        int nombreNotes = 0;
        for (Resultat resultat : resultats) {
            if (resultat.getNote() != null) {
                total = total.add(resultat.getNote());
                nombreNotes++;
            }
        }
        return nombreNotes == 0 ? null
                : total.divide(BigDecimal.valueOf(nombreNotes),
                        2, RoundingMode.HALF_UP);
    }

    private List<Map<String, String>> creerCalendrier(
            List<InscriptionCours> inscriptions
    ) {
        List<Map<String, String>> calendrier = new ArrayList<>();
        for (InscriptionCours inscription : inscriptions) {
            Map<String, String> cours = new HashMap<>();
            cours.put("debut", formater(inscription.getDebutCours()));
            cours.put("fin", formater(inscription.getFinCours()));
            cours.put("nom", inscription.getCours() == null
                    ? "Cours" : inscription.getCours().getNomCours());
            calendrier.add(cours);
        }
        return calendrier;
    }

    private String formater(LocalDateTime date) {
        return date == null ? "" : date.format(DATE_FORMATTER);
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
