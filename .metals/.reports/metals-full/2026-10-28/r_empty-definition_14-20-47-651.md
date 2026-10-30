error id: file://<WORKSPACE>/src/main/servlet/Login/Eleve/LoginController.java:modele/Note#
file://<WORKSPACE>/src/main/servlet/Login/Eleve/LoginController.java
empty definition using pc, found symbol in pc: modele/Note#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 419
uri: file://<WORKSPACE>/src/main/servlet/Login/Eleve/LoginController.java
text:
```scala
package accespagetudiant;

import dao.EtudiantRepository;
import dao.InscriptionCoursRepository;
import dao.NoteRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Etudiant;
import modele.InscriptionCours;
import modele.@@Note;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginController", value = "/loginController")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to the login page
        request.getRequestDispatcher("/etudiant/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email_etudiant");
        String motDePasseSaisi = request.getParameter("mot_de_passe");

        EtudiantRepository etudiantRepository = new EtudiantRepository();
        NoteRepository noteRepository = new NoteRepository();
        InscriptionCoursRepository inscriptionCoursRepository = new InscriptionCoursRepository();

        if (email == null || motDePasseSaisi == null || email.isEmpty() || motDePasseSaisi.isEmpty()) {
            request.setAttribute("error", "Email et mot de passe sont requis.");
            request.getRequestDispatcher("/etudiant/login.jsp").forward(request, response);
            return;
        }

        Etudiant etudiant = etudiantRepository.getEtudiantByEmail(email);

        if (etudiant != null && etudiant.getUtilisateur().getMotDePasse().equals(motDePasseSaisi)) {
            // Successful login
            request.getSession().setAttribute("user", "etudiant");
            request.getSession().setAttribute("id_etudiant", etudiant.getIdEtudiant());

            List<Note> notes = noteRepository.getNotesByEtudiant(etudiant.getIdEtudiant());
            List<InscriptionCours> inscriptionCours = inscriptionCoursRepository.getInscriptionsByEtudiant(etudiant.getIdEtudiant());

            request.setAttribute("notes", notes);
            request.setAttribute("inscriptionCours", inscriptionCours);
            request.getRequestDispatcher("etudiant/notes.jsp").forward(request, response);
        } else {
            // Failed login
            request.setAttribute("error", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("/etudiant/login.jsp").forward(request, response);
        }
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: modele/Note#