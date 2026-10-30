
package dao;

import modele.InscriptionAnnee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InscriptionAnneeRepository {

    private static final Logger LOGGER =
            Logger.getLogger(InscriptionAnneeRepository.class.getName());

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
    public void save(InscriptionAnnee inscription) {
        checkInscription(inscription);

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(inscription);

            transaction.commit();

            LOGGER.info(
                    "Inscription annuelle ajoutée : ID "
                            + inscription.getIdInscription()
            );

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de l'ajout de l'inscription annuelle.",
                    ex
            );

            throw ex;
        }
    }

    // READ BY ID
    public InscriptionAnnee getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT ia FROM InscriptionAnnee ia " +
                            "JOIN FETCH ia.etudiant " +
                            "WHERE ia.idInscription = :id",
                            InscriptionAnnee.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération de l'inscription ID "
                            + id,
                    ex
            );

            throw ex;
        }
    }

    // READ BY STUDENT AND SCHOOL YEAR
    public InscriptionAnnee getByEtudiantAndAnnee(
            int idEtudiant,
            String anneeScolaire
    ) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT ia FROM InscriptionAnnee ia " +
                            "JOIN FETCH ia.etudiant " +
                            "WHERE ia.etudiant.idEtudiant = :idEtudiant " +
                            "AND ia.anneeScolaire = :anneeScolaire",
                            InscriptionAnnee.class
                    )
                    .setParameter("idEtudiant", idEtudiant)
                    .setParameter("anneeScolaire", anneeScolaire)
                    .uniqueResult();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la recherche de l'inscription pour " +
                    "l'étudiant ID " + idEtudiant +
                    " et l'année " + anneeScolaire,
                    ex
            );

            throw ex;
        }
    }

    // READ ALL
    public List<InscriptionAnnee> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT ia FROM InscriptionAnnee ia " +
                            "JOIN FETCH ia.etudiant " +
                            "ORDER BY ia.dateInscription DESC",
                            InscriptionAnnee.class
                    )
                    .getResultList();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération des inscriptions annuelles.",
                    ex
            );

            throw ex;
        }
    }

    // UPDATE
    public void update(InscriptionAnnee inscription) {
        checkInscription(inscription);

        if (inscription.getIdInscription() <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant de l'inscription doit être positif."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            InscriptionAnnee existante = session.get(
                    InscriptionAnnee.class,
                    inscription.getIdInscription()
            );

            if (existante == null) {
                throw new IllegalArgumentException(
                        "Inscription introuvable : "
                                + inscription.getIdInscription()
                );
            }

            existante.setEtudiant(inscription.getEtudiant());
            existante.setAnneeScolaire(inscription.getAnneeScolaire());

            // La date d'inscription initiale reste inchangée.
            transaction.commit();

            LOGGER.info(
                    "Inscription annuelle mise à jour : ID "
                            + inscription.getIdInscription()
            );

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la mise à jour de l'inscription annuelle.",
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

            InscriptionAnnee inscription = session.get(
                    InscriptionAnnee.class,
                    id
            );

            if (inscription == null) {
                throw new IllegalArgumentException(
                        "Inscription introuvable : " + id
                );
            }

            session.remove(inscription);

            transaction.commit();

            LOGGER.info("Inscription annuelle supprimée : ID " + id);

        } catch (RuntimeException ex) {
            rollbackTransaction(transaction);

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la suppression de l'inscription ID " + id,
                    ex
            );

            throw ex;
        }
    }

    // VALIDATION
    private void checkInscription(InscriptionAnnee inscription) {
        if (inscription == null) {
            throw new IllegalArgumentException(
                    "L'inscription ne peut pas être null."
            );
        }

        if (inscription.getEtudiant() == null) {
            throw new IllegalArgumentException(
                    "Un étudiant doit être associé à l'inscription."
            );
        }

        if (inscription.getEtudiant().getIdEtudiant() <= 0) {
            throw new IllegalArgumentException(
                    "L'étudiant doit posséder un identifiant valide."
            );
        }

        String annee = inscription.getAnneeScolaire();

        if (annee == null || !annee.matches("\\d{4}-\\d{4}")) {
            throw new IllegalArgumentException(
                    "L'année scolaire doit respecter le format AAAA-AAAA."
            );
        }

        int debut = Integer.parseInt(annee.substring(0, 4));
        int fin = Integer.parseInt(annee.substring(5, 9));

        if (fin != debut + 1) {
            throw new IllegalArgumentException(
                    "L'année scolaire doit contenir deux années consécutives."
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
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            LOGGER.info("SessionFactory Hibernate fermée.");
        }
    }
}
