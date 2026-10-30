error id: file://<WORKSPACE>/src/main/java/TelechargerReleveNotesServlet.java:modele/Note#
file://<WORKSPACE>/src/main/java/TelechargerReleveNotesServlet.java
empty definition using pc, found symbol in pc: modele/Note#
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 367
uri: file://<WORKSPACE>/src/main/java/TelechargerReleveNotesServlet.java
text:
```scala
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import dao.NoteRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.@@Note;
import modele.Etudiant;
import dao.EtudiantRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "TelechargerReleveNotesServlet", value = "/telechargerReleveNotes")
public class TelechargerReleveNotesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer idEtudiant = Integer.valueOf(session.getAttribute("id_etudiant").toString());

        Etudiant et= new EtudiantRepository().getEtudiantById(Integer.valueOf(session.getAttribute("id_etudiant").toString()));
        String etudiantFullName = et.getUtilisateur().getNom()+" "+et.getUtilisateur().getPrenom();
        // Si le nom n'existe pas dans la session, afficher un message par défaut
        if (etudiantFullName == null || etudiantFullName.isEmpty()) {
            etudiantFullName = "étudiant"; // Valeur par défaut
        }


        if (idEtudiant == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        NoteRepository noteRepository = new NoteRepository();
        List<Note> notes = noteRepository.getNotesByEtudiant(idEtudiant);

        BigDecimal totalNotes = BigDecimal.ZERO;
        int noteCount = 0;

        for (Note note : notes) {
            totalNotes = totalNotes.add(note.getNote());
            noteCount++;
        }

        BigDecimal average = (noteCount > 0) ? totalNotes.divide(BigDecimal.valueOf(noteCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=releve_notes.pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            // Titre

            document.add(new Paragraph("Relevé de Notes", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK)));
            document.add(new Paragraph("Nom de l'étudiant : " + etudiantFullName, FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK)));
            document.add(new Paragraph(" ")); // Espace

            // Tableau des notes
            PdfPTable table = new PdfPTable(2); // Deux colonnes : Nom du cours, Note
            table.setWidthPercentage(100);
            table.addCell(new PdfPCell(new Phrase("Nom du Cours", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
            table.addCell(new PdfPCell(new Phrase("Note", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

            for (Note note : notes) {
                table.addCell(note.getCours().getNomCours());
                table.addCell(note.getNote().toString());
            }

            document.add(table);

            // Moyenne
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Moyenne générale : " + average.toString() + "/20", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));

            document.close();
        } catch (DocumentException e) {
            throw new IOException("Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: modele/Note#