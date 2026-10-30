package servlet.telechargement.csv;

import controller.EnseignantController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.Enseignant;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "TelechargerProfesseursCSVServlet", value = "/telechargerProfesseursCSV")
public class TelechargerProfesseursCSVServlet extends HttpServlet {

    private transient EnseignantController enseignantController;

    @Override
    public void init() throws ServletException {
        enseignantController = new EnseignantController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("user"))) {
            response.sendRedirect(request.getContextPath() + "/connexionAdmin");
            return;
        }

        List<Enseignant> professeurs = enseignantController.afficherEnseignants();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=professeurs.csv");

        try (PrintWriter writer = response.getWriter()) {
            writer.write('\uFEFF');
            writer.println("ID,Nom,Prénom,Email,Spécialité");

            for (Enseignant professeur : professeurs) {
                if (professeur == null || professeur.getUtilisateur() == null) {
                    continue;
                }
                writer.println(String.join(",",
                        csvValue(String.valueOf(professeur.getIdEnseignant())),
                        csvValue(professeur.getUtilisateur().getNom()),
                        csvValue(professeur.getUtilisateur().getPrenom()),
                        csvValue(professeur.getUtilisateur().getEmail()),
                        csvValue(professeur.getSpecialite() == null
                            ? "N/A" : professeur.getSpecialite().getNom())));
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
