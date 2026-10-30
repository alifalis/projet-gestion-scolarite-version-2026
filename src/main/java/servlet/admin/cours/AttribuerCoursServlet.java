package servlet.admin.cours;

import controller.CoursController;
import controller.EnseignantController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Cours;
import modele.Enseignant;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AttribuerCoursServlet", value = "/attribuerCours")
public class AttribuerCoursServlet extends HttpServlet {

    private static final String LIST_URL = "/listeCours";
    private transient CoursController coursController;
    private transient EnseignantController enseignantController;

    @Override
    public void init() throws ServletException {
        coursController = new CoursController();
        enseignantController = new EnseignantController();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String idProfesseurStr = request.getParameter("id_professeur");
        String nomCours = request.getParameter("nom_cours");
        String description = request.getParameter("description");

        try {
            if (idProfesseurStr == null || nomCours == null
                    || nomCours.isBlank() || description == null
                    || description.isBlank()) {
                throw new IllegalArgumentException(
                        "Le professeur, le nom et la description sont obligatoires.");
            }

            int idProfesseur = Integer.parseInt(idProfesseurStr);
            Enseignant enseignant = enseignantController
                    .rechercherEnseignant(idProfesseur);
            if (enseignant == null) {
                throw new IllegalArgumentException(
                        "Le professeur sélectionné n'existe plus.");
            }
            List<Cours> coursExistants = coursController.afficherCours();
            boolean dejaAttribue = coursExistants.stream().anyMatch(cours ->
                    cours.getEnseignant() != null
                            && cours.getEnseignant().getIdEnseignant()
                                    == idProfesseur
                            && cours.getNomCours().equalsIgnoreCase(
                                    nomCours.trim()));
            if (dejaAttribue) {
                throw new IllegalArgumentException(
                        "Ce professeur enseigne déjà ce cours.");
            }

            Cours cours = new Cours();
            cours.setNomCours(nomCours.trim());
            cours.setDescription(description.trim());
            cours.setEnseignant(enseignant);
            coursController.ajouterCours(cours);
            response.sendRedirect(
                    request.getContextPath() + LIST_URL + "?success=true");
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher(LIST_URL).forward(request, response);
        } catch (RuntimeException e) {
            getServletContext().log("Erreur lors de l'attribution du cours.", e);
            request.setAttribute("message", "Impossible d'attribuer ce cours.");
            request.getRequestDispatcher(LIST_URL).forward(request, response);
        }
    }
}
