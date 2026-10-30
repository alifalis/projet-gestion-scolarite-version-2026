
package dao;

import modele.InscriptionCours;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InscriptionCoursRepository {

    private static final Logger LOGGER =
            Logger.getLogger(InscriptionCoursRepository.class.getName());

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

    // Ajouter une inscription
    public void save(InscriptionCours inscription) {
        checkInscription(inscription);

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(inscription);

            transaction.commit();

            LOGGER.info("Inscription ajoutée : ID "
                    + inscription.getIdInscription());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de l'ajout de l'inscription.", e);

            throw e;
        }
    }

    // Rechercher une inscription par son identifiant
    public InscriptionCours getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT i FROM InscriptionCours i " +
                            "JOIN FETCH i.etudiant " +
                            "JOIN FETCH i.cours " +
                            "WHERE i.idInscription = :id",
                            InscriptionCours.class)
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche de l'inscription ID " + id, e);

            throw e;
        }
    }

    // Récupérer toutes les inscriptions
    public List<InscriptionCours> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT i FROM InscriptionCours i " +
                            "JOIN FETCH i.etudiant e " +
                            "LEFT JOIN FETCH e.classe " +
                            "JOIN FETCH e.utilisateur " +
                            "JOIN FETCH i.cours c " +
                            "LEFT JOIN FETCH c.enseignant enseignant " +
                            "LEFT JOIN FETCH enseignant.utilisateur " +
                            "ORDER BY i.idInscription",
                            InscriptionCours.class)
                    .getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération des inscriptions.", e);

            throw e;
        }
    }

    // Récupérer les inscriptions d'un étudiant
    public List<InscriptionCours> getByEtudiant(int idEtudiant) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT i FROM InscriptionCours i " +
                            "JOIN FETCH i.etudiant e " +
                            "LEFT JOIN FETCH e.classe " +
                            "JOIN FETCH e.utilisateur " +
                            "JOIN FETCH i.cours c " +
                            "LEFT JOIN FETCH c.enseignant enseignant " +
                            "LEFT JOIN FETCH enseignant.utilisateur " +
                            "WHERE i.etudiant.idEtudiant = :idEtudiant " +
                            "ORDER BY i.debutCours",
                            InscriptionCours.class)
                    .setParameter("idEtudiant", idEtudiant)
                    .getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération des inscriptions "
                            + "de l'étudiant ID " + idEtudiant, e);

            throw e;
        }
    }
        // Récupérer les identifiants des étudiants inscrits à un cours
    public List<Integer> getIdEtudiantsByCours(int idCours) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT i.etudiant.idEtudiant " +
                            "FROM InscriptionCours i " +
                            "WHERE i.cours.idCours = :idCours " +
                            "ORDER BY i.etudiant.idEtudiant",
                            Integer.class)
                    .setParameter("idCours", idCours)
                    .getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération des identifiants "
                            + "des étudiants du cours ID " + idCours,
                    e
            );
            throw e;
        }
    }

   

    // Modifier une inscription
    public void update(InscriptionCours inscription) {
        checkInscription(inscription);

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            InscriptionCours inscriptionExistante = session.get(
                    InscriptionCours.class,
                    inscription.getIdInscription());

            if (inscriptionExistante == null) {
                throw new IllegalArgumentException(
                        "Aucune inscription trouvée avec l'identifiant "
                                + inscription.getIdInscription());
            }

            inscriptionExistante.setEtudiant(inscription.getEtudiant());
            inscriptionExistante.setCours(inscription.getCours());
            inscriptionExistante.setDebutCours(inscription.getDebutCours());
            inscriptionExistante.setFinCours(inscription.getFinCours());

            transaction.commit();

            LOGGER.info("Inscription mise à jour : ID "
                    + inscription.getIdInscription());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la mise à jour de l'inscription.", e);

            throw e;
        }
    }

    // Supprimer une inscription par son identifiant
    public void deleteById(int id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            InscriptionCours inscription = session.get(
                    InscriptionCours.class, id);

            if (inscription == null) {
                transaction.rollback();

                LOGGER.warning(
                        "Aucune inscription trouvée avec l'identifiant " + id);

                return;
            }

            session.remove(inscription);

            transaction.commit();

            LOGGER.info("Inscription supprimée : ID " + id);

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la suppression de l'inscription ID " + id, e);

            throw e;
        }
    }

    // Vérifier les données obligatoires et la cohérence des dates
    private void checkInscription(InscriptionCours inscription) {
        if (inscription == null) {
            throw new IllegalArgumentException(
                    "L'inscription ne peut pas être null.");
        }

        if (inscription.getEtudiant() == null
                || inscription.getCours() == null) {
            throw new IllegalArgumentException(
                    "L'inscription doit avoir un étudiant et un cours.");
        }

        if (inscription.getDebutCours() == null
                || inscription.getFinCours() == null) {
            throw new IllegalArgumentException(
                    "Les dates de début et de fin sont obligatoires.");
        }

        if (!inscription.getFinCours().isAfter(inscription.getDebutCours())) {
            throw new IllegalArgumentException(
                    "La date de fin doit être postérieure à la date de début.");
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
