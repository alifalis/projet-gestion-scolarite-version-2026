package controller.web;

import jakarta.servlet.http.HttpServletRequest;
import modele.Etudiant;
import modele.InscriptionCours;
import modele.Resultat;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.EtudiantService;
import service.InscriptionCoursService;
import service.ResultatService;
import service.UtilisateurService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class StudentLoginController {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final EtudiantService etudiants;
    private final InscriptionCoursService inscriptions;
    private final ResultatService resultats;
    private final UtilisateurService utilisateurs;

    public StudentLoginController(
            EtudiantService etudiants, InscriptionCoursService inscriptions,
            ResultatService resultats, UtilisateurService utilisateurs) {
        this.etudiants = etudiants;
        this.inscriptions = inscriptions;
        this.resultats = resultats;
        this.utilisateurs = utilisateurs;
    }

    @GetMapping("/loginController")
    public String page() {
        return "login/etudiant/login";
    }

    @PostMapping("/loginController")
    public String connexion(
            @RequestParam(name = "email_etudiant", required = false) String email,
            @RequestParam(name = "mot_de_passe", required = false) String password,
            HttpServletRequest request) {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            return erreur(request, "L'adresse e-mail et le mot de passe sont obligatoires.");
        }
        try {
            Etudiant etudiant = etudiants.rechercherParEmail(email.trim());
            if (etudiant == null || etudiant.getUtilisateur() == null
                    || !motDePasseValide(password, etudiant)) {
                return erreur(request, "Adresse e-mail ou mot de passe incorrect.");
            }
            List<Resultat> notes = resultats.rechercherParEtudiant(etudiant.getIdEtudiant());
            List<InscriptionCours> cours = inscriptions.rechercherParEtudiant(etudiant.getIdEtudiant());
            request.getSession().invalidate();
            var session = request.getSession(true);
            session.setAttribute("user", "etudiant");
            session.setAttribute("id_etudiant", etudiant.getIdEtudiant());
            request.setAttribute("nomEtudiant", etudiant.getUtilisateur().getPrenom()
                    + " " + etudiant.getUtilisateur().getNom());
            request.setAttribute("resultats", notes);
            request.setAttribute("calendrier", calendrier(cours));
            request.setAttribute("moyenne", moyenne(notes));
            return "etudiant/notes";
        } catch (RuntimeException exception) {
            return erreur(request, "La connexion est momentanément indisponible.");
        }
    }

    private boolean motDePasseValide(String password, Etudiant etudiant) {
        String stored = etudiant.getUtilisateur().getMotDePasse();
        if (stored == null) return false;
        if (stored.startsWith("$2")) return BCrypt.checkpw(password, stored);
        if (!stored.equals(password)) return false;
        etudiant.getUtilisateur().setMotDePasse(BCrypt.hashpw(password, BCrypt.gensalt()));
        utilisateurs.modifierUtilisateur(etudiant.getUtilisateur());
        return true;
    }

    private BigDecimal moyenne(List<Resultat> notes) {
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (Resultat resultat : notes) {
            if (resultat.getNote() != null) {
                total = total.add(resultat.getNote());
                count++;
            }
        }
        return count == 0 ? null : total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    private List<Map<String, String>> calendrier(List<InscriptionCours> cours) {
        List<Map<String, String>> result = new ArrayList<>();
        for (InscriptionCours inscription : cours) {
            Map<String, String> item = new HashMap<>();
            item.put("debut", date(inscription.getDebutCours()));
            item.put("fin", date(inscription.getFinCours()));
            item.put("nom", inscription.getCours() == null ? "Cours" : inscription.getCours().getNomCours());
            result.add(item);
        }
        return result;
    }

    private String date(LocalDateTime value) {
        return value == null ? "" : value.format(DATE_FORMAT);
    }

    private String erreur(HttpServletRequest request, String message) {
        request.setAttribute("error", message);
        return "login/etudiant/login";
    }
}
