package servlet.admin.etudiants;



import controller.EtudiantController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import modele.Etudiant;

@WebServlet(name = "ListeEtudiantsServlet", urlPatterns = "/listeEtudiants")
public class ListeEtudiantsServlet extends HttpServlet {

    private transient EtudiantController etudiantController;

    @Override
    public void init() throws ServletException {
        etudiantController = new EtudiantController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Etudiant> etudiants = etudiantController.afficherEtudiants();
        String query = request.getParameter("searchQuery");
        if (query != null && !query.isBlank()) {
            String critere = query.trim().toLowerCase(Locale.ROOT);
            etudiants = etudiants.stream()
                    .filter(etudiant -> etudiant.getUtilisateur() != null
                            && (contient(etudiant.getUtilisateur().getNom(), critere)
                                || contient(etudiant.getUtilisateur().getPrenom(), critere)
                                || contient(etudiant.getUtilisateur().getAdresse(), critere)
                                || contient(etudiant.getUtilisateur().getEmail(), critere)
                                || contient(etudiant.getClasse() == null
                                        ? null : etudiant.getClasse().getNom(), critere)
                                || contient(etudiant.getFiliere(), critere)))
                    .toList();
        }
        request.setAttribute(
                "etudiantList", etudiants);
        request.getRequestDispatcher("/admin/etudiants/liste.jsp")
            .forward(request, response);
    }

    private boolean contient(String valeur, String critere) {
        return valeur != null && valeur.toLowerCase(Locale.ROOT).contains(critere);
    }

}

