package servlet.accespageprof;

import controller.ResultatController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.Resultat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ModifierNotesServlet", value = "/modifierNotes")
public class ModifierNotesServlet extends HttpServlet {

    private static final String RESULTS_VIEW = "/prof/gestion_notes.jsp";
    private static final String LOGIN_URL = "/connexionProf";
    private transient ResultatController resultatController;

    @Override
    public void init() throws ServletException {
        resultatController = new ResultatController();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object idSession = session == null
                ? null : session.getAttribute("id_professeur");
        if (session == null || !"prof".equals(session.getAttribute("user"))
                || !(idSession instanceof Integer idProfesseur)) {
            response.sendRedirect(request.getContextPath() + LOGIN_URL);
            return;
        }

        List<Resultat> resultats = resultatController
                .afficherResultatsParEnseignant(idProfesseur);
        Map<Integer, Resultat> resultatsParId = new HashMap<>();
        for (Resultat resultat : resultats) {
            resultatsParId.put(resultat.getIdResultat(), resultat);
        }

        String[] idResultats = request.getParameterValues("id_resultat");
        Map<Integer, BigDecimal> notesAEnregistrer = new HashMap<>();
        String messageErreur = null;

        if (idResultats == null) {
            messageErreur = "Aucun résultat n'a été transmis.";
        } else {
            for (String idTexte : idResultats) {
                try {
                    int idResultat = Integer.parseInt(idTexte);
                    Resultat resultat = resultatsParId.get(idResultat);
                    if (resultat == null) {
                        messageErreur = "Un résultat transmis est invalide.";
                        break;
                    }

                    String valeur = request.getParameter("note_" + idResultat);
                    if (valeur == null || valeur.isBlank()) {
                        continue;
                    }

                    BigDecimal note = new BigDecimal(valeur.trim());
                    if (note.compareTo(BigDecimal.ZERO) < 0
                            || note.compareTo(BigDecimal.valueOf(20)) > 0) {
                        messageErreur =
                                "Toutes les notes doivent être comprises entre 0 et 20.";
                        break;
                    }
                    notesAEnregistrer.put(idResultat, note);
                } catch (NumberFormatException e) {
                    messageErreur = "Une note ou un identifiant est invalide.";
                    break;
                }
            }
        }

        if (messageErreur == null) {
            try {
                for (Map.Entry<Integer, BigDecimal> entree
                        : notesAEnregistrer.entrySet()) {
                    Resultat resultat = resultatsParId.get(entree.getKey());
                    resultat.setNote(entree.getValue());
                    resultatController.modifierResultat(resultat);
                }
                request.setAttribute("message", "Les notes sont enregistrées.");
            } catch (RuntimeException e) {
                getServletContext().log(
                        "Erreur lors de l'enregistrement des résultats.", e);
                request.setAttribute(
                        "errorMessage", "Impossible d'enregistrer les notes.");
            }
        } else {
            request.setAttribute("errorMessage", messageErreur);
        }

        try {
            resultats = resultatController
                    .afficherResultatsParEnseignant(idProfesseur);
        } catch (RuntimeException e) {
            getServletContext().log(
                    "Erreur lors du rechargement des résultats.", e);
            resultats = new ArrayList<>();
            request.setAttribute(
                    "errorMessage", "Impossible de recharger les résultats.");
        }
        request.setAttribute("resultats", resultats);
        request.getRequestDispatcher(RESULTS_VIEW).forward(request, response);
    }
}
