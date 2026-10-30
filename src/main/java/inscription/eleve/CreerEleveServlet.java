package inscription.eleve;

import controller.ClasseController;
import controller.UtilisateurController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "Creer_eleve", urlPatterns = "/creer_eleve")
public class CreerEleveServlet extends HttpServlet {

        private static final String PENDING_PASSWORD_PREFIX =
                        "pendingStudentPassword:";

    private transient UtilisateurController utilisateurController;
        private transient ClasseController classeController;

    @Override
    public void init() throws ServletException {
        utilisateurController =
                new UtilisateurController();
        classeController = new ClasseController();
    }

    // Afficher le formulaire d'inscription
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

                request.setAttribute("classes", classeController.afficherClasses());

        request.getRequestDispatcher(
                "/inscription/inscription_eleve.jsp"
        ).forward(request, response);
    }

    // Vérifier le formulaire et afficher le récapitulatif
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Récupérer les données du formulaire
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("mot_de_passe");

        String numeroAdresse = request.getParameter("numero_adresse");
        String adresse = request.getParameter("adresse");
        String codePostal = request.getParameter("code_postal");
        String commune = request.getParameter("commune");

        String dateNaissance = request.getParameter("date_naissance");
        String classe = request.getParameter("classe");
        String specialite = request.getParameter("specialite");

        // 2. Vérifier les champs obligatoires
        if (estVide(nom) || estVide(prenom)
                || estVide(telephone) || estVide(email)
                || estVide(motDePasse) || estVide(dateNaissance)
                || estVide(numeroAdresse) || estVide(adresse)
                || estVide(codePostal) || estVide(commune)
                || estVide(classe) || estVide(specialite)) {

            afficherErreur(
                    request,
                    response,
                    "Veuillez remplir tous les champs obligatoires."
            );
            return;
        }

                if (!codePostal.matches("\\d{5}")) {
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

        // 3. Vérifier si l'adresse e-mail existe déjà
        try {
            if (utilisateurController.rechercherParEmail(
                    email.trim()) != null) {

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

        // 4. Transmettre les données à la page de récapitulatif
        request.setAttribute("nom", nom.trim());
        request.setAttribute("prenom", prenom.trim());
        request.setAttribute("telephone", telephone.trim());
        request.setAttribute("email", email.trim());

        request.setAttribute("numero_adresse", numeroAdresse);
        request.setAttribute("adresse", adresse);
        request.setAttribute("code_postal", codePostal);
        request.setAttribute("commune", commune);

        request.setAttribute("date_naissance", dateNaissance);
        request.setAttribute("classe", classe);
        request.setAttribute("specialite", specialite);

        // Le mot de passe reste en session jusqu'à la confirmation.

        // 5. Afficher le récapitulatif
        request.getRequestDispatcher(
                "/inscription/afficherEleve.jsp"
        ).forward(request, response);
    }

    // Vérifier si une valeur est vide
    private boolean estVide(String valeur) {
        return valeur == null || valeur.trim().isEmpty();
    }

    // Afficher un message d'erreur sur le formulaire
    private void afficherErreur(
            HttpServletRequest request,
            HttpServletResponse response,
            String message)
            throws ServletException, IOException {

        request.setAttribute("errorMessage", message);
        request.setAttribute("classes", classeController.afficherClasses());

        request.getRequestDispatcher(
                "/inscription/inscription_eleve.jsp"
        ).forward(request, response);
    }
}
