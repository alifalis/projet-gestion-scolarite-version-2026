package controller.web;

import jakarta.servlet.http.HttpServletRequest;
import modele.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.*;

import java.util.List;
import java.util.Locale;

@Controller
public class AdminPagesController {

    private final CoursService cours;
    private final EnseignantService enseignants;
    private final EtudiantService etudiants;
    private final InscriptionCoursService inscriptions;

    public AdminPagesController(
            CoursService cours, EnseignantService enseignants,
            EtudiantService etudiants, InscriptionCoursService inscriptions) {
        this.cours = cours;
        this.enseignants = enseignants;
        this.etudiants = etudiants;
        this.inscriptions = inscriptions;
    }

    @GetMapping("/listeCours")
    public String cours(HttpServletRequest request) {
        request.setAttribute("cours", cours.lister());
        if ("true".equals(request.getParameter("success"))) {
            request.setAttribute("message", "Le cours a été attribué.");
        }
        return "admin/cours/attribution";
    }

    @GetMapping("/listeInscriptions")
    public String inscriptions(HttpServletRequest request) {
        request.setAttribute("inscriptions", inscriptions.lister());
        return "admin/inscriptions/gestion";
    }

    @GetMapping("/listeProfesseurs")
    public String professeurs(
            @RequestParam(required = false) String searchQuery,
            HttpServletRequest request) {
        List<Enseignant> result = enseignants.lister();
        if (searchQuery != null && !searchQuery.isBlank()) {
            String query = searchQuery.trim().toLowerCase(Locale.ROOT);
            result = result.stream().filter(professeur -> {
                Utilisateur utilisateur = professeur.getUtilisateur();
                return utilisateur != null
                        && contient(utilisateur.getNom(), query)
                        || utilisateur != null && contient(utilisateur.getPrenom(), query)
                        || utilisateur != null && contient(utilisateur.getAdresse(), query)
                        || utilisateur != null && contient(utilisateur.getEmail(), query)
                        || professeur.getSpecialite() != null
                        && contient(professeur.getSpecialite().getNom(), query);
            }).toList();
        }
        request.setAttribute("professeurs", result);
        return "admin/enseignants/liste";
    }

    @GetMapping("/listeEtudiants")
    public String etudiants(
            @RequestParam(required = false) String searchQuery,
            HttpServletRequest request) {
        List<Etudiant> result = etudiants.lister();
        if (searchQuery != null && !searchQuery.isBlank()) {
            String query = searchQuery.trim().toLowerCase(Locale.ROOT);
            result = result.stream().filter(etudiant -> {
                Utilisateur utilisateur = etudiant.getUtilisateur();
                return utilisateur != null
                        && contient(utilisateur.getNom(), query)
                        || utilisateur != null && contient(utilisateur.getPrenom(), query)
                        || utilisateur != null && contient(utilisateur.getAdresse(), query)
                        || utilisateur != null && contient(utilisateur.getEmail(), query)
                        || etudiant.getClasse() != null
                        && contient(etudiant.getClasse().getNom(), query)
                        || contient(etudiant.getFiliere(), query);
            }).toList();
        }
        request.setAttribute("etudiantList", result);
        return "admin/etudiants/liste";
    }

    @PostMapping("/supprimerProfesseur")
    public String supprimerProfesseur(@RequestParam int id) {
        enseignants.supprimer(id);
        return "redirect:/listeProfesseurs";
    }

    @PostMapping("/supprimerEtudiant")
    public String supprimerEtudiant(@RequestParam int id) {
        etudiants.supprimer(id);
        return "redirect:/listeEtudiants";
    }

