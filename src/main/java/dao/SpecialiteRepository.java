
package dao;

import modele.Specialite;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpecialiteRepository {

    private static final Logger LOGGER =
            Logger.getLogger(SpecialiteRepository.class.getName());

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

    // Ajouter une spécialité
    public void save(Specialite specialite) {
        if (specialite == null) {
            throw new IllegalArgumentException(
                    "La spécialité à enregistrer ne peut pas être null.");
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(specialite);

            transaction.commit();

            LOGGER.info("Spécialité ajoutée : " + specialite.getNom());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de l'ajout de la spécialité.", e);

            throw e;
        }
    }

    // Rechercher une spécialité par son identifiant
    public Specialite getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Specialite.class, id);

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche de la spécialité ID " + id, e);

            throw e;
        }
    }

    // Rechercher une spécialité par son nom
    public Specialite getByNom(String nom) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Specialite s WHERE s.nom = :nom",
                            Specialite.class)
                    .setParameter("nom", nom)
                    .uniqueResult();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche de la spécialité par nom.", e);

            throw e;
        }
    }

    // Récupérer toutes les spécialités
    public List<Specialite> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Specialite s ORDER BY s.nom",
                            Specialite.class)
                    .getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération des spécialités.", e);

            throw e;
        }
    }

    // Modifier une spécialité
    public void update(Specialite specialite) {
        if (specialite == null) {
            throw new IllegalArgumentException(
                    "La spécialité à modifier ne peut pas être null.");
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Specialite specialiteExistante = session.get(
                    Specialite.class, specialite.getIdSpecialite());

            if (specialiteExistante == null) {
                throw new IllegalArgumentException(
                        "Aucune spécialité trouvée avec l'identifiant "
                                + specialite.getIdSpecialite());
            }

            specialiteExistante.setNom(specialite.getNom());

            transaction.commit();

            LOGGER.info("Spécialité mise à jour : " + specialite.getNom());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la mise à jour de la spécialité.", e);

            throw e;
        }
    }

    // Supprimer une spécialité par son identifiant
    public void deleteById(int id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Specialite specialite = session.get(Specialite.class, id);

            if (specialite == null) {
                transaction.rollback();

                LOGGER.warning(
                        "Aucune spécialité trouvée avec l'identifiant " + id);

                return;
            }

            session.remove(specialite);

            transaction.commit();

            LOGGER.info("Spécialité supprimée : ID " + id);

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la suppression de la spécialité ID " + id, e);

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
