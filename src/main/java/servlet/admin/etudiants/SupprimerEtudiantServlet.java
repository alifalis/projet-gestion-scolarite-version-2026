package servlet.admin.etudiants;


import controller.EtudiantController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;



@WebServlet(name = "SupprimerEtudiantServlet", urlPatterns = "/supprimerEtudiant")
public class SupprimerEtudiantServlet extends HttpServlet {


    private transient EtudiantController etudiantController;

    @Override
    public void init() throws ServletException {
        etudiantController = new EtudiantController();
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idEtudiantParam = request.getParameter("id");
        try {
            int idEtudiant = Integer.parseInt(idEtudiantParam);
            etudiantController.supprimerEtudiant(idEtudiant);
            response.sendRedirect(
                    request.getContextPath() + "/listeEtudiants");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Identifiant étudiant invalide.");
        }
    }
}

