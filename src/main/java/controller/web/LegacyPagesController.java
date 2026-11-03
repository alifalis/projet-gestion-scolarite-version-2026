package controller.web;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modele.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;

@Controller
public class LegacyPagesController {

    private final ClasseService classes;
    private final CoursService cours;
    private final EnseignantService enseignants;
    private final EtudiantService etudiants;
    private final InscriptionAnneeService inscriptionsAnnee;
    private final InscriptionCoursService inscriptionsCours;
    private final ResultatService resultats;
    private final RoleService roles;
    private final SpecialiteService specialites;
    private final UtilisateurService utilisateurs;

    public LegacyPagesController(
            ClasseService classes, CoursService cours,
            EnseignantService enseignants, EtudiantService etudiants,
            InscriptionAnneeService inscriptionsAnnee,
            InscriptionCoursService inscriptionsCours, ResultatService resultats,
            RoleService roles, SpecialiteService specialites,
            UtilisateurService utilisateurs) {
        this.classes = classes;
        this.cours = cours;
        this.enseignants = enseignants;
        this.etudiants = etudiants;
        this.inscriptionsAnnee = inscriptionsAnnee;
        this.inscriptionsCours = inscriptionsCours;
        this.resultats = resultats;
        this.roles = roles;
        this.specialites = specialites;
        this.utilisateurs = utilisateurs;
    }

    @GetMapping("/creer_eleve")
    public String creerEleve(HttpServletRequest request) {
        request.setAttribute("classes", classes.lister());
        return "inscription/inscription_eleve";
    }

    @PostMapping("/creer_eleve")
    public String preparerEleve(HttpServletRequest request) {
        copierParametres(request, "nom", "prenom", "telephone", "email",
                "numero_adresse", "adresse", "code_postal", "commune",
                "date_naissance", "classe", "specialite");
        String password = request.getParameter("mot_de_passe");
        if (password == null || password.length() < 8) {
            return erreurEleve(request, "Le mot de passe doit contenir au moins 8 caractères.");
        }
        String token = UUID.randomUUID().toString();
        request.getSession().setAttribute("pendingStudentPassword:" + token,
                BCrypt.hashpw(password, BCrypt.gensalt()));
        request.setAttribute("inscriptionToken", token);
        return "inscription/afficherEleve";
    }

    @PostMapping("/enregistrer_eleve")
    public String enregistrerEleve(HttpServletRequest request) {
        try {
            String token = request.getParameter("inscriptionToken");
            String hash = consommerMotDePasse(request, "pendingStudentPassword:", token);
            Utilisateur utilisateur = utilisateur(request, hash, "Etudiant");
            Classe classe = classes.rechercherParNom(request.getParameter("classe"));
            Etudiant etudiant = new Etudiant();
            etudiant.setUtilisateur(utilisateur);
            etudiant.setDateNaissance(dateNaissance(request));
            etudiant.setContact(request.getParameter("telephone"));
            etudiant.setClasse(classe);
            etudiant.setFiliere(request.getParameter("specialite"));
            etudiants.ajouter(etudiant);
            return "redirect:/creer_eleve?success=true";
        } catch (RuntimeException exception) {
            return erreurEleve(request, exception.getMessage());
        }
    }

    @GetMapping("/creer_enseignant")
    public String creerEnseignant(HttpServletRequest request) {
        request.setAttribute("specialites", specialites.lister());
        return "inscription/inscription_enseignant";
    }

    @GetMapping("/mettreAJourEtudiant")
    public String afficherReinscription(HttpServletRequest request) {
        request.setAttribute("classes", classes.lister());
        return "inscription/reinscription";
    }

    @PostMapping("/mettreAJourEtudiant")
    public String mettreAJourEtudiant(HttpServletRequest request) {
        request.setAttribute("classes", classes.lister());
        try {
            int idEtudiant = Integer.parseInt(request.getParameter("id_etudiant"));
            Etudiant etudiant = etudiants.rechercherParId(idEtudiant);
            Utilisateur utilisateur = etudiant.getUtilisateur();

            utilisateur.setAdresse(adresse(request));
            utilisateur.setEmail(obligatoire(request, "email"));
            etudiant.setContact(obligatoire(request, "contact"));
            etudiant.setClasse(classes.rechercherParNom(obligatoire(request, "classe")));
            etudiant.setFiliere(obligatoire(request, "filiere"));

            utilisateurs.modifierUtilisateur(utilisateur);
            etudiants.modifier(etudiant);
            request.setAttribute("messageSucces", "L'étudiant a été réinscrit avec succès.");
        } catch (RuntimeException exception) {
            request.setAttribute("messageErreur", exception.getMessage());
        }
        return "inscription/reinscription";
    }

