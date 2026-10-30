package admin.inscriptions;

import controller.InscriptionCoursController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.InscriptionCours;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListeInscriptionsServlet", value = "/listeInscriptions")
public class ListeInscriptionsCoursServlet extends HttpServlet {

    private transient InscriptionCoursController inscriptionController;

    @Override
    public void init() throws ServletException {
        inscriptionController = new InscriptionCoursController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<InscriptionCours> inscriptions =
                inscriptionController.afficherToutesInscriptions();
        request.setAttribute("inscriptions", inscriptions);
        request.getRequestDispatcher(
                "/admin/inscriptions/gestion.jsp")
                .forward(request, response);
    }

}
