package admin.enseignants;

import controller.EnseignantController;
import modele.Enseignant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;
import java.util.List;

@WebServlet(name = "ListeProfesseursServlet", urlPatterns = "/listeProfesseurs")
public class ListeProfesseursServlet extends HttpServlet {

    private transient EnseignantController enseignantController;

    @Override
    public void init() throws ServletException {
        enseignantController = new EnseignantController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Enseignant> professeurs = enseignantController.afficherEnseignants();
        String query = request.getParameter("searchQuery");
        if (query != null && !query.isBlank()) {
            String critere = query.trim().toLowerCase(Locale.ROOT);
            professeurs = professeurs.stream()
                    .filter(professeur -> professeur.getUtilisateur() != null
                            && (contient(professeur.getUtilisateur().getNom(), critere)
                                || contient(professeur.getUtilisateur().getPrenom(), critere)
                                || contient(professeur.getUtilisateur().getAdresse(), critere)
                                || contient(professeur.getUtilisateur().getEmail(), critere)
                                || (professeur.getSpecialite() != null
                                    && contient(professeur.getSpecialite().getNom(), critere))))
                    .toList();
        }
        request.setAttribute("professeurs", professeurs);
        request.getRequestDispatcher("/admin/enseignants/liste.jsp")
            .forward(request, response);
    }

    private boolean contient(String valeur, String critere) {
        return valeur != null && valeur.toLowerCase(Locale.ROOT).contains(critere);
    }
}
