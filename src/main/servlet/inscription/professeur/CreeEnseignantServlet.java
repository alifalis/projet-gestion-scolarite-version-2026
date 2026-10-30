package inscription.professeur;

import controller.SpecialiteController;
import controller.UtilisateurController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "Creer_enseignant", value = "/creer_enseignant")
public class CreeEnseignantServlet extends HttpServlet {

    private static final String PENDING_PASSWORD_PREFIX =
            "pendingTeacherPassword:";

    private transient UtilisateurController utilisateurController;
    private transient SpecialiteController specialiteController;

    @Override
    public void init() throws ServletException {
        utilisateurController = new UtilisateurController();
        specialiteController = new SpecialiteController();
    }

    // Afficher le formulaire d'inscription
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        request.setAttribute("specialites", specialiteController.afficherSpecialites());
        request.getRequestDispatcher(
                "/inscription/inscription_enseignant.jsp"
        ).forward(request, response);
    }

    // Valider les données et afficher le récapitulatif
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Récupération des données
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

        // 2. Vérification des champs obligatoires
        if (estVide(nom) || estVide(prenom)
                || estVide(telephone) || estVide(email)
                || estVide(motDePasse) || estVide(dateNaissance)
            || estVide(adresse) || estVide(commune)
            || estVide(codePostal) || estVide(specialite)) {

            afficherErreur(
                    request,
                    response,
                    "Veuillez remplir tous les champs obligatoires."
            );
            return;
        }

        if (!codePostal.trim().matches("\\d{5}")) {
            afficherErreur(
                    request,
                    response,
                    "Le code postal doit contenir cinq chiffres."
            );
            return;
        }

        if (motDePasse.length() < 8 || motDePasse.length() > 128) {
            afficherErreur(
                    request,
                    response,
                    "Le mot de passe doit contenir entre 8 et 128 caractères."
            );
            return;
        }

        if (specialiteController.rechercherSpecialiteParNom(
                specialite.trim()) == null) {
            afficherErreur(
                    request,
                    response,
                    "La spécialité sélectionnée n'existe pas."
            );
            return;
        }

        // 3. Nettoyage des données
        nom = nom.trim();
        prenom = prenom.trim();
        telephone = telephone.trim();
        email = email.trim();
        dateNaissance = dateNaissance.trim();
        specialite = specialite.trim();

        // 4. Vérification de l'adresse e-mail via le contrôleur
        try {
            if (utilisateurController.rechercherParEmail(email) != null) {
                afficherErreur(
                        request,
                        response,
                        "Cette adresse e-mail est déjà utilisée."
                );
                return;
            }
        } catch (Exception e) {
            getServletContext().log(
                    "Erreur lors de la vérification de l'e-mail",
                    e
            );

            afficherErreur(
                    request,
                    response,
                    "Impossible de vérifier l'adresse e-mail. Réessayez."
            );
            return;
        }

            String inscriptionToken = UUID.randomUUID().toString();
            request.getSession().setAttribute(
                PENDING_PASSWORD_PREFIX + inscriptionToken,
                BCrypt.hashpw(motDePasse, BCrypt.gensalt())
            );
            request.setAttribute("inscriptionToken", inscriptionToken);

        // 5. Transmettre les données au récapitulatif
        request.setAttribute("nom", nom);
        request.setAttribute("prenom", prenom);
        request.setAttribute("telephone", telephone);
        request.setAttribute("email", email);

        request.setAttribute("numero_adresse", numeroAdresse);
        request.setAttribute("adresse", adresse);
        request.setAttribute("code_postal", codePostal);
        request.setAttribute("commune", commune);

        request.setAttribute("date_naissance", dateNaissance);
        request.setAttribute("specialite", specialite);

        // Le mot de passe reste en session jusqu'à la confirmation.

        // 6. Afficher la page de récapitulatif
        request.getRequestDispatcher(
                "/inscription/afficherEnseignant.jsp"
        ).forward(request, response);
    }

    // Vérifier si une valeur est vide
    private boolean estVide(String valeur) {
        return valeur == null || valeur.trim().isEmpty();
    }

    // Afficher une erreur sur le formulaire
    private void afficherErreur(
            HttpServletRequest request,
            HttpServletResponse response,
            String message)
            throws ServletException, IOException {

        request.setAttribute("errorMessage", message);
        request.setAttribute("specialites", specialiteController.afficherSpecialites());

        request.getRequestDispatcher(
                "/inscription/inscription_enseignant.jsp"
        ).forward(request, response);
    }
}
