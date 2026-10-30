error id: file://<WORKSPACE>/src/main/servlet/admin/liste/etudiant_professeur/nonhibernate/Dao/ProfesseurRepository.java:admin/liste/etudiant_professeur/nonhibernate/modele/Professeur#
file://<WORKSPACE>/src/main/servlet/admin/liste/etudiant_professeur/nonhibernate/Dao/ProfesseurRepository.java
empty definition using pc, found symbol in pc: admin/liste/etudiant_professeur/nonhibernate/modele/Professeur#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 146
uri: file://<WORKSPACE>/src/main/servlet/admin/liste/etudiant_professeur/nonhibernate/Dao/ProfesseurRepository.java
text:
```scala
package admin.liste.etudiant_professeur.nonhibernate.Dao;

import admin.ExecuteSchema;
import admin.liste.etudiant_professeur.nonhibernate.modele.@@Professeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesseurRepository {

    private static final String DB_URL = ExecuteSchema.getDbUrl()+ "/jee_project";
    private static final String DB_USER = ExecuteSchema.getDbUser(); // Modifier si nécessaire
    private static final String DB_PASSWORD = ExecuteSchema.getDbPassword(); // Modifier si nécessaire


    public List<Professeur> getAllProfesseurs() throws SQLException {
        List<Professeur> professeurs = new ArrayList<>();
        String sql = "SELECT p.id_enseignant, u.nom, u.prenom, u.adresse, u.email, p.contact, p.specialite " +
                "FROM enseignants p " +
                "JOIN utilisateurs u ON p.id_utilisateur = u.id_utilisateur";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Professeur professeur = new Professeur(
                        rs.getInt("id_enseignant"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("specialite")
                );
                professeurs.add(professeur);
            }
        }
        return professeurs;
    }

    // Méthode pour supprimer un professeur
    public boolean supprimerProfesseur(int idProfesseur) throws SQLException {
        // Requête pour récupérer l'id_utilisateur du professeur
        String sqlSelectUtilisateur = "SELECT id_utilisateur FROM enseignants WHERE id_enseignant = ?";

        // Requête pour supprimer le professeur
        String sqlDeleteProfesseur = "DELETE FROM enseignants WHERE id_enseignant = ?";

        // Requête pour supprimer l'utilisateur associé
        String sqlDeleteUtilisateur = "DELETE FROM utilisateurs WHERE id_utilisateur = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Démarrage de la transaction
            conn.setAutoCommit(false);

            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelectUtilisateur);
                 PreparedStatement stmtDeleteProfesseur = conn.prepareStatement(sqlDeleteProfesseur);
                 PreparedStatement stmtDeleteUtilisateur = conn.prepareStatement(sqlDeleteUtilisateur)) {

                // Étape 1 : Récupérer l'id_utilisateur associé
                stmtSelect.setInt(1, idProfesseur);
                ResultSet rs = stmtSelect.executeQuery();

                if (rs.next()) {
                    int idUtilisateur = rs.getInt("id_utilisateur");

                    // Étape 2 : Supprimer le professeur
                    stmtDeleteProfesseur.setInt(1, idProfesseur);
                    stmtDeleteProfesseur.executeUpdate();

                    // Étape 3 : Supprimer l'utilisateur associé
                    stmtDeleteUtilisateur.setInt(1, idUtilisateur);
                    stmtDeleteUtilisateur.executeUpdate();

                    // Validation de la transaction
                    conn.commit();
                    return true; // Suppression réussie
                } else {
                    // Pas de professeur trouvé
                    conn.rollback();
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback(); // Annulation de la transaction en cas d'erreur
                throw e;
            } finally {
                conn.setAutoCommit(true); // Réinitialiser le mode auto-commit
            }
        }
    }

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: admin/liste/etudiant_professeur/nonhibernate/modele/Professeur#