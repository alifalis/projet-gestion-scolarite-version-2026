package servlet.telechargement.csv;

import controller.EtudiantController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.Etudiant;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "TelechargerEtudiantsCSVServlet", value = "/telechargerEtudiantsCSV")
public class TelechargerEtudiantsCSVServlet extends HttpServlet {

    private transient EtudiantController etudiantController;

    @Override
    public void init() throws ServletException {
        etudiantController = new EtudiantController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("user"))) {
            response.sendRedirect(request.getContextPath() + "/connexionAdmin");
            return;
        }

        List<Etudiant> etudiants = etudiantController.afficherEtudiants();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=etudiants.csv");

        try (PrintWriter writer = response.getWriter()) {
            writer.write('\uFEFF');
            writer.println("ID,Nom,Prénom,Email,Contact,Classe,Filière");

            for (Etudiant etudiant : etudiants) {
                if (etudiant == null || etudiant.getUtilisateur() == null) {
                    continue;
                }
                writer.println(String.join(",",
                        csvValue(String.valueOf(etudiant.getIdEtudiant())),
                        csvValue(etudiant.getUtilisateur().getNom()),
                        csvValue(etudiant.getUtilisateur().getPrenom()),
                        csvValue(etudiant.getUtilisateur().getEmail()),
                        csvValue(etudiant.getContact()),
                        csvValue(etudiant.getClasse() == null
                                ? "N/A" : etudiant.getClasse().getNom()),
                        csvValue(etudiant.getFiliere())));
            }
        }
    }

    private String csvValue(String value) {
        String safeValue = value == null ? "" : value;
        String trimmedValue = safeValue.stripLeading();
        if (!trimmedValue.isEmpty()
                && "=+-@".indexOf(trimmedValue.charAt(0)) >= 0) {
            safeValue = "'" + safeValue;
        }
        return "\"" + safeValue.replace("\"", "\"\"") + "\"";
    }
}