    @PostMapping("/creer_enseignant")
    public String preparerEnseignant(HttpServletRequest request) {
        copierParametres(request, "nom", "prenom", "telephone", "email",
                "numero_adresse", "adresse", "code_postal", "commune",
                "date_naissance", "specialite");
        String password = request.getParameter("mot_de_passe");
        if (password == null || password.length() < 8) {
            return erreurEnseignant(request, "Le mot de passe doit contenir au moins 8 caractères.");
        }
        String token = UUID.randomUUID().toString();
        request.getSession().setAttribute("pendingTeacherPassword:" + token,
                BCrypt.hashpw(password, BCrypt.gensalt()));
        request.setAttribute("inscriptionToken", token);
        return "inscription/afficherEnseignant";
    }

    @PostMapping("/enregister_enseignant")
    public String enregistrerEnseignant(HttpServletRequest request) {
        try {
            String hash = consommerMotDePasse(request, "pendingTeacherPassword:",
                    request.getParameter("inscriptionToken"));
            Utilisateur utilisateur = utilisateur(request, hash, "Enseignant");
            Enseignant enseignant = new Enseignant();
            enseignant.setUtilisateur(utilisateur);
            enseignant.setDateNaissance(dateNaissance(request));
            enseignant.setContact(request.getParameter("telephone"));
            enseignant.setSpecialite(specialites.rechercherParNom(request.getParameter("specialite")));
            enseignants.ajouter(enseignant);
            return "redirect:/creer_enseignant?success=true";
        } catch (RuntimeException exception) {
            return erreurEnseignant(request, exception.getMessage());
        }
    }

    @GetMapping("/listeResultats")
    public String listeResultats(HttpServletRequest request) {
        Integer id = sessionId(request, "prof", "id_professeur");
        if (id == null) return "redirect:/connexionProf";
        request.setAttribute("resultats", resultats.rechercherParEnseignant(id));
        return "prof/gestion_notes";
    }

    @PostMapping("/modifierNotes")
    public String modifierNotes(HttpServletRequest request) {
        Integer id = sessionId(request, "prof", "id_professeur");
        if (id == null) return "redirect:/connexionProf";
        List<Resultat> owned = resultats.rechercherParEnseignant(id);
        Map<Integer, Resultat> byId = new HashMap<>();
        owned.forEach(r -> byId.put(r.getIdResultat(), r));
        String[] ids = request.getParameterValues("id_resultat");
        if (ids != null) {
            for (String value : ids) {
                String note = request.getParameter("note_" + value);
                if (note != null && !note.isBlank()) {
                    Resultat resultat = byId.get(Integer.parseInt(value));
                    if (resultat == null) throw new IllegalArgumentException("Résultat invalide.");
                    BigDecimal parsed = new BigDecimal(note);
                    if (parsed.compareTo(BigDecimal.ZERO) < 0
                            || parsed.compareTo(BigDecimal.valueOf(20)) > 0) {
                        throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
                    }
                    resultat.setNote(parsed);
                    resultats.modifier(resultat);
                }
            }
        }
        request.setAttribute("message", "Les notes sont enregistrées.");
        request.setAttribute("resultats", resultats.rechercherParEnseignant(id));
        return "prof/gestion_notes";
    }

