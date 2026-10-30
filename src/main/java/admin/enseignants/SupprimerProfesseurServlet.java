package admin.enseignants;



import controller.EnseignantController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

@WebServlet(name = "SupprimerProfesseurServlet", urlPatterns = "/supprimerProfesseur")
public class SupprimerProfesseurServlet extends HttpServlet {

    private transient EnseignantController enseignantController;

    @Override
    public void init() throws ServletException {
        enseignantController = new EnseignantController();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idProfesseurParam = request.getParameter("id");
        try {
            int idProfesseur = Integer.parseInt(idProfesseurParam);
            enseignantController.supprimerEnseignant(idProfesseur);
            response.sendRedirect(
                    request.getContextPath() + "/listeProfesseurs");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Identifiant professeur invalide.");
        }
    }
}
