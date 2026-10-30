package servlet.admin.cours;

import controller.CoursController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Cours;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListeCoursServlet", value = "/listeCours")
public class ListeCoursServlet extends HttpServlet {

    private transient CoursController coursController;

    @Override
    public void init() throws ServletException {
        coursController = new CoursController();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        List<Cours> cours = coursController.afficherCours();
        request.setAttribute("cours", cours);
        if ("true".equals(request.getParameter("success"))) {
            request.setAttribute("message", "Le cours a été attribué.");
        }
        request.getRequestDispatcher(
                "/admin/cours/attribution.jsp")
                .forward(request, response);
    }

}
