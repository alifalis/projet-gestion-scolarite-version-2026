package servlet.admin.inscriptions;

import controller.CoursController;
import controller.EtudiantController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Cours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@WebServlet(name = "RechercherCoursServlet", value = "/rechercherCours")
public class RechercherCoursServlet extends HttpServlet {
    private transient CoursController coursController;
    private transient EtudiantController etudiantController;

    @Override
    public void init() throws ServletException {
        coursController = new CoursController();
        etudiantController = new EtudiantController();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String recherche = request.getParameter("searchCours");
        if (recherche == null || recherche.isBlank()) {
            request.setAttribute(
                    "message", "Veuillez saisir un critère de recherche.");
            request.getRequestDispatcher("/listeInscriptions")
                    .forward(request, response);
            return;
        }

        String idEtudiantTexte = request.getParameter("id_etudiant");
        if (idEtudiantTexte != null && !idEtudiantTexte.isBlank()) {
            try {
                request.setAttribute("etudiantSelectionne",
                        etudiantController.rechercherEtudiant(
                                Integer.parseInt(idEtudiantTexte)));
            } catch (IllegalArgumentException e) {
                request.setAttribute("message", "Identifiant étudiant invalide.");
            }
        }

        List<Cours> coursTrouves = new ArrayList<>();
        String critere = recherche.trim();
        Integer idRecherche = null;
        try {
            idRecherche = Integer.valueOf(critere);
        } catch (NumberFormatException ignored) {
            // Une recherche textuelle peut aussi correspondre à un nom.
        }

        for (Cours cours : coursController.afficherCours()) {
            if ((idRecherche != null && cours.getIdCours() == idRecherche)
                    || (cours.getNomCours() != null && cours.getNomCours()
                            .toLowerCase(Locale.ROOT)
                            .contains(critere.toLowerCase(Locale.ROOT)))) {
                coursTrouves.add(cours);
            }
        }

        if (coursTrouves.isEmpty()) {
            request.setAttribute(
                    "message", "Aucun cours correspondant n'a été trouvé.");
        } else {
            request.setAttribute("cours", coursTrouves);
        }
        request.getRequestDispatcher("/listeInscriptions")
                .forward(request, response);
    }
}
