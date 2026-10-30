package admin;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/admin/*",
        "/listeEtudiants",
        "/supprimerEtudiant",
        "/listeProfesseurs",
        "/supprimerProfesseur",
        "/listeCours",
        "/attribuerCours",
        "/rechercherProfesseur",
        "/listeInscriptions",
        "/rechercherEtudiant",
        "/rechercherCours",
        "/inscrireCours",
        "/telechargerEtudiantsCSV",
        "/telechargerProfesseursCSV"
})
public class AdminAccessFilter implements Filter {

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest servletRequest,
            jakarta.servlet.ServletResponse servletResponse,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        if (session == null || !"admin".equals(session.getAttribute("user"))
                || session.getAttribute("id_admin") == null) {
            response.sendRedirect(request.getContextPath() + "/connexionAdmin");
            return;
        }

        chain.doFilter(request, response);
    }
}
