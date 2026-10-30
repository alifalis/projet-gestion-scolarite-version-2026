
package dao;

import modele.Classe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClasseRepository {

    private static final Logger LOGGER =
            Logger.getLogger(ClasseRepository.class.getName());

    private static final SessionFactory sessionFactory;

    // Initialisation de la SessionFactory Hibernate
    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            LOGGER.info("SessionFactory Hibernate initialisée.");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de l'initialisation d'Hibernate.", ex);

            throw new ExceptionInInitializerError(ex);
        }
    }

    // Ajouter une classe
    public void save(Classe classe) {
        if (classe == null) {
            throw new IllegalArgumentException(
                    "La classe à enregistrer ne peut pas être null.");
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(classe);

            transaction.commit();

            LOGGER.info("Classe ajoutée : " + classe.getNom());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de l'ajout de la classe.", e);

            throw e;
        }
    }

    // Rechercher une classe par son identifiant
    public Classe getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Classe.class, id);

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche de la classe ID " + id, e);

            throw e;
        }
    }

    // Rechercher une classe par son nom
    public Classe getByNom(String nom) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Classe c WHERE c.nom = :nom",
                            Classe.class)
                    .setParameter("nom", nom)
                    .uniqueResult();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche de la classe par nom.", e);

            throw e;
        }
    }

    // Récupérer toutes les classes
    public List<Classe> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Classe c ORDER BY c.nom",
                            Classe.class)
                    .getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération des classes.", e);

            throw e;
        }
    }

    // Modifier une classe
    public void update(Classe classe) {
        if (classe == null) {
            throw new IllegalArgumentException(
                    "La classe à modifier ne peut pas être null.");
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Classe classeExistante = session.get(
                    Classe.class, classe.getIdClasse());

            if (classeExistante == null) {
                throw new IllegalArgumentException(
                        "Aucune classe trouvée avec l'identifiant "
                                + classe.getIdClasse());
            }

            classeExistante.setNom(classe.getNom());

            transaction.commit();

            LOGGER.info("Classe mise à jour : " + classe.getNom());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la mise à jour de la classe.", e);

            throw e;
        }
    }

    // Supprimer une classe par son identifiant
    public void deleteById(int id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Classe classe = session.get(Classe.class, id);

            if (classe == null) {
                transaction.rollback();

                LOGGER.warning(
                        "Aucune classe trouvée avec l'identifiant " + id);

                return;
            }

            session.remove(classe);

            transaction.commit();

            LOGGER.info("Classe supprimée : ID " + id);

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la suppression de la classe ID " + id, e);

            throw e;
        }
    }

    // Annuler la transaction en cas d'erreur
    private void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();

            } catch (RuntimeException e) {
                LOGGER.log(Level.SEVERE,
                        "Erreur lors de l'annulation de la transaction.", e);
            }
        }
    }

    // Fermer la SessionFactory à l'arrêt de l'application
    public static void closeSessionFactory() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();

            LOGGER.info("SessionFactory Hibernate fermée.");
        }
    }
}
