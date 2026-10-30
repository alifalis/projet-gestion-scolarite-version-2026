package dao;

import modele.Utilisateur;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
public class UtilisateurRepository {

    private static final Logger LOGGER =
            Logger.getLogger(UtilisateurRepository.class.getName());

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

    // Enregistrer un utilisateur
    public void save(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur ne peut pas être null."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(utilisateur);

            transaction.commit();

            LOGGER.info("Utilisateur ajouté : " + utilisateur.getNom());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de l'ajout de l'utilisateur.",
                    e
            );

            throw e;
        }
    }

    // Récupérer un utilisateur par son identifiant
    public Utilisateur getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Utilisateur.class, id);

        } catch (RuntimeException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération de l'utilisateur ID " + id,
                    e
            );

            throw e;
        }
    }

    // Rechercher un utilisateur par email
    public Utilisateur getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT u FROM Utilisateur u " +
                    "JOIN FETCH u.role " +
                    "WHERE u.email = :email",
                    Utilisateur.class
            )
            .setParameter("email", email)
            .uniqueResult();

        } catch (RuntimeException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la recherche d'un utilisateur par email.",
                    e
            );

            throw e;
        }
    }

    // Supprimer un utilisateur par son identifiant
    public void deleteById(int id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Utilisateur utilisateur =
                    session.get(Utilisateur.class, id);

            if (utilisateur == null) {
                LOGGER.warning(
                        "Aucun utilisateur trouvé avec l'identifiant " + id
                );

                transaction.rollback();
                return;
            }

            session.remove(utilisateur);
            transaction.commit();

            LOGGER.info("Utilisateur supprimé : ID " + id);

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la suppression de l'utilisateur ID " + id,
                    e
            );

            throw e;
        }
    }

    // Modifier un utilisateur existant
    public void update(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur ne peut pas être null."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Utilisateur existant = session.get(
                    Utilisateur.class,
                    utilisateur.getIdUtilisateur()
            );

            if (existant == null) {
                throw new IllegalArgumentException(
                        "Aucun utilisateur trouvé avec cet identifiant."
                );
            }

            existant.setNom(utilisateur.getNom());
            existant.setPrenom(utilisateur.getPrenom());
            existant.setAdresse(utilisateur.getAdresse());
            existant.setEmail(utilisateur.getEmail());
            existant.setMotDePasse(utilisateur.getMotDePasse());

            // Mettre à jour le rôle uniquement s'il a été fourni
            if (utilisateur.getRole() != null) {
                existant.setRole(utilisateur.getRole());
            }

            transaction.commit();

            LOGGER.info(
                    "Utilisateur mis à jour : " + existant.getNom()
            );

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la mise à jour de l'utilisateur.",
                    e
            );

            throw e;
        }
    }



        // Récupérer la liste de tous les utilisateurs
    public List<Utilisateur> getAll() {
        try (Session session = sessionFactory.openSession()) {

            return session.createQuery(
                    "FROM Utilisateur", Utilisateur.class
            ).getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération de la liste des utilisateurs",
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
