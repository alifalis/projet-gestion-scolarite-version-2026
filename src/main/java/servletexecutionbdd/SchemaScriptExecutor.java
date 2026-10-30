package servletexecutionbdd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class SchemaScriptExecutor {

    // Configuration de la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_USER = "root"; // à personnaliser
    private static final String DB_PASSWORD = "root"; // à personnaliser
    private static final String SCHEMA_FILE = "/db/schema.sql"; // Chemin relatif dans le classpath

    /**
     * Méthode pour créer ou initialiser la base de données en exécutant un script SQL.
     */
    private SchemaScriptExecutor() {
    }

    public static void initializeDatabase() {
        // Tomcat charge les bibliothèques de WEB-INF/lib avec son chargeur
        // propre. DriverManager ne découvre pas toujours automatiquement ces
        // pilotes depuis une application web : on enregistre donc le pilote
        // MySQL avant d'ouvrir la connexion.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Pilote MySQL introuvable. Vérifiez la dépendance mysql-connector-j.", e);
        }

        try {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement statement = connection.createStatement();
                 InputStream inputStream = SchemaScriptExecutor.class.getResourceAsStream(SCHEMA_FILE)) {
                if (inputStream == null) {
                    throw new IllegalStateException("Script SQL introuvable : " + SCHEMA_FILE);
                }
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                StringBuilder sqlStatement = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
                        continue;
                    }

                    sqlStatement.append(trimmedLine).append(' ');
                    if (trimmedLine.endsWith(";")) {
                        statement.execute(sqlStatement.toString());
                        sqlStatement.setLength(0);
                    }
                }
                if (!sqlStatement.isEmpty()) {
                    statement.execute(sqlStatement.toString());
                }
            }
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Impossible d'initialiser le schéma SQL.", e);
        }
    }
}
