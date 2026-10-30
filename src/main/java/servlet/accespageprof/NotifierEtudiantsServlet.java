package servlet.accespageprof;

import controller.ResultatController;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.Resultat;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@WebServlet(name = "NotifierEtudiantsServlet", value = "/notifierEtudiants")
public class NotifierEtudiantsServlet extends HttpServlet {

    private static final String RESULTS_VIEW = "/prof/gestion_notes.jsp";

    private transient ResultatController resultatController;
    private transient JSONObject smtpSettings;

    @Override
    public void init() throws ServletException {
        resultatController = new ResultatController();
        try (InputStream input = getServletContext()
                .getResourceAsStream("/settings.json")) {
            if (input == null) {
                throw new ServletException(
                        "Le fichier settings.json est introuvable.");
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                smtpSettings = new JSONObject(content.toString());
            }
        } catch (IOException | RuntimeException e) {
            throw new ServletException(
                    "Erreur lors du chargement des paramètres SMTP.", e);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object idSession = session == null
                ? null : session.getAttribute("id_professeur");
        if (session == null || !"prof".equals(session.getAttribute("user"))
                || !(idSession instanceof Integer idProfesseur)) {
            response.sendRedirect(
                    request.getContextPath() + "/connexionProf");
            return;
        }

        List<Resultat> resultats = resultatController
                .afficherResultatsParEnseignant(idProfesseur);
        Set<String> emails = trouverEmails(resultats);
        if (emails.isEmpty()) {
            request.setAttribute("errorMessage", "Aucun étudiant à notifier.");
            afficherResultats(request, response, resultats);
            return;
        }

        try {
            JSONObject smtp = smtpSettings.getJSONObject("smtp");
            String fromEmail = smtp.getString("username");
            String password = smtp.getString("password");
            Properties properties = new Properties();
            properties.put("mail.smtp.host", smtp.getString("host"));
            properties.put("mail.smtp.port", smtp.getString("port"));
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session mailSession = Session.getInstance(properties,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fromEmail, password);
                        }
                    });

            for (String email : emails) {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(email)
                );
                message.setSubject("Notification de notes", "UTF-8");
                message.setText(
                        "Bonjour,\n\nVos notes ont été mises à jour. "
                                + "Veuillez vérifier votre compte.",
                        "UTF-8"
                );
                Transport.send(message);
            }
            request.setAttribute(
                    "message", "Les étudiants ont été notifiés avec succès.");
        } catch (MessagingException | RuntimeException e) {
            getServletContext().log(
                    "Erreur lors de l'envoi des notifications.", e);
            request.setAttribute(
                    "errorMessage", "Erreur lors de l'envoi des notifications.");
        }

        afficherResultats(request, response, resultats);
    }

    private Set<String> trouverEmails(List<Resultat> resultats) {
        Set<String> emails = new LinkedHashSet<>();
        for (Resultat resultat : resultats) {
            if (resultat.getEtudiant() == null
                    || resultat.getEtudiant().getUtilisateur() == null) {
                continue;
            }
            String email = resultat.getEtudiant()
                    .getUtilisateur().getEmail();
            if (email != null && !email.isBlank()) {
                emails.add(email.trim());
            }
        }
        return emails;
    }

    private void afficherResultats(
            HttpServletRequest request,
            HttpServletResponse response,
            List<Resultat> resultats
    ) throws ServletException, IOException {
        request.setAttribute("resultats", resultats);
        request.getRequestDispatcher(RESULTS_VIEW).forward(request, response);
    }
}
