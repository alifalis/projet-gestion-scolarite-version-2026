
package dao;

import modele.Enseignant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnseignantRepository {

    private static final Logger LOGGER =
            Logger.getLogger(EnseignantRepository.class.getName());

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

    // CREATE
    public void save(Enseignant enseignant) {
        checkEnseignant(enseignant);

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(enseignant);

            transaction.commit();

            LOGGER.info(
                    "Enseignant ajouté : ID "
                            + enseignant.getIdEnseignant()
            );

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de l'ajout de l'enseignant.",
                    ex
            );

            throw ex;
        }
    }

    // READ BY ID
    public Enseignant getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e FROM Enseignant e " +
                            "LEFT JOIN FETCH e.specialite " +
                            "JOIN FETCH e.utilisateur " +
                            "WHERE e.idEnseignant = :id",
                            Enseignant.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération de l'enseignant ID " + id,
                    ex
            );

            throw ex;
        }
    }

    // READ ALL
    public List<Enseignant> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e FROM Enseignant e " +
                            "LEFT JOIN FETCH e.specialite " +
                            "JOIN FETCH e.utilisateur " +
                            "ORDER BY e.idEnseignant",
                            Enseignant.class
                    )
                    .getResultList();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération des enseignants.",
                    ex
            );

            throw ex;
        }
    }

    // READ BY EMAIL
    public Enseignant getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "L'adresse email ne peut pas être vide."
            );
        }

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e FROM Enseignant e " +
                            "LEFT JOIN FETCH e.specialite " +
                            "JOIN FETCH e.utilisateur u " +
                            "JOIN FETCH u.role " +
                            "WHERE u.email = :email",
                            Enseignant.class
                    )
                    .setParameter("email", email)
                    .uniqueResult();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la recherche de l'enseignant par email.",
                    ex
            );

            throw ex;
        }
    }

    // SEARCH
    public List<Enseignant> rechercher(String recherche) {
        if (recherche == null || recherche.isBlank()) {
            return getAll();
        }

        String recherchePreparee = "%" + recherche.trim() + "%";

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e FROM Enseignant e " +
                            "LEFT JOIN FETCH e.specialite " +
                            "JOIN FETCH e.utilisateur u " +
                            "WHERE LOWER(u.nom) LIKE LOWER(:recherche) " +
                            "OR LOWER(u.prenom) LIKE LOWER(:recherche) " +
                            "OR CAST(e.idEnseignant AS string) LIKE :recherche " +
                            "ORDER BY e.idEnseignant",
                            Enseignant.class
                    )
                    .setParameter("recherche", recherchePreparee)
                    .getResultList();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la recherche des enseignants.",
                    ex
            );

            throw ex;
        }
    }

    // UPDATE
    public void update(Enseignant enseignant) {
        checkEnseignant(enseignant);

        if (enseignant.getIdEnseignant() <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant de l'enseignant doit être positif."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Enseignant existant = session.get(
                    Enseignant.class,
                    enseignant.getIdEnseignant()
            );

            if (existant == null) {
                throw new IllegalArgumentException(
                        "Enseignant introuvable : "
                                + enseignant.getIdEnseignant()
                );
            }

            existant.setDateNaissance(enseignant.getDateNaissance());
            existant.setContact(enseignant.getContact());
            existant.setSpecialite(enseignant.getSpecialite());
            existant.setUtilisateur(enseignant.getUtilisateur());

            transaction.commit();

            LOGGER.info(
                    "Enseignant mis à jour : ID "
                            + enseignant.getIdEnseignant()
            );

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la mise à jour de l'enseignant.",
                    ex
            );

            throw ex;
        }
    }

    // DELETE BY ID
    public void deleteById(int id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Enseignant enseignant = session.get(Enseignant.class, id);

            if (enseignant == null) {
                throw new IllegalArgumentException(
                        "Enseignant introuvable : " + id
                );
            }

            session.remove(enseignant);

            transaction.commit();

            LOGGER.info("Enseignant supprimé : ID " + id);

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la suppression de l'enseignant ID " + id,
                    ex
            );

            throw ex;
        }
    }

    // VALIDATION
    private void checkEnseignant(Enseignant enseignant) {
        if (enseignant == null) {
            throw new IllegalArgumentException(
                    "L'enseignant ne peut pas être null."
            );
        }

        if (enseignant.getDateNaissance() == null) {
            throw new IllegalArgumentException(
                    "La date de naissance est obligatoire."
            );
        }

        if (enseignant.getSpecialite() == null) {
            throw new IllegalArgumentException(
                    "La spécialité est obligatoire."
            );
        }

        if (enseignant.getSpecialite().getIdSpecialite() <= 0) {
            throw new IllegalArgumentException(
                    "La spécialité doit avoir un identifiant valide."
            );
        }

        if (enseignant.getUtilisateur() == null) {
            throw new IllegalArgumentException(
                    "Un utilisateur doit être associé à l'enseignant."
            );
        }

        if (enseignant.getContact() != null
                && enseignant.getContact().length() > 15) {
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

    // TEST
}
