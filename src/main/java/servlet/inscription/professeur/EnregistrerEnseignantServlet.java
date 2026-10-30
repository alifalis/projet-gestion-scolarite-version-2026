package servlet.inscription.professeur;

import controller.EnseignantController;
import controller.RoleController;
import controller.SpecialiteController;
import controller.UtilisateurController;

import modele.Enseignant;
import modele.Role;
import modele.Specialite;
import modele.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


@WebServlet(
        name = "Enregistrer_enseignant",
        value = "/enregister_enseignant"
)
public class EnregistrerEnseignantServlet extends HttpServlet {

    private static final String PENDING_PASSWORD_PREFIX =
            "pendingTeacherPassword:";

    private transient UtilisateurController utilisateurController;
    private transient EnseignantController enseignantController;
    private transient RoleController roleController;
    private transient SpecialiteController specialiteController;

    @Override
    public void init() throws ServletException {
        utilisateurController = new UtilisateurController();
        enseignantController = new EnseignantController();
        roleController = new RoleController();
        specialiteController = new SpecialiteController();
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
                    String motDePasseHash = consommerMotDePasse(
                    request,
                    request.getParameter("inscriptionToken")
                );
            String dateNaissance = request.getParameter("date_naissance");

            String numeroAdresse = request.getParameter("numero_adresse");
            String adresse = request.getParameter("adresse");
            String commune = request.getParameter("commune");
            String codePostal = request.getParameter("code_postal");
            String specialite = request.getParameter("specialite");

            // 2. Vérifier les champs obligatoires
            if (estVide(nom) || estVide(prenom)
                    || estVide(telephone) || estVide(email)
                    || estVide(motDePasseHash) || estVide(dateNaissance)
                    || estVide(adresse) || estVide(commune)
                    || estVide(codePostal) || estVide(specialite)) {

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

            if (!codePostal.matches("\\d{5}")) {
                afficherErreur(
                        request,
                        response,
                        "Le code postal doit contenir cinq chiffres."
                );
                return;
            }

            if (adresseComplete.length() > 200) {
                afficherErreur(
                        request,
                        response,
                        "L'adresse complète ne doit pas dépasser 200 caractères."
                );
                return;
            }

            Specialite specialiteEntity =
                    specialiteController.rechercherSpecialiteParNom(specialite);
            if (specialiteEntity == null) {
                throw new IllegalArgumentException(
                        "La spécialité sélectionnée n'existe pas."
                );
            }

            // 6. Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();

            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setEmail(email);
            utilisateur.setAdresse(adresseComplete.toString());

            Role role = roleController.rechercherRoleParNom("Enseignant");
            if (role == null) {
                throw new IllegalArgumentException(
                        "Le rôle enseignant est indisponible."
                );
            }
            utilisateur.setRole(role);

            // Hacher le mot de passe avant son stockage
                utilisateur.setMotDePasse(motDePasseHash);

            // 7. Enregistrer l'utilisateur via son contrôleur
            utilisateurController.ajouterUtilisateur(utilisateur);

            // 8. Créer l'enseignant
            Enseignant enseignant = new Enseignant();

            enseignant.setContact(telephone);
            enseignant.setSpecialite(specialiteEntity);
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

    private String consommerMotDePasse(
            HttpServletRequest request,
            String inscriptionToken
    ) {
        if (inscriptionToken == null) {
            return null;
        }
        if (inscriptionToken.length() != 36) {
            return null;
        }
        try {
            if (!UUID.fromString(inscriptionToken).toString()
                    .equals(inscriptionToken)) {
                return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        synchronized (session) {
            String attributeName =
                    PENDING_PASSWORD_PREFIX + inscriptionToken;
            Object motDePasse = session.getAttribute(attributeName);
            session.removeAttribute(attributeName);
            return motDePasse instanceof String passwordHash
                    ? passwordHash
                    : null;
        }
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
        request.setAttribute(
            "specialites", specialiteController.afficherSpecialites());

        request.getRequestDispatcher(
                "/inscription/inscription_enseignant.jsp"
        ).forward(request, response);
    }
}
