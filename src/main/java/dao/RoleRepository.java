
package dao;

import modele.Role;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoleRepository {

    private static final Logger LOGGER =
            Logger.getLogger(RoleRepository.class.getName());

    private static final SessionFactory sessionFactory;

    // Initialisation de Hibernate
    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            LOGGER.info("SessionFactory Hibernate initialisée.");

        } catch (Exception ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de l'initialisation d'Hibernate.",
                    ex
            );
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Ajouter un rôle
    public void add(Role role) {
        if (role == null) {
            throw new IllegalArgumentException(
                    "Le rôle ne peut pas être null."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(role);

            transaction.commit();

            LOGGER.info("Rôle ajouté : " + role.getNomRole());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de l'ajout du rôle.",
                    e
            );

            throw e;
        }
    }

    // Rechercher un rôle par identifiant
    public Role getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Role.class, id);

        } catch (RuntimeException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération du rôle ID " + id,
                    e
            );
            throw e;
        }
    }

    // Récupérer tous les rôles
    public List<Role> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM Role",
                    Role.class
            ).getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération des rôles.",
                    e
            );
            throw e;
        }
    }

    // Rechercher un rôle par son nom
    public Role getByNomRole(String nomRole) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM Role r WHERE r.nomRole = :nom",
                    Role.class
            )
            .setParameter("nom", nomRole)
            .uniqueResult();

        } catch (RuntimeException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la recherche du rôle par son nom.",
                    e
            );
            throw e;
        }
    }

    // Modifier un rôle existant
    public void update(Role role) {
        if (role == null) {
            throw new IllegalArgumentException(
                    "Le rôle ne peut pas être null."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Role existant = session.get(
                    Role.class,
                    role.getIdRole()
            );

            if (existant == null) {
                throw new IllegalArgumentException(
                        "Aucun rôle trouvé avec cet identifiant."
                );
            }

            existant.setNomRole(role.getNomRole());

            transaction.commit();

            LOGGER.info(
                    "Rôle modifié : " + existant.getNomRole()
            );

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la modification du rôle.",
                    e
            );

            throw e;
        }
    }

    // Supprimer un rôle par identifiant
    public void deleteById(int id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Role role = session.get(Role.class, id);

            if (role == null) {
                throw new IllegalArgumentException(
                        "Aucun rôle trouvé avec l'identifiant " + id
                );
            }

            session.remove(role);

            transaction.commit();

            LOGGER.info("Rôle supprimé : ID " + id);

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la suppression du rôle ID " + id,
                    e
            );

            throw e;
        }
    }

    // Annuler une transaction en cas d'erreur
    private void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (RuntimeException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Erreur lors de l'annulation de la transaction.",
                        e
                );
            }
        }
    }

    // Fermer Hibernate à l'arrêt de l'application
    public static void closeSessionFactory() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
            LOGGER.info("SessionFactory Hibernate fermée.");
        }
    }
}
