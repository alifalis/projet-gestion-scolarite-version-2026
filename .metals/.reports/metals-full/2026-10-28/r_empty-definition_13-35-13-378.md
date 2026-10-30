error id: file://<WORKSPACE>/src/main/servlet/inscription/professeur/EnregistrerEnseignantServlet.java:_empty_/EnseignantService#
file://<WORKSPACE>/src/main/servlet/inscription/professeur/EnregistrerEnseignantServlet.java
empty definition using pc, found symbol in pc: _empty_/EnseignantService#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1163
uri: file://<WORKSPACE>/src/main/servlet/inscription/professeur/EnregistrerEnseignantServlet.java
text:
```scala
package servlet.inscription.professeur;

import controller.EnseignantController;
import controller.UtilisateurController;

import modele.Enseignant;
import modele.Utilisateur;
import service.EnseignantService;
import service.UtilisateurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet(
        name = "Enregistrer_enseignant",
        value = "/enregister_enseignant"
)
public class EnregistrerEnseignantServlet extends HttpServlet {

    private transient UtilisateurController utilisateurController;
    private transient EnseignantController enseignantController;

    @Override
    public void init() throws ServletException {
        utilisateurController = new UtilisateurController(new UtilisateurService());
        enseignantController = new EnseignantController(new Enseigna@@ntService());
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // 1. Récupérer les données du formulaire
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String telephone = request.getParameter("telephone");
            String email = request.getParameter("email");
            String motDePasse = request.getParameter("mot_de_passe");
            String dateNaissance = request.getParameter("date_naissance");

            String numeroAdresse = request.getParameter("numero_adresse");
            String adresse = request.getParameter("adresse");
            String commune = request.getParameter("commune");
            String codePostal = request.getParameter("code_postal");
            String specialite = request.getParameter("specialite");

            // 2. Vérifier les champs obligatoires
            if (estVide(nom) || estVide(prenom)
 estVide(telephone) || estVide(email)
 estVide(motDePasse) || estVide(dateNaissance)
 estVide(specialite)) {

                afficherErreur(
                        request,
                        response,
                        "Veuillez remplir tous les champs obligatoires."
                );
                return;
            }

            nom = nom.trim();
            prenom = prenom.trim();
            telephone = telephone.trim();
            email = email.trim();
            specialite = specialite.trim();

            // 3. Vérifier que l'e-mail n'est pas déjà utilisé
            if (utilisateurController.rechercherParEmail(email) != null) {
                afficherErreur(
                        request,
                        response,
                        "Cette adresse e-mail est déjà utilisée."
                );
                return;
            }

            // 4. Convertir la date de naissance
            SimpleDateFormat format =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

            format.setLenient(false);

            Date date;
            try {
                date = format.parse(dateNaissance);
            } catch (ParseException e) {
                afficherErreur(
                        request,
                        response,
                        "La date de naissance est invalide."
                );
                return;
            }

            // 5. Construire l'adresse complète
            StringBuilder adresseComplete = new StringBuilder();

            ajouterAdresse(adresseComplete, numeroAdresse);
            ajouterAdresse(adresseComplete, adresse);
            ajouterAdresse(adresseComplete, codePostal);
            ajouterAdresse(adresseComplete, commune);

            if (adresseComplete.length() == 0) {
                afficherErreur(
                        request,
                        response,
                        "Veuillez renseigner une adresse."
                );
                return;
            }

            // 6. Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();

            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setEmail(email);
            utilisateur.setAdresse(adresseComplete.toString());

            // Hacher le mot de passe avant son stockage
            utilisateur.setMotDePasse(
                    BCrypt.hashpw(motDePasse, BCrypt.gensalt())
            );

            /*
             * Si ton modèle exige un rôle non nul, affecte ici
             * le rôle correspondant à un enseignant.
             */

            // 7. Enregistrer l'utilisateur via son contrôleur
            utilisateurController.ajouterUtilisateur(utilisateur);

            // 8. Créer l'enseignant
            Enseignant enseignant = new Enseignant();

            enseignant.setContact(telephone);
            enseignant.setSpecialite(specialite);
            enseignant.setUtilisateur(utilisateur);
            enseignant.setDateNaissance(date);

            // 9. Enregistrer l'enseignant via son contrôleur
            enseignantController.ajouterEnseignant(enseignant);

            // 10. Rediriger après l'enregistrement
            response.sendRedirect(
                    request.getContextPath() + "/creer_enseignant?success=true"
            );

        } catch (IllegalArgumentException e) {

            afficherErreur(request, response, e.getMessage());

        } catch (Exception e) {

            getServletContext().log(
                    "Erreur lors de l'enregistrement de l'enseignant",
                    e
            );

            afficherErreur(
                    request,
                    response,
                    "Une erreur est survenue lors de l'inscription."
            );
        }
    }

    private boolean estVide(String valeur) {
        return valeur == null || valeur.trim().isEmpty();
    }

    private void ajouterAdresse(StringBuilder resultat, String partie) {
        if (partie != null && !partie.trim().isEmpty()) {
            if (resultat.length() > 0) {
                resultat.append(", ");
            }
            resultat.append(partie.trim());
        }
    }

    private void afficherErreur(
            HttpServletRequest request,
            HttpServletResponse response,
            String message)
            throws ServletException, IOException {

        request.setAttribute("errorMessage", message);

        request.getRequestDispatcher(
                "/inscription/inscription_enseignant.jsp"
        ).forward(request, response);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/EnseignantService#