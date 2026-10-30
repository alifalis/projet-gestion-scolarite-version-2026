package servlet.inscription.eleve;

import controller.ClasseController;
import controller.EtudiantController;
import controller.RoleController;
import controller.UtilisateurController;
import modele.Classe;
import modele.Etudiant;
import modele.Role;
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
import java.util.List;
import java.util.UUID;


@WebServlet(name = "Enregistrer_eleve", value = "/enregistrer_eleve")
public class EnregistrerEleveServlet extends HttpServlet {

        private static final String PENDING_PASSWORD_PREFIX =
                        "pendingStudentPassword:";

    private transient EtudiantController etudiantController;
        private transient RoleController roleController;
    private transient UtilisateurController utilisateurController;
    private transient ClasseController classeController;

    @Override
    public void init() throws ServletException {
        etudiantController =
                new EtudiantController();
        roleController = new RoleController();

        utilisateurController =
                new UtilisateurController();

        classeController =
                new ClasseController();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // 1. Récupération des données du formulaire
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String telephone = request.getParameter("telephone");
            String email = request.getParameter("email");
            String motDePasseHash = consommerMotDePasse(
                    request,
                    request.getParameter("inscriptionToken")
            );

            String numeroAdresse =
                    request.getParameter("numero_adresse");
            String adresse = request.getParameter("adresse");
            String codePostal = request.getParameter("code_postal");
            String commune = request.getParameter("commune");

            String dateNaissance =
                    request.getParameter("date_naissance");
            String classeParam = request.getParameter("classe");
            String filiereParam = request.getParameter("specialite");

            // 2. Vérification des champs obligatoires
            if (estVide(nom) || estVide(prenom)
                    || estVide(telephone) || estVide(email)
                    || estVide(motDePasseHash) || estVide(dateNaissance)
                    || estVide(numeroAdresse) || estVide(adresse)
                    || estVide(codePostal) || estVide(commune)
                    || estVide(classeParam) || estVide(filiereParam)) {

                throw new IllegalArgumentException(
                        "Veuillez remplir tous les champs obligatoires."
                );
            }

            nom = nom.trim();
            prenom = prenom.trim();
            telephone = telephone.trim();
            email = email.trim();

                        if (!codePostal.trim().matches("\\d{5}")) {
                                throw new IllegalArgumentException(
                                                "Le code postal doit contenir cinq chiffres."
                                );
                        }

            // 3. Vérification de l'adresse e-mail via le contrôleur
            if (utilisateurController.rechercherParEmail(email) != null) {
                throw new IllegalArgumentException(
                        "Cette adresse e-mail est déjà utilisée."
                );
            }

            // 4. Conversion de la date de naissance
            SimpleDateFormat format =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
            format.setLenient(false);

            Date date;
            try {
                date = format.parse(dateNaissance);
            } catch (ParseException e) {
                throw new IllegalArgumentException(
                        "La date de naissance est invalide."
                );
            }

            // 5. Recherche de la classe via le contrôleur
            Classe classe =
                    classeController.rechercherClasseParNom(
                            classeParam.trim()
                    );

            if (classe == null) {
                throw new IllegalArgumentException(
                        "La classe sélectionnée n'existe pas."
                );
            }

                        // 6. Vérification de la filière
                        String filiere = filiereParam.trim();
                        if (!List.of("GI", "GM", "GC").contains(filiere)) {
                throw new IllegalArgumentException(
                        "La filière sélectionnée est invalide."
                );
            }

            // 7. Création de l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setEmail(email);

                        Role role = roleController.rechercherRoleParNom("Etudiant");
                        if (role == null) {
                                throw new IllegalArgumentException(
                                                "Le rôle étudiant est indisponible."
                                );
                        }
                        utilisateur.setRole(role);

            String adresseComplete =
                    ((numeroAdresse != null
                            && !numeroAdresse.isBlank())
                            ? numeroAdresse.trim() + " " : "")
                    + ((adresse != null && !adresse.isBlank())
                            ? adresse.trim() + ", " : "")
                    + ((codePostal != null && !codePostal.isBlank())
                            ? codePostal.trim() + " " : "")
                    + ((commune != null && !commune.isBlank())
                            ? commune.trim() : "");

            utilisateur.setAdresse(adresseComplete.trim());
                        if (adresseComplete.trim().length() > 200) {
                                throw new IllegalArgumentException(
                                                "L'adresse complète ne doit pas dépasser 200 caractères."
                                );
                        }

            // Hachage du mot de passe avant stockage
            utilisateur.setMotDePasse(motDePasseHash);

            // 8. Enregistrement de l'utilisateur via le contrôleur
            utilisateurController.ajouterUtilisateur(utilisateur);

            // 9. Création de l'étudiant
            Etudiant etudiant = new Etudiant();
            etudiant.setContact(telephone);
            etudiant.setUtilisateur(utilisateur);
            etudiant.setDateNaissance(date);
            etudiant.setClasse(classe);
            etudiant.setFiliere(filiere);

            // 10. Enregistrement de l'étudiant via le contrôleur
            etudiantController.ajouterEtudiant(etudiant);

            // 11. Redirection après succès
            response.sendRedirect(
                    request.getContextPath()
                            + "/creer_eleve?success=true"
            );

        } catch (IllegalArgumentException e) {

            afficherErreur(request, response, e.getMessage());

        } catch (Exception e) {

            getServletContext().log(
                    "Erreur lors de l'enregistrement de l'élève",
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
        if (inscriptionToken == null || inscriptionToken.length() != 36) {
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

    private void afficherErreur(
            HttpServletRequest request,
            HttpServletResponse response,
            String message)
            throws ServletException, IOException {

        request.setAttribute("errorMessage", message);

        request.getRequestDispatcher(
                "/inscription/inscription_eleve.jsp"
        ).forward(request, response);
    }
}
