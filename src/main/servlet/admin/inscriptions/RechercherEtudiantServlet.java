package admin.inscriptions;

import controller.EtudiantController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Etudiant;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "RechercherEtudiantServlet", value = "/rechercherEtudiant")
public class RechercherEtudiantServlet extends HttpServlet {

    private transient EtudiantController etudiantController;

    @Override
    public void init() throws ServletException {
        etudiantController = new EtudiantController();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String recherche = request.getParameter("searchEtudiant");
        if (recherche == null || recherche.isBlank()) {
            request.setAttribute(
                    "message", "Veuillez saisir un critère de recherche.");
            request.getRequestDispatcher("/listeInscriptions")
                    .forward(request, response);
            return;
        }

        List<Etudiant> etudiants = etudiantController.afficherEtudiants();
        Etudiant etudiantSelectionne = null;
        try {
            int id = Integer.parseInt(recherche.trim());
            etudiantSelectionne = etudiants.stream()
                    .filter(etudiant -> etudiant.getIdEtudiant() == id)
                    .findFirst()
                    .orElse(null);
        } catch (NumberFormatException e) {
            String critere = recherche.trim().toLowerCase(Locale.ROOT);
            etudiantSelectionne = etudiants.stream()
                    .filter(etudiant -> etudiant.getUtilisateur() != null
                            && (contient(etudiant.getUtilisateur().getNom(), critere)
                                || contient(etudiant.getUtilisateur().getPrenom(), critere)
                                || contient(etudiant.getUtilisateur().getEmail(), critere)))
                    .findFirst()
                    .orElse(null);
        }

        request.setAttribute("etudiantSelectionne", etudiantSelectionne);
        if (etudiantSelectionne == null) {
            request.setAttribute(
                    "message", "Aucun étudiant trouvé correspondant au critère.");
        }
        request.getRequestDispatcher("/listeInscriptions")
                .forward(request, response);
    }

    private boolean contient(String valeur, String critere) {
        return valeur != null && valeur.toLowerCase(Locale.ROOT).contains(critere);
    }


}