    @GetMapping("/rechercherProfesseur")
    public String rechercherProfesseur(
            @RequestParam(required = false) String search,
            HttpServletRequest request) {
        if (search == null || search.isBlank()) {
            request.setAttribute("message", "Veuillez entrer un terme de recherche valide.");
        } else {
            String query = search.trim().toLowerCase(Locale.ROOT);
            List<Enseignant> result = enseignants.lister().stream()
                    .filter(e -> e.getUtilisateur() != null
                            && (contient(e.getUtilisateur().getNom(), query)
                            || contient(e.getUtilisateur().getPrenom(), query)
                            || contient(e.getUtilisateur().getEmail(), query)))
                    .toList();
            if (result.isEmpty()) {
                request.setAttribute("message", "Aucun professeur trouvé pour cette recherche.");
            } else {
                request.setAttribute("professeurs", result);
            }
        }
        request.setAttribute("cours", cours.lister());
        return "admin/cours/attribution";
    }

    @PostMapping("/attribuerCours")
    public String attribuerCours(
            @RequestParam int id_professeur,
            @RequestParam String nom_cours,
            @RequestParam String description,
            HttpServletRequest request) {
        try {
            if (nom_cours.isBlank() || description.isBlank()) {
                throw new IllegalArgumentException("Le professeur, le nom et la description sont obligatoires.");
            }
            Enseignant enseignant = enseignants.rechercherParId(id_professeur);
            Cours coursEntity = new Cours();
            coursEntity.setNomCours(nom_cours.trim());
            coursEntity.setDescription(description.trim());
            coursEntity.setEnseignant(enseignant);
            cours.ajouter(coursEntity);
            return "redirect:/listeCours?success=true";
        } catch (RuntimeException exception) {
            request.setAttribute("message", exception.getMessage());
            request.setAttribute("cours", cours.lister());
            return "admin/cours/attribution";
        }
    }

    @GetMapping("/rechercherEtudiant")
    public String rechercherEtudiant(
            @RequestParam(required = false) String searchEtudiant,
            HttpServletRequest request) {
        request.setAttribute("inscriptions", inscriptions.lister());
        if (searchEtudiant == null || searchEtudiant.isBlank()) {
            request.setAttribute("message", "Veuillez saisir un critère de recherche.");
            return "admin/inscriptions/gestion";
        }
        Etudiant selected = etudiants.lister().stream()
                .filter(e -> correspond(e, searchEtudiant.trim()))
                .findFirst().orElse(null);
        request.setAttribute("etudiantSelectionne", selected);
        if (selected == null) {
            request.setAttribute("message", "Aucun étudiant trouvé correspondant au critère.");
        }
        return "admin/inscriptions/gestion";
    }

    @GetMapping("/rechercherCours")
    public String rechercherCours(
            @RequestParam(required = false) String searchCours,
            @RequestParam(required = false) Integer id_etudiant,
            HttpServletRequest request) {
        request.setAttribute("inscriptions", inscriptions.lister());
        if (id_etudiant != null) {
            request.setAttribute("etudiantSelectionne", etudiants.rechercherParId(id_etudiant));
        }
        if (searchCours == null || searchCours.isBlank()) {
            request.setAttribute("message", "Veuillez saisir un critère de recherche.");
            return "admin/inscriptions/gestion";
        }
        String query = searchCours.trim().toLowerCase(Locale.ROOT);
        List<Cours> result = cours.lister().stream()
                .filter(c -> String.valueOf(c.getIdCours()).equals(query)
                        || c.getNomCours().toLowerCase(Locale.ROOT).contains(query))
                .toList();
        request.setAttribute("cours", result);
        if (result.isEmpty()) {
            request.setAttribute("message", "Aucun cours correspondant n'a été trouvé.");
        }
        return "admin/inscriptions/gestion";
    }

    private boolean correspond(Etudiant etudiant, String search) {
        try {
            if (etudiant.getIdEtudiant() == Integer.parseInt(search)) return true;
        } catch (NumberFormatException ignored) {
            // Search by user fields below.
        }
        String query = search.toLowerCase(Locale.ROOT);
        return etudiant.getUtilisateur() != null
                && (contient(etudiant.getUtilisateur().getNom(), query)
                || contient(etudiant.getUtilisateur().getPrenom(), query)
                || contient(etudiant.getUtilisateur().getEmail(), query));
    }

    private boolean contient(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }
}
