
package dao;

import modele.Resultat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultatRepository {

    private static final Logger LOGGER =
            Logger.getLogger(ResultatRepository.class.getName());

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

    // Ajouter un résultat
    public void save(Resultat resultat) {
        if (resultat == null) {
            throw new IllegalArgumentException(
                    "Le résultat à enregistrer ne peut pas être null.");
        }

        checkNote(resultat.getNote());

        if (resultat.getEtudiant() == null || resultat.getCours() == null) {
            throw new IllegalArgumentException(
                    "Un résultat doit être associé à un étudiant et à un cours.");
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(resultat);

            transaction.commit();

            LOGGER.info("Résultat ajouté : ID " + resultat.getIdResultat());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de l'ajout du résultat.", e);

            throw e;
        }
    }

    // Rechercher un résultat par son identifiant
    public Resultat getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT r FROM Resultat r " +
                            "JOIN FETCH r.etudiant " +
                            "JOIN FETCH r.cours " +
                            "WHERE r.idResultat = :id",
                            Resultat.class)
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche du résultat ID " + id, e);

            throw e;
        }
    }

    // Récupérer tous les résultats avec les étudiants et les cours
    public List<Resultat> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT r FROM Resultat r " +
                            "JOIN FETCH r.etudiant " +
                            "JOIN FETCH r.cours " +
                            "ORDER BY r.idResultat",
                            Resultat.class)
                    .getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération des résultats.", e);

            throw e;
        }
    }

    // Récupérer les résultats d'un étudiant
    public List<Resultat> getByEtudiant(int idEtudiant) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT r FROM Resultat r " +
                            "JOIN FETCH r.etudiant " +
                            "JOIN FETCH r.cours " +
                            "WHERE r.etudiant.idEtudiant = :idEtudiant " +
                            "ORDER BY r.idResultat",
                            Resultat.class)
                    .setParameter("idEtudiant", idEtudiant)
                    .getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération des résultats de l'étudiant "
                            + idEtudiant, e);

            throw e;
        }
    }

    // Récupérer les résultats des cours d'un enseignant
    public List<Resultat> getByEnseignant(int idEnseignant) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT r FROM Resultat r " +
                            "JOIN FETCH r.etudiant et " +
                            "JOIN FETCH et.utilisateur " +
                            "JOIN FETCH r.cours c " +
                            "JOIN c.enseignant e " +
                            "WHERE e.idEnseignant = :idEnseignant " +
                            "ORDER BY r.idResultat",
                            Resultat.class)
                    .setParameter("idEnseignant", idEnseignant)
                    .getResultList();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération des résultats de l'enseignant "
                            + idEnseignant, e);

            throw e;
        }
    }

    // Rechercher le résultat d'un étudiant pour un cours
    public Resultat getByCoursAndEtudiant(int idCours, int idEtudiant) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT r FROM Resultat r " +
                            "JOIN FETCH r.etudiant " +
                            "JOIN FETCH r.cours c " +
                            "WHERE c.idCours = :idCours " +
                            "AND r.etudiant.idEtudiant = :idEtudiant",
                            Resultat.class)
                    .setParameter("idCours", idCours)
                    .setParameter("idEtudiant", idEtudiant)
                    .uniqueResult();

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche du résultat du cours "
                            + idCours + " pour l'étudiant " + idEtudiant, e);

            throw e;
        }
    }

    // Modifier la note d'un résultat
    public void update(Resultat resultat) {
        if (resultat == null) {
            throw new IllegalArgumentException(
                    "Le résultat à modifier ne peut pas être null.");
        }

        checkNote(resultat.getNote());

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Resultat resultatExistant = session.get(
                    Resultat.class, resultat.getIdResultat());

            if (resultatExistant == null) {
                throw new IllegalArgumentException(
                        "Aucun résultat trouvé avec l'identifiant "
                                + resultat.getIdResultat());
            }

            resultatExistant.setNote(resultat.getNote());

            transaction.commit();

            LOGGER.info("Résultat mis à jour : ID "
                    + resultat.getIdResultat());

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la mise à jour du résultat.", e);

            throw e;
        }
    }

    // Mettre à jour plusieurs notes dans une seule transaction
    public void updateNotes(
            List<Integer> idsResultats,
            List<BigDecimal> nouvellesNotes) {

        if (idsResultats == null || nouvellesNotes == null
                || idsResultats.size() != nouvellesNotes.size()) {
            throw new IllegalArgumentException(
                    "Les listes d'identifiants et de notes doivent être "
                            + "non nulles et de même taille.");
        }

        for (BigDecimal note : nouvellesNotes) {
            checkNote(note);
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            for (int i = 0; i < idsResultats.size(); i++) {
                int id = idsResultats.get(i);

                int nombreModifie = session.createMutationQuery(
                                "UPDATE Resultat r " +
                                "SET r.note = :note " +
                                "WHERE r.idResultat = :id")
                        .setParameter("note", nouvellesNotes.get(i))
                        .setParameter("id", id)
                        .executeUpdate();

                if (nombreModifie == 0) {
                    throw new IllegalArgumentException(
                            "Aucun résultat trouvé avec l'identifiant " + id);
                }
            }

            transaction.commit();

            LOGGER.info("Notes mises à jour avec succès.");

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la mise à jour des notes.", e);

            throw e;
        }
    }

    // Supprimer un résultat par son identifiant
    public void deleteById(int id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Resultat resultat = session.get(Resultat.class, id);

            if (resultat == null) {
                transaction.rollback();

                LOGGER.warning(
                        "Aucun résultat trouvé avec l'identifiant " + id);

                return;
            }

            session.remove(resultat);

            transaction.commit();

            LOGGER.info("Résultat supprimé : ID " + id);

        } catch (RuntimeException e) {
            rollbackTransaction(transaction);

            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la suppression du résultat ID " + id, e);

            throw e;
        }
    }

    // Vérifier qu'une note est comprise entre 0 et 20, ou null
    private void checkNote(BigDecimal note) {
        if (note != null
                && (note.compareTo(BigDecimal.ZERO) < 0
                || note.compareTo(BigDecimal.valueOf(20)) > 0)) {
            throw new IllegalArgumentException(
                    "La note doit être comprise entre 0 et 20.");
        }
    }

    // Annuler une transaction en cas d'erreur
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
