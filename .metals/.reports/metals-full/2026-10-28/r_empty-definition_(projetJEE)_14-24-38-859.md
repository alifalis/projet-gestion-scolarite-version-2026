error id: file://<WORKSPACE>/src/main/java/admin/attribution_cours/poureleve/InscrireCoursServlet.java:dao/NoteRepository#
file://<WORKSPACE>/src/main/java/admin/attribution_cours/poureleve/InscrireCoursServlet.java
empty definition using pc, found symbol in pc: dao/NoteRepository#
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 181
uri: file://<WORKSPACE>/src/main/java/admin/attribution_cours/poureleve/InscrireCoursServlet.java
text:
```scala
package admin.attribution_cours.poureleve;

import admin.ExecuteSchema;
import dao.CoursRepository;
import dao.EtudiantRepository;
import dao.InscriptionCoursRepository;
import dao.@@NoteRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Cours;
import modele.Etudiant;
import modele.InscriptionCours;
import modele.Note;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet(name = "InscrireCoursServlet", value = "/inscrireCours")
public class InscrireCoursServlet extends HttpServlet {

    private static final String DB_URL = ExecuteSchema.getDbUrl() + "/jee_project";
    private static final String DB_USER = ExecuteSchema.getDbUser(); // Modifier si nécessaire
    private static final String DB_PASSWORD = ExecuteSchema.getDbPassword(); // Modifier si nécessaire

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Retrieve parameters
            String idEtudiantStr = request.getParameter("id_etudiant");
            String idCoursStr = request.getParameter("id_cours");
            String dateCours = request.getParameter("dateCours");
            String debutHeure = request.getParameter("debutHeure");
            String finHeure = request.getParameter("finHeure");

            // Validate parameters
            if (idEtudiantStr == null || idCoursStr == null || dateCours == null || debutHeure == null || finHeure == null) {
                request.setAttribute("message", "Tous les champs sont obligatoires.");
                request.getRequestDispatcher("listeInscriptions").forward(request, response);
                return; // Stop execution
            }

            // Combine date and time
            String debutCoursStr = dateCours + "T" + debutHeure;
            String finCoursStr = dateCours + "T" + finHeure;

            // Parse inputs
            int idEtudiant = Integer.parseInt(idEtudiantStr);
            int idCours = Integer.parseInt(idCoursStr);
            LocalDateTime debutCours = LocalDateTime.parse(debutCoursStr);
            LocalDateTime finCours = LocalDateTime.parse(finCoursStr);

            if (debutCours.isAfter(finCours)) {
                request.setAttribute("message", "L'heure de fin doit être après l'heure de début.");
                request.getRequestDispatcher("listeInscriptions").forward(request, response);
                return; // Stop execution
            }

            // Retrieve entities
            Etudiant etudiant = new EtudiantRepository().getEtudiantById(idEtudiant);
            if (etudiant == null) {
                request.setAttribute("message", "Étudiant introuvable.");
                request.getRequestDispatcher("listeInscriptions").forward(request, response);
                return; // Stop execution
            }

            Cours cours = new CoursRepository().getCoursById(idCours);
            if (cours == null) {
                request.setAttribute("message", "Cours introuvable.");
                request.getRequestDispatcher("listeInscriptions").forward(request, response);
                return; // Stop execution
            }

            // Create and save InscriptionCours
            InscriptionCours inscriptionCours = new InscriptionCours();
            inscriptionCours.setEtudiant(etudiant);
            inscriptionCours.setCours(cours);
            inscriptionCours.setDebutCours(debutCours);
            inscriptionCours.setFinCours(finCours);

            InscriptionCoursRepository inscriptionCoursRepository = new InscriptionCoursRepository();
            inscriptionCoursRepository.saveInscription(inscriptionCours);

            NoteRepository noteRepository = new NoteRepository();


            if(noteRepository.getNoteByCoursAndEtudiant(idCours,idEtudiant)==null){
            // Create and save Note
                Note note = new Note();

                note.setEtudiant(etudiant);
                note.setCours(cours);
                note.setNote(BigDecimal.ZERO);


                noteRepository.saveNote(note);
}
            // Redirect to success page
            response.sendRedirect("listeInscriptions"); // Ensure this is not after response content is written
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            request.setAttribute("message", "Erreur de validation : " + e.getMessage());
            request.getRequestDispatcher("listeInscriptions").forward(request, response);
        } catch (Exception e) {
            // Handle unexpected errors
            request.setAttribute("message", "Erreur inattendue : " + e.getMessage());
            request.getRequestDispatcher("listeInscriptions").forward(request, response);
        }
    }

        /*

        // Requêtes SQL
        String sqlInsertInscriptions = "INSERT INTO inscriptions_cours (id_etudiant, id_cours, debut_cours, fin_cours) VALUES (?, ?, ?, ?)";
        String sqlInsertResultats = "INSERT IGNORE INTO resultats (id_etudiant, id_cours, note) VALUES (?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement stmtInscriptions = conn.prepareStatement(sqlInsertInscriptions);
                 PreparedStatement stmtResultats = conn.prepareStatement(sqlInsertResultats)) {

                // Préparation de l'insertion des inscriptions
                stmtInscriptions.setInt(1, Integer.parseInt(idEtudiant));
                stmtInscriptions.setInt(2, Integer.parseInt(idCours));
                stmtInscriptions.setString(3, debutCours);
                stmtInscriptions.setString(4, finCours);

                // Préparation de l'insertion dans les résultats
                stmtResultats.setInt(1, Integer.parseInt(idEtudiant));
                stmtResultats.setInt(2, Integer.parseInt(idCours));

                // Exécution des requêtes
                stmtInscriptions.executeUpdate();
                stmtResultats.executeUpdate();

                request.setAttribute("message", "Inscription réussie !");
            }
        } catch (SQLException e) {
            request.setAttribute("message", "Erreur lors de l'inscription : " + e.getMessage());
        }*/

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: dao/NoteRepository#