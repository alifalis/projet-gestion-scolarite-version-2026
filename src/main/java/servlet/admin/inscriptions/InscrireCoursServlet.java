package servlet.admin.inscriptions;

import controller.CoursController;
import controller.EtudiantController;
import controller.InscriptionCoursController;
import controller.ResultatController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Cours;
import modele.Etudiant;
import modele.InscriptionCours;
import modele.Resultat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@WebServlet(name = "InscrireCoursServlet", value = "/inscrireCours")
public class InscrireCoursServlet extends HttpServlet {

    private static final String LIST_URL = "/listeInscriptions";

    private transient CoursController coursController;
    private transient EtudiantController etudiantController;
    private transient InscriptionCoursController inscriptionController;
    private transient ResultatController resultatController;

    @Override
    public void init() throws ServletException {
        coursController = new CoursController();
        etudiantController = new EtudiantController();
        inscriptionController = new InscriptionCoursController();
        resultatController = new ResultatController();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Etudiant etudiantSelectionne = null;

        try {
            String idEtudiantTexte = request.getParameter("id_etudiant");
            String idCoursTexte = request.getParameter("id_cours");
            String dateTexte = request.getParameter("dateCours");
            String debutTexte = request.getParameter("debutHeure");
            String finTexte = request.getParameter("finHeure");

            if (estVide(idEtudiantTexte) || estVide(idCoursTexte)
                    || estVide(dateTexte) || estVide(debutTexte)
                    || estVide(finTexte)) {
                throw new IllegalArgumentException(
                        "Tous les champs sont obligatoires.");
            }

            int idEtudiant = Integer.parseInt(idEtudiantTexte.trim());
            int idCours = Integer.parseInt(idCoursTexte.trim());
            LocalDate jour = LocalDate.parse(dateTexte.trim());
            LocalDateTime debut = LocalDateTime.of(
                    jour, LocalTime.parse(debutTexte.trim()));
            LocalDateTime fin = LocalDateTime.of(
                    jour, LocalTime.parse(finTexte.trim()));
            if (!fin.isAfter(debut)) {
                throw new IllegalArgumentException(
                        "L'heure de fin doit être après l'heure de début.");
            }

            Etudiant etudiant = etudiantController.rechercherEtudiant(idEtudiant);
            etudiantSelectionne = etudiant;
            Cours cours = coursController.rechercherCours(idCours);
            List<InscriptionCours> inscriptions =
                    inscriptionController.afficherInscriptions(idEtudiant);
            if (inscriptions.stream().anyMatch(inscription ->
                    inscription.getCours().getIdCours() == idCours)) {
                throw new IllegalArgumentException(
                        "Cet étudiant est déjà inscrit à ce cours.");
            }

            InscriptionCours inscription = new InscriptionCours();
            inscription.setEtudiant(etudiant);
            inscription.setCours(cours);
            inscription.setDebutCours(debut);
            inscription.setFinCours(fin);
            inscriptionController.ajouterInscription(inscription);

            boolean resultatExiste = resultatController
                    .afficherResultatsParEtudiant(idEtudiant).stream()
                    .anyMatch(resultat ->
                            resultat.getCours().getIdCours() == idCours);
            if (!resultatExiste) {
                resultatController.ajouterResultat(
                        new Resultat(etudiant, cours, null));
            }
            request.setAttribute("message", "Inscription enregistrée.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
        } catch (RuntimeException e) {
            getServletContext().log("Erreur lors de l'inscription au cours.", e);
            request.setAttribute(
                    "message", "Impossible d'inscrire l'étudiant à ce cours.");
        }

        if (etudiantSelectionne == null) {
            try {
                String id = request.getParameter("id_etudiant");
                if (id != null && !id.isBlank()) {
                    etudiantSelectionne = etudiantController
                            .rechercherEtudiant(Integer.parseInt(id));
                }
            } catch (IllegalArgumentException ignored) {
                // L'identifiant invalide est déjà signalé dans le message.
            }
        }
        request.setAttribute("etudiantSelectionne", etudiantSelectionne);
        request.getRequestDispatcher(LIST_URL).forward(request, response);
    }

    private boolean estVide(String valeur) {
        return valeur == null || valeur.isBlank();
    }
}
