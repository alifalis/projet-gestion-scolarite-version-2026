package reinscription;

import controller.ClasseController;
import controller.EtudiantController;
import controller.InscriptionAnneeController;
import controller.UtilisateurController;

import modele.Classe;
import modele.Etudiant;
import modele.InscriptionAnnee;
import modele.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

@WebServlet("/mettreAJourEtudiant")
public class ReinscriptionEleveServlet extends HttpServlet {

    private static final String REINSCRIPTION_VIEW =
            "/inscription/reinscription.jsp";

    private final transient EtudiantController etudiantController =
            new EtudiantController();

    private final transient ClasseController classeController =
            new ClasseController();

    private final transient UtilisateurController utilisateurController =
            new UtilisateurController();

    private final transient InscriptionAnneeController inscriptionController =
            new InscriptionAnneeController();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

                request.setAttribute("classes", classeController.afficherClasses());
        request.getRequestDispatcher(REINSCRIPTION_VIEW)
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Récupération des champs du formulaire
            String idTexte = nettoyer(request.getParameter("id_etudiant"));
            String numeroAdresse =
                    nettoyer(request.getParameter("numero_adresse"));
            String adresse = nettoyer(request.getParameter("adresse"));
            String codePostal = nettoyer(request.getParameter("code_postal"));
            String commune = nettoyer(request.getParameter("commune"));
            String email = nettoyer(request.getParameter("email"));
            String contact = nettoyer(request.getParameter("contact"));
            String classeNom = nettoyer(request.getParameter("classe"));
            String filiere = nettoyer(request.getParameter("filiere"));

            // 2. Validation des champs
            if (idTexte.isEmpty() || numeroAdresse.isEmpty()
                    || adresse.isEmpty() || codePostal.isEmpty()
                    || commune.isEmpty()
                    || email.isEmpty() || contact.isEmpty()
                    || classeNom.isEmpty() || filiere.isEmpty()) {

                afficherErreur(request, response,
                        "Tous les champs sont obligatoires.");
                return;
            }

            int idEtudiant;

            try {
                idEtudiant = Integer.parseInt(idTexte);
            } catch (NumberFormatException e) {
                afficherErreur(request, response,
                        "L'identifiant de l'étudiant est invalide.");
                return;
            }

            if (idEtudiant <= 0) {
                afficherErreur(request, response,
                        "L'identifiant de l'étudiant doit être positif.");
                return;
            }

            if (email.length() > 100
                    || !Pattern.matches(
                            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                            email)) {

                afficherErreur(request, response,
                        "L'adresse e-mail est invalide.");
                return;
            }

            if (!codePostal.matches("\\d{5}")) {
                afficherErreur(request, response,
                        "Le code postal doit contenir cinq chiffres.");
                return;
            }

            if (contact.length() > 15) {
                afficherErreur(request, response,
                        "Le contact ne doit pas dépasser 15 caractères.");
                return;
            }

            String adresseComplete = numeroAdresse + " " + adresse
                    + ", " + codePostal + " " + commune;
            if (adresseComplete.length() > 200) {
                afficherErreur(request, response,
                        "L'adresse complète ne doit pas dépasser 200 caractères.");
                return;
            }

            // 3. Recherche de l'étudiant via son contrôleur
            Etudiant etudiant =
                    etudiantController.rechercherEtudiant(idEtudiant);

            if (etudiant == null) {
                afficherErreur(request, response,
                        "Aucun étudiant trouvé avec cet identifiant.");
                return;
            }

            // 4. Recherche de la classe via son contrôleur
            Classe classe =
                    classeController.rechercherClasseParNom(classeNom);

            if (classe == null) {
                afficherErreur(request, response,
                        "La classe sélectionnée n'existe pas.");
                return;
            }

            // 5. Récupération de l'utilisateur associé à l'étudiant
            Utilisateur utilisateur = etudiant.getUtilisateur();

            if (utilisateur == null) {
                afficherErreur(request, response,
                        "Aucun utilisateur n'est associé à cet étudiant.");
                return;
            }

            // 6. Vérification de l'unicité de l'e-mail
            Utilisateur utilisateurExistant =
                    utilisateurController.rechercherParEmail(email);

            if (utilisateurExistant != null
                    && utilisateurExistant.getIdUtilisateur()
                    != utilisateur.getIdUtilisateur()) {

                afficherErreur(request, response,
                        "Cette adresse e-mail est déjà utilisée.");
                return;
            }

            // 7. Détermination de l'année scolaire
            String anneeScolaire = obtenirAnneeScolaire();

            // 8. Vérification d'une inscription annuelle existante
            InscriptionAnnee inscriptionExistante =
                    inscriptionController.rechercherParEtudiantEtAnnee(
                            idEtudiant,
                            anneeScolaire
                    );

            if (inscriptionExistante != null) {
                afficherErreur(request, response,
                        "L'étudiant est déjà réinscrit pour l'année scolaire "
                                + anneeScolaire + ".");
                return;
            }

            // 9. Mise à jour des informations de l'utilisateur
            utilisateur.setAdresse(adresseComplete);
            utilisateur.setEmail(email);

            utilisateurController.modifierUtilisateur(utilisateur);

            // 10. Mise à jour des informations de l'étudiant
            etudiant.setContact(contact);
            etudiant.setClasse(classe);
            etudiant.setFiliere(filiere);

            etudiantController.modifierEtudiant(etudiant);

            // 11. Création de la nouvelle inscription annuelle
            InscriptionAnnee inscription =
                    new InscriptionAnnee(etudiant, anneeScolaire);

            inscription.setDateInscription(
                    LocalDateTime.now(ZoneId.systemDefault())
            );

            inscriptionController.ajouterInscription(inscription);

            // 12. Message de succès
            request.setAttribute(
                    "messageSucces",
                    "La réinscription de l'étudiant a été effectuée "
                            + "pour l'année scolaire " + anneeScolaire + "."
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute("messageErreur", e.getMessage());

        } catch (Exception e) {

            getServletContext().log(
                    "Erreur lors de la réinscription de l'étudiant.",
                    e
            );

            request.setAttribute(
                    "messageErreur",
                    "Une erreur est survenue lors de la réinscription."
            );
        }

        request.setAttribute("classes", classeController.afficherClasses());
        request.getRequestDispatcher(REINSCRIPTION_VIEW)
                .forward(request, response);
    }

    /**
     * Retourne l'année scolaire en cours.
     * L'année scolaire commence en septembre.
     */
    private String obtenirAnneeScolaire() {

        LocalDate aujourdHui = LocalDate.now(ZoneId.systemDefault());
        int anneeDebut = aujourdHui.getYear();

        if (aujourdHui.getMonthValue() < 9) {
            anneeDebut--;
        }

        return anneeDebut + "-" + (anneeDebut + 1);
    }

    private String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }

    private void afficherErreur(
            HttpServletRequest request,
            HttpServletResponse response,
            String message
    ) throws ServletException, IOException {

        request.setAttribute("messageErreur", message);
        request.setAttribute("classes", classeController.afficherClasses());

        request.getRequestDispatcher(REINSCRIPTION_VIEW)
                .forward(request, response);
    }
}
