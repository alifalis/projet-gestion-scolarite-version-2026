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
import java.util.List;

@WebServlet(name = "ListeResultatsServlet", value = "/listeResultats")
public class ListeResultatsServlet extends HttpServlet {

    private static final String LOGIN_URL = "/connexionProf";
    private static final String RESULTS_VIEW = "/prof/gestion_notes.jsp";
    private transient ResultatController resultatController;

    @Override
    public void init() throws ServletException {
        resultatController = new ResultatController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        request.setAttribute("resultats", resultats);
        request.getRequestDispatcher(RESULTS_VIEW).forward(request, response);
    }
}
