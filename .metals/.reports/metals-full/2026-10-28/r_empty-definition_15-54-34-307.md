error id: file://<WORKSPACE>/src/main/servlet/accespageprof/NotifierEtudiantsServlet.java:dao/NoteRepository#
file://<WORKSPACE>/src/main/servlet/accespageprof/NotifierEtudiantsServlet.java
empty definition using pc, found symbol in pc: dao/NoteRepository#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 399
uri: file://<WORKSPACE>/src/main/servlet/accespageprof/NotifierEtudiantsServlet.java
text:
```scala
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import dao.@@NoteRepository;
import modele.Note;
import jakarta.mail.*;
import jakarta.mail.internet.*;

@WebServlet(name = "NotifierEtudiantsServlet", value = "/notifierEtudiants")
public class NotifierEtudiantsServlet extends HttpServlet {

    private JSONObject smtpSettings;

    @Override
    public void init() throws ServletException {
        super.init();

        // Load SMTP settings from JSON
        try {
            String path = getServletContext().getRealPath("/settings.json");
            StringBuilder jsonContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
            }
            smtpSettings = new JSONObject(jsonContent.toString());
        } catch (Exception e) {
            throw new ServletException("Erreur lors du chargement des paramètres SMTP : " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the professor's ID from the session
        Integer profId = (Integer) request.getSession().getAttribute("id_professeur");
        if (profId == null) {
            request.setAttribute("errorMessage", "Session expirée. Veuillez vous reconnecter.");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // Fetch emails of the professor's students
        NoteRepository noteRepository = new NoteRepository();
        List<Note> resultats = noteRepository.getNotesByEnseignant(profId);
        List<String> emails = new ArrayList<>();
        for (Note note : resultats) {
            emails.add(note.getEtudiant().getUtilisateur().getEmail());
        }

        // Remove duplicate emails
        emails = new ArrayList<>(new HashSet<>(emails));

        // SMTP details
        String fromEmail = smtpSettings.getJSONObject("smtp").getString("username");
        String password = smtpSettings.getJSONObject("smtp").getString("password");
        String host = smtpSettings.getJSONObject("smtp").getString("host");
        String port = smtpSettings.getJSONObject("smtp").getString("port");
        String subject = "Notification de Notes";
        String messageContent = "Bonjour, \n\nVos notes ont été mises à jour. Veuillez vérifier votre compte.";

        try {
            // Configure SMTP session
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            // Send email to each student
            for (String email : emails) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject(subject);
                message.setText(messageContent);
                Transport.send(message);
            }
            request.setAttribute("message", "Les étudiants ont été notifiés avec succès !");
        } catch (MessagingException e) {
            request.setAttribute("errorMessage", "Erreur lors de l'envoi des notifications : " + e.getMessage());
        }

        // Repopulate the notes for the professor
        request.setAttribute("resultats", resultats);

        // Forward back to gestion_notes.jsp
        request.getRequestDispatcher("prof/gestion_notes.jsp").forward(request, response);
    }
}


```


#### Short summary: 

empty definition using pc, found symbol in pc: dao/NoteRepository#