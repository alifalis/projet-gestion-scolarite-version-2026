package accespagetudiant;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import controller.EtudiantController;
import controller.ResultatController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.Etudiant;
import modele.Resultat;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@WebServlet(name = "TelechargerReleveNotesServlet", value = "/telechargerReleveNotes")
public class TelechargerReleveNotesServlet extends HttpServlet {

    private transient EtudiantController etudiantController;
    private transient ResultatController resultatController;

    @Override
    public void init() throws ServletException {
        etudiantController = new EtudiantController();
        resultatController = new ResultatController();
    }

    @Override
        protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
        ) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object idSession = session == null
                ? null : session.getAttribute("id_etudiant");
        if (session == null
            || !"etudiant".equals(session.getAttribute("user"))
                || !(idSession instanceof Integer idEtudiant)) {
            response.sendRedirect(request.getContextPath() + "/loginController");
            return;
        }

        Etudiant etudiant = etudiantController.rechercherEtudiant(idEtudiant);
        if (etudiant == null || etudiant.getUtilisateur() == null) {
            response.sendRedirect(request.getContextPath() + "/loginController");
            return;
        }
        String etudiantFullName = etudiant.getUtilisateur().getPrenom() + " "
            + etudiant.getUtilisateur().getNom();
        List<Resultat> resultats =
                resultatController.afficherResultatsParEtudiant(idEtudiant);

        BigDecimal totalNotes = BigDecimal.ZERO;
        int noteCount = 0;

        for (Resultat resultat : resultats) {
            if (resultat.getNote() != null) {
                totalNotes = totalNotes.add(resultat.getNote());
                noteCount++;
            }
        }

        BigDecimal average = noteCount == 0 ? null
                : totalNotes.divide(BigDecimal.valueOf(noteCount),
                        2, RoundingMode.HALF_UP);

        response.setContentType("application/pdf");
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=releve_notes.pdf"
        );

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            document.add(new Paragraph("Relevé de notes",
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK)));
            document.add(new Paragraph("Nom de l'étudiant : " + etudiantFullName,
                    FontFactory.getFont(FontFactory.HELVETICA, 14)));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell("Nom du cours");
            table.addCell("Note");

            for (Resultat resultat : resultats) {
                table.addCell(resultat.getCours().getNomCours());
                table.addCell(resultat.getNote() == null
                        ? "Non noté" : resultat.getNote().toString() + "/20");
            }

            document.add(table);
            if (average != null) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Moyenne générale : "
                        + average + "/20",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14,
                                BaseColor.BLACK)));
            }

            document.close();
        } catch (DocumentException e) {
            if (document.isOpen()) {
                document.close();
            }
            throw new IOException("Erreur lors de la génération du PDF.", e);
        }
    }
}