    @GetMapping("/telechargerEtudiantsCSV")
    public void exporterEtudiants(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (sessionId(request, "admin", "id_admin") == null) {
            response.sendRedirect(request.getContextPath() + "/connexionAdmin");
            return;
        }
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=etudiants.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.write('\uFEFF');
            writer.println("ID,Nom,Prénom,Email,Contact,Classe,Filière");
            for (Etudiant e : etudiants.lister()) {
                if (e.getUtilisateur() == null) continue;
                writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                        csv(e.getIdEtudiant()), csv(e.getUtilisateur().getNom()),
                        csv(e.getUtilisateur().getPrenom()), csv(e.getUtilisateur().getEmail()),
                        csv(e.getContact()), csv(e.getClasse() == null ? "N/A" : e.getClasse().getNom()),
                        csv(e.getFiliere()));
            }
        }
    }

    @GetMapping("/telechargerProfesseursCSV")
    public void exporterProfesseurs(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (sessionId(request, "admin", "id_admin") == null) {
            response.sendRedirect(request.getContextPath() + "/connexionAdmin");
            return;
        }
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=professeurs.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.write('\uFEFF');
            writer.println("ID,Nom,Prénom,Email,Spécialité");
            for (Enseignant e : enseignants.lister()) {
                if (e.getUtilisateur() == null) continue;
                writer.printf("%s,%s,%s,%s,%s%n", csv(e.getIdEnseignant()),
                        csv(e.getUtilisateur().getNom()), csv(e.getUtilisateur().getPrenom()),
                        csv(e.getUtilisateur().getEmail()),
                        csv(e.getSpecialite() == null ? "N/A" : e.getSpecialite().getNom()));
            }
        }
    }

    @GetMapping("/telechargerReleveNotes")
    public void releve(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer id = sessionId(request, "etudiant", "id_etudiant");
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/loginController");
            return;
        }
        Etudiant etudiant = etudiants.rechercherParId(id);
        List<Resultat> notes = resultats.rechercherParEtudiant(id);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=releve_notes.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            document.add(new Paragraph("Relevé de notes"));
            document.add(new Paragraph("Nom : " + etudiant.getUtilisateur().getPrenom()
                    + " " + etudiant.getUtilisateur().getNom()));
            PdfPTable table = new PdfPTable(2);
            table.addCell("Cours");
            table.addCell("Note");
            for (Resultat resultat : notes) {
                table.addCell(resultat.getCours().getNomCours());
                table.addCell(resultat.getNote() == null ? "Non noté" : resultat.getNote() + "/20");
            }
            document.add(table);
        } catch (DocumentException exception) {
            throw new IOException("Erreur lors de la génération du PDF.", exception);
        } finally {
            if (document.isOpen()) document.close();
        }
    }

    @PostMapping("/inscrireCours")
    public String inscrireCours(HttpServletRequest request) {
        try {
            int idEtudiant = Integer.parseInt(request.getParameter("id_etudiant"));
            int idCours = Integer.parseInt(request.getParameter("id_cours"));
            LocalDate date = LocalDate.parse(request.getParameter("dateCours"));
            LocalDateTime debut = LocalDateTime.of(date, LocalTime.parse(request.getParameter("debutHeure")));
            LocalDateTime fin = LocalDateTime.of(date, LocalTime.parse(request.getParameter("finHeure")));
            if (!fin.isAfter(debut)) throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début.");
            Etudiant etudiant = etudiants.rechercherParId(idEtudiant);
            Cours coursEntity = cours.rechercherParId(idCours);
            InscriptionCours inscription = new InscriptionCours();
            inscription.setEtudiant(etudiant);
            inscription.setCours(coursEntity);
            inscription.setDebutCours(debut);
            inscription.setFinCours(fin);
            inscriptionsCours.ajouter(inscription);
            resultats.ajouter(new Resultat(etudiant, coursEntity, null));
            request.setAttribute("message", "Inscription enregistrée.");
        } catch (RuntimeException exception) {
            request.setAttribute("message", exception.getMessage());
        }
        return "forward:/listeInscriptions";
    }

    @PostMapping("/notifierEtudiants")
    public String notifier(HttpServletRequest request) {
        Integer id = sessionId(request, "prof", "id_professeur");
        if (id == null) return "redirect:/connexionProf";
        request.setAttribute("message", "La notification sera envoyée par le service de messagerie configuré.");
        request.setAttribute("resultats", resultats.rechercherParEnseignant(id));
        return "prof/gestion_notes";
    }

    private Utilisateur utilisateur(HttpServletRequest request, String hash, String roleName) {
        Utilisateur user = new Utilisateur();
        user.setNom(request.getParameter("nom"));
        user.setPrenom(request.getParameter("prenom"));
        user.setEmail(request.getParameter("email"));
        user.setAdresse(Optional.ofNullable(request.getParameter("adresse")).orElse(""));
        user.setMotDePasse(hash);
        user.setRole(roles.rechercherRoleParNom(roleName));
        return utilisateurs.ajouterUtilisateur(user);
    }

    private String consommerMotDePasse(HttpServletRequest request, String prefix, String token) {
        if (token == null || token.length() != 36) return null;
        Object value = request.getSession().getAttribute(prefix + token);
        request.getSession().removeAttribute(prefix + token);
        return value instanceof String ? (String) value : null;
    }

    private Integer sessionId(HttpServletRequest request, String role, String attribute) {
        HttpSession session = request.getSession(false);
        if (session == null || !role.equals(session.getAttribute("user"))) return null;
        Object value = session.getAttribute(attribute);
        return value instanceof Integer ? (Integer) value : null;
    }

    private void copierParametres(HttpServletRequest request, String... names) {
        for (String name : names) request.setAttribute(name, request.getParameter(name));
    }

    private String erreurEleve(HttpServletRequest request, String message) {
        request.setAttribute("errorMessage", message);
        request.setAttribute("classes", classes.lister());
        return "inscription/inscription_eleve";
    }

    private String erreurEnseignant(HttpServletRequest request, String message) {
        request.setAttribute("errorMessage", message);
        request.setAttribute("specialites", specialites.lister());
        return "inscription/inscription_enseignant";
    }

    private String adresse(HttpServletRequest request) {
        return obligatoire(request, "numero_adresse") + " "
                + obligatoire(request, "adresse") + ", "
                + obligatoire(request, "code_postal") + " "
                + obligatoire(request, "commune");
    }

    private Date dateNaissance(HttpServletRequest request) {
        String valeur = obligatoire(request, "date_naissance");
        try {
            return java.sql.Date.valueOf(LocalDate.parse(valeur));
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("La date de naissance est invalide.", exception);
        }
    }

    private String obligatoire(HttpServletRequest request, String nom) {
        String valeur = request.getParameter(nom);
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le champ " + nom + " est obligatoire.");
        }
        return valeur.trim();
    }

    private String csv(Object value) {
        String text = value == null ? "" : value.toString();
        if (!text.isEmpty() && "=+-@".indexOf(text.charAt(0)) >= 0) text = "'" + text;
        return "\"" + text.replace("\"", "\"\"") + "\"";
    }
}
