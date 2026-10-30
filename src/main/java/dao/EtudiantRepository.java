package dao;

import modele.Etudiant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EtudiantRepository {

    private static final Logger LOGGER =
            Logger.getLogger(EtudiantRepository.class.getName());

    private static final SessionFactory sessionFactory;

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

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // CREATE
    public void save(Etudiant etudiant) {
        checkEtudiant(etudiant);

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(etudiant);

            transaction.commit();

            LOGGER.info(
                    "Étudiant ajouté avec succès : ID "
                            + etudiant.getIdEtudiant()
            );

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de l'ajout de l'étudiant.",
                    ex
            );

            throw ex;
        }
    }

    // READ BY ID
    public Etudiant getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e FROM Etudiant e " +
                            "LEFT JOIN FETCH e.classe " +
                            "JOIN FETCH e.utilisateur " +
                            "WHERE e.idEtudiant = :id",
                            Etudiant.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération de l'étudiant ID " + id,
                    ex
            );

            throw ex;
        }
    }

    // READ BY EMAIL
    public Etudiant getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "L'adresse email ne peut pas être vide."
            );
        }

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e FROM Etudiant e " +
                            "LEFT JOIN FETCH e.classe " +
                            "JOIN FETCH e.utilisateur u " +
                            "WHERE u.email = :email",
                            Etudiant.class
                    )
                    .setParameter("email", email)
                    .uniqueResult();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la recherche de l'étudiant par email.",
                    ex
            );

            throw ex;
        }
    }

    // READ ALL
    public List<Etudiant> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e FROM Etudiant e " +
                            "LEFT JOIN FETCH e.classe " +
                            "JOIN FETCH e.utilisateur " +
                            "ORDER BY e.idEtudiant",
                            Etudiant.class
                    )
                    .getResultList();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération des étudiants.",
                    ex
            );

            throw ex;
        }
    }

    // UPDATE
    public void update(Etudiant etudiant) {
        checkEtudiant(etudiant);

        if (etudiant.getIdEtudiant() <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant de l'étudiant doit être positif."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Etudiant existant = session.get(
                    Etudiant.class,
                    etudiant.getIdEtudiant()
            );

            if (existant == null) {
                throw new IllegalArgumentException(
                        "Étudiant introuvable : "
                                + etudiant.getIdEtudiant()
                );
            }

            existant.setDateNaissance(etudiant.getDateNaissance());
            existant.setContact(etudiant.getContact());
            existant.setClasse(etudiant.getClasse());
            existant.setFiliere(etudiant.getFiliere());
            existant.setUtilisateur(etudiant.getUtilisateur());

            transaction.commit();

            LOGGER.info(
                    "Étudiant mis à jour : ID "
                            + etudiant.getIdEtudiant()
            );

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la mise à jour de l'étudiant.",
                    ex
            );

            throw ex;
        }
    }

    // DELETE
    public void deleteById(int id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Etudiant etudiant = session.get(Etudiant.class, id);

            if (etudiant == null) {
                throw new IllegalArgumentException(
                        "Étudiant introuvable : " + id
                );
            }

            session.remove(etudiant);

            transaction.commit();

            LOGGER.info("Étudiant supprimé : ID " + id);

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la suppression de l'étudiant ID " + id,
                    ex
            );

            throw ex;
        }
    }

    // VALIDATION
    private void checkEtudiant(Etudiant etudiant) {
        if (etudiant == null) {
            throw new IllegalArgumentException(
                    "L'étudiant ne peut pas être null."
            );
        }

        if (etudiant.getDateNaissance() == null) {
            throw new IllegalArgumentException(
                    "La date de naissance est obligatoire."
            );
        }

        if (etudiant.getUtilisateur() == null) {
            throw new IllegalArgumentException(
                    "Un utilisateur doit être associé à l'étudiant."
            );
        }

        if (etudiant.getFiliere() != null
                && etudiant.getFiliere().length() > 50) {
            throw new IllegalArgumentException(
                    "La filière ne doit pas dépasser 50 caractères."
            );
        }

        if (etudiant.getContact() != null
                && etudiant.getContact().length() > 15) {
            throw new IllegalArgumentException(
                    "Le contact ne doit pas dépasser 15 caractères."
            );
        }
    }

    // ROLLBACK
    private void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();

            } catch (RuntimeException ex) {
                LOGGER.log(
                        Level.SEVERE,
                        "Erreur lors du rollback de la transaction.",
                        ex
                );
            }
        }
    }

    // CLOSE SESSION FACTORY
    public static void closeSessionFactory() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
            LOGGER.info("SessionFactory Hibernate fermée.");
        }
    }
}
