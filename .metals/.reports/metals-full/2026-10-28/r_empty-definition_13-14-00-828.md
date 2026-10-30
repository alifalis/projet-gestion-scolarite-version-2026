error id: file://<WORKSPACE>/src/main/servlet/inscription/eleve/CreerEleveServlet.java:_empty_/UtilisateurRepository#
file://<WORKSPACE>/src/main/servlet/inscription/eleve/CreerEleveServlet.java
empty definition using pc, found symbol in pc: _empty_/UtilisateurRepository#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 464
uri: file://<WORKSPACE>/src/main/servlet/inscription/eleve/CreerEleveServlet.java
text:
```scala

package inscription.eleve;

import dao.UtilisateurRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Creer_eleve", urlPatterns = "/creer_eleve")
public class CreerEleveServlet extends HttpServlet {

    private transient Utili@@sateurRepository utilisateurRepository;

    @Override
    public void init() throws ServletException {
        utilisateurRepository = new UtilisateurRepository();
    }

    // Afficher le formulaire d'inscription
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

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
 estVide(telephone) || estVide(email)
 estVide(motDePasse) || estVide(dateNaissance)
 estVide(classe) || estVide(specialite)) {

            afficherErreur(
                    request,
                    response,
                    "Veuillez remplir tous les champs obligatoires."
            );
            return;
        }

        // 3. Vérifier si l'adresse e-mail existe déjà
        try {
            if (utilisateurRepository.getUtilisateurByEmail(
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
                    "Erreur lors de la vérification de l'e-mail", e
            );

            afficherErreur(
                    request,
                    response,
                    "Impossible de vérifier l'adresse e-mail. Réessayez."
            );
            return;
        }

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

        // Ne pas afficher ni transmettre le mot de passe au récapitulatif.
        // Attention : il ne sera pas automatiquement disponible
        // lors d'une nouvelle soumission HTTP.

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

        request.getRequestDispatcher(
                "/inscription/inscription_eleve.jsp"
        ).forward(request, response);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/UtilisateurRepository#