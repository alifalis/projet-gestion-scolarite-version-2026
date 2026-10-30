package servlet.admin.cours;

import controller.EnseignantController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Enseignant;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "RechercherProfesseurServlet", value = "/rechercherProfesseur")
public class RechercherProfesseurServlet extends HttpServlet {

    private transient EnseignantController enseignantController;

    @Override
    public void init() throws ServletException {
        enseignantController = new EnseignantController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");

        if (search != null && !search.trim().isEmpty()) {
            String critere = search.trim().toLowerCase(Locale.ROOT);
            List<Enseignant> professeurs = enseignantController
                    .afficherEnseignants().stream()
                    .filter(professeur -> professeur.getUtilisateur() != null
                            && (contient(professeur.getUtilisateur().getNom(), critere)
                                || contient(professeur.getUtilisateur().getPrenom(), critere)
                                || contient(professeur.getUtilisateur().getEmail(), critere)))
                    .toList();

            if (professeurs.isEmpty()) {
                request.setAttribute("message", "Aucun professeur trouvé pour cette recherche.");
            } else {
                request.setAttribute("professeurs", professeurs);
            }
        } else {
            request.setAttribute("message", "Veuillez entrer un terme de recherche valide.");
        }

        request.getRequestDispatcher("/listeCours")
                .forward(request, response);
    }

    private boolean contient(String valeur, String critere) {
        return valeur != null && valeur.toLowerCase(Locale.ROOT).contains(critere);
    }

}
