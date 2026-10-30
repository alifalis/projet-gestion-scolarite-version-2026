error id: file://<WORKSPACE>/src/main/servlet/reinscription/ReinscriptionEleveServlet.java:java/util/regex/Pattern#
file://<WORKSPACE>/src/main/servlet/reinscription/ReinscriptionEleveServlet.java
empty definition using pc, found symbol in pc: java/util/regex/Pattern#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 617
uri: file://<WORKSPACE>/src/main/servlet/reinscription/ReinscriptionEleveServlet.java
text:
```scala

package servlet.reinscription;

import dao.ClasseRepository;
import dao.EtudiantRepository;
import dao.InscriptionAnneeRepository;
import dao.UtilisateurRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import modele.Classe;
import modele.Etudiant;
import modele.InscriptionAnnee;
import modele.Utilisateur;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern@@;

@WebServlet(name = "MettreAJourEtudiant", value = "/mettreAJourEtudiant")
public class ReinscriptionEleveServlet extends HttpServlet {

    private static final String JSP_FORMULAIRE = "/reinscription.jsp";

    private final transient EtudiantRepository etudiantRepository =
            new EtudiantRepository();

    private final transient ClasseRepository classeRepository =
            new ClasseRepository();

    private final transient UtilisateurRepository utilisateurRepository =
            new UtilisateurRepository();

    private final transient InscriptionAnneeRepository inscriptionRepository =
            new InscriptionAnneeRepository();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher(JSP_FORMULAIRE)
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id_etudiant");
        String adresse = nettoyer(request.getParameter("adresse"));
        String email = nettoyer(request.getParameter("email"));
        String contact = nettoyer(request.getParameter("contact"));
        String classeNom = nettoyer(request.getParameter("classe"));
        String filiere = nettoyer(request.getParameter("filiere"));

        // 1. Vérifier l'identifiant
        final int idEtudiant;

        try {
            if (idParam == null || idParam.isBlank()) {
                throw new NumberFormatException();
            }

            idEtudiant = Integer.parseInt(idParam);

            if (idEtudiant <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            afficherErreur(request, response,
                    "L'identifiant de l'étudiant est invalide.");
            return;
        }

        // 2. Vérifier les champs obligatoires
        if (adresse == null || adresse.isBlank()
 email == null || email.isBlank()
 contact == null || contact.isBlank()
 classeNom == null || classeNom.isBlank()
 filiere == null || filiere.isBlank()) {

            afficherErreur(request, response,
                    "Tous les champs obligatoires doivent être renseignés.");
            return;
        }

        // 3. Valider les formats
        if (adresse.length() > 200) {
            afficherErreur(request, response,
                    "L'adresse ne doit pas dépasser 200 caractères.");
            return;
        }

        if (email.length() > 100
 !EMAIL_PATTERN.matcher(email).matches()) {
            afficherErreur(request, response,
                    "L'adresse e-mail est invalide.");
            return;
        }

        if (contact.length() > 15) {
            afficherErreur(request, response,
                    "Le contact ne doit pas dépasser 15 caractères.");
            return;
        }

        try {
            // 4. Récupérer l'étudiant existant
            Etudiant etudiant = etudiantRepository.getById(idEtudiant);

            if (etudiant == null) {
                afficherErreur(request, response,
                        "Aucun étudiant ne correspond à cet identifiant.");
                return;
            }

            // 5. Récupérer la classe existante
            Classe classe = classeRepository.getByNom(classeNom);

            if (classe == null) {
                afficherErreur(request, response,
                        "La classe sélectionnée n'existe pas.");
                return;
            }

            // 6. Vérifier l'utilisateur associé
            Utilisateur utilisateur = etudiant.getUtilisateur();

            if (utilisateur == null) {
                afficherErreur(request, response,
                        "Aucun utilisateur n'est associé à cet étudiant.");
                return;
            }

            // 7. Vérifier que l'e-mail n'appartient pas à un autre utilisateur
            Utilisateur utilisateurEmail =
                    utilisateurRepository.getByEmail(email);

            if (utilisateurEmail != null
                    && utilisateurEmail.getIdUtilisateur()
                    != utilisateur.getIdUtilisateur()) {

                afficherErreur(request, response,
                        "Cette adresse e-mail est déjà utilisée.");
                return;
            }

            // 8. Déterminer l'année scolaire
            String anneeScolaire = obtenirAnneeScolaire();

            // 9. Vérifier si l'étudiant est déjà inscrit cette année
            InscriptionAnnee inscriptionExistante =
                    inscriptionRepository.getByEtudiantAndAnnee(
                            idEtudiant,
                            anneeScolaire
                    );

            if (inscriptionExistante != null) {
                afficherErreur(request, response,
                        "Cet étudiant est déjà inscrit pour l'année scolaire "
                                + anneeScolaire + ".");
                return;
            }

            // 10. Mettre à jour les informations de l'utilisateur
            utilisateur.setAdresse(adresse);
            utilisateur.setEmail(email);
            utilisateurRepository.update(utilisateur);

            // 11. Mettre à jour les informations de l'étudiant
            etudiant.setContact(contact);
            etudiant.setClasse(classe);
            etudiant.setFiliere(filiere);
            etudiantRepository.update(etudiant);

            // 12. Créer l'inscription annuelle
            InscriptionAnnee nouvelleInscription =
                    new InscriptionAnnee(etudiant, anneeScolaire);

            nouvelleInscription.setDateInscription(
                    LocalDateTime.now()
            );

            inscriptionRepository.save(nouvelleInscription);

            // 13. Afficher le succès
            request.setAttribute(
                    "messageSucces",
                    "La réinscription pour l'année scolaire "
                            + anneeScolaire + " a été enregistrée."
            );

            request.getRequestDispatcher(JSP_FORMULAIRE)
                    .forward(request, response);

        } catch (Exception e) {
            getServletContext().log(
                    "Erreur pendant la réinscription de l'étudiant "
                            + idEtudiant,
                    e
            );

            afficherErreur(request, response,
                    "Une erreur est survenue lors de la réinscription. "
                            + "Vérifiez les données et réessayez.");
        }
    }

    private String obtenirAnneeScolaire() {
        LocalDate aujourdHui = LocalDate.now();
        int anneeDebut = aujourdHui.getYear();

        // Hypothèse : la nouvelle année scolaire commence en septembre.
        if (aujourdHui.getMonthValue() < 9) {
            anneeDebut--;
        }

        return anneeDebut + "-" + (anneeDebut + 1);
    }

    private String nettoyer(String valeur) {
        return valeur == null ? null : valeur.trim();
    }

    private void afficherErreur(
            HttpServletRequest request,
            HttpServletResponse response,
            String message
    ) throws ServletException, IOException {

        request.setAttribute("messageErreur", message);

        request.getRequestDispatcher(JSP_FORMULAIRE)
                .forward(request, response);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: java/util/regex/Pattern#