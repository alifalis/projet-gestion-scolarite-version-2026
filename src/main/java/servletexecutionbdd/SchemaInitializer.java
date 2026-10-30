package servletexecutionbdd;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SchemaInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Démarrage de l'application - Initialisation du schéma de la base de données.");
        try {
            SchemaScriptExecutor.initializeDatabase();
            System.out.println("Schéma de la base de données initialisé avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du schéma de la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Arrêt de l'application - Libération des ressources.");
    }
}
