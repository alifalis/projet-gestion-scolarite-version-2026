error id: file://<WORKSPACE>/src/main/java/accespageprof/ListeResultatsServlet.java:dao/NoteRepository#
file://<WORKSPACE>/src/main/java/accespageprof/ListeResultatsServlet.java
empty definition using pc, found symbol in pc: dao/NoteRepository#
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 68
uri: file://<WORKSPACE>/src/main/java/accespageprof/ListeResultatsServlet.java
text:
```scala
package accespageprof;

import dao.EnseignantRepository;
import dao.@@NoteRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Enseignant;
import modele.Note;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListeResultatsServlet", value = "/listeResultats")
public class ListeResultatsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to the login page
        request.getRequestDispatcher("/prof/connexion_prof.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email_enseignant");
        String motDePasse = request.getParameter("mot_de_passe");
        EnseignantRepository enseignantRepository = new EnseignantRepository();

        if (email == null || motDePasse == null || email.isEmpty() || motDePasse.isEmpty()) {
            request.setAttribute("error", "Email et mot de passe sont requis.");
            request.getRequestDispatcher("prof/connexion_prof.jsp").forward(request, response);
            return;
        }

        Enseignant enseignant = enseignantRepository.getProfesseurByEmail(email);

        if (enseignant != null && enseignant.getUtilisateur().getMotDePasse().equals(motDePasse)) {
            // Authentication successful
            request.getSession().setAttribute("user", "prof");
            request.getSession().setAttribute("id_professeur", enseignant.getIdEnseignant());

            NoteRepository noteRepository = new NoteRepository();
            List<Note> resultats = noteRepository.getNotesByEnseignant(enseignant.getIdEnseignant());

            request.setAttribute("resultats", resultats);
            request.setAttribute("email", email);
            request.getRequestDispatcher("prof/gestion_notes.jsp").forward(request, response);
        } else {
            // Authentication failed
            request.setAttribute("error", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("prof/connexion_prof.jsp").forward(request, response);
        }
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: dao/NoteRepository#