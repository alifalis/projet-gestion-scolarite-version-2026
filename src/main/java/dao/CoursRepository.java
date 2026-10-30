
package dao;

import modele.Cours;
import modele.Enseignant;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoursRepository {

    private static final Logger LOGGER =
            Logger.getLogger(CoursRepository.class.getName());

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Exception ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de l'initialisation de Hibernate.",
                    ex
            );
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Ajouter un cours.
     */
    public void save(Cours cours) {
        checkCours(cours);

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Associer un enseignant géré par cette session.
            if (cours.getEnseignant() != null) {
                Enseignant enseignant = session.get(
                        Enseignant.class,
                        cours.getEnseignant().getIdEnseignant()
                );

                if (enseignant == null) {
                    throw new IllegalArgumentException(
                            "L'enseignant associé au cours n'existe pas."
                    );
                }

                cours.setEnseignant(enseignant);
            }

            session.persist(cours);
            transaction.commit();

            LOGGER.info("Cours ajouté avec succès : " + cours.getNomCours());

        } catch (RuntimeException ex) {
            rollback(transaction);
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout du cours.", ex);
            throw ex;
        }
    }

    /**
     * Récupérer un cours par son identifiant.
     * L'enseignant est chargé avec le cours.
     */
    public Cours getById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant du cours doit être positif."
            );
        }

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT c FROM Cours c " +
                            "LEFT JOIN FETCH c.enseignant " +
                            "WHERE c.idCours = :id",
                            Cours.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération du cours " + id,
                    ex
            );
            throw ex;
        }
    }

    /**
     * Récupérer tous les cours.
     */
    public List<Cours> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT c FROM Cours c " +
                            "LEFT JOIN FETCH c.enseignant e " +
                            "LEFT JOIN FETCH e.utilisateur " +
                            "ORDER BY c.idCours",
                            Cours.class
                    )
                    .getResultList();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la récupération des cours.",
                    ex
            );
            throw ex;
        }
    }

    /**
     * Rechercher l'enseignant associé à un cours
     * en fonction du nom du cours et de l'identifiant de l'enseignant.
     */
    public Enseignant getEnseignantByNomCoursAndProfesseur(
            String nomCours,
            int idProfesseur) {

        if (nomCours == null || nomCours.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom du cours ne doit pas être vide."
            );
        }

        if (idProfesseur <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant du professeur doit être positif."
            );
        }

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT c.enseignant FROM Cours c " +
                            "WHERE c.nomCours = :nomCours " +
                            "AND c.enseignant.idEnseignant = :idProfesseur",
                            Enseignant.class
                    )
                    .setParameter("nomCours", nomCours.trim())
                    .setParameter("idProfesseur", idProfesseur)
                    .setMaxResults(1)
                    .uniqueResult();

        } catch (RuntimeException ex) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la recherche de l'enseignant.",
                    ex
            );
            throw ex;
        }
    }

    /**
     * Modifier un cours existant.
     */
    public void update(Cours cours) {
        checkCours(cours);

        if (cours.getIdCours() <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant du cours doit être positif."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Cours existant = session.get(Cours.class, cours.getIdCours());

            if (existant == null) {
                throw new IllegalArgumentException(
                        "Cours introuvable : " + cours.getIdCours()
                );
            }

            existant.setNomCours(cours.getNomCours().trim());
            existant.setDescription(cours.getDescription());

            if (cours.getEnseignant() == null) {
                existant.setEnseignant(null);
            } else {
                Enseignant enseignant = session.get(
                        Enseignant.class,
                        cours.getEnseignant().getIdEnseignant()
                );

                if (enseignant == null) {
                    throw new IllegalArgumentException(
                            "L'enseignant associé au cours n'existe pas."
                    );
                }

                existant.setEnseignant(enseignant);
            }

            transaction.commit();

            LOGGER.info(
                    "Cours modifié avec succès : " + existant.getNomCours()
            );

        } catch (RuntimeException ex) {
            rollback(transaction);
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la modification du cours.",
                    ex
            );
            throw ex;
        }
    }

    /**
     * Supprimer un cours par son identifiant.
     */
    public void deleteById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant du cours doit être positif."
            );
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Cours cours = session.get(Cours.class, id);

            if (cours == null) {
                throw new IllegalArgumentException(
                        "Cours introuvable : " + id
                );
            }

            String nomCours = cours.getNomCours();

            session.remove(cours);
            transaction.commit();

            LOGGER.info("Cours supprimé avec succès : " + nomCours);

        } catch (RuntimeException ex) {
            rollback(transaction);
            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la suppression du cours " + id,
                    ex
            );
            throw ex;
        }
    }

    /**
     * Vérifier les données obligatoires du cours.
     */
    private void checkCours(Cours cours) {
        if (cours == null) {
            throw new IllegalArgumentException(
                    "Le cours ne doit pas être null."
            );
        }

        if (cours.getNomCours() == null
                || cours.getNomCours().isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom du cours est obligatoire."
            );
        }

        if (cours.getNomCours().trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Le nom du cours ne doit pas dépasser 100 caractères."
            );
        }
    }

    /**
     * Annuler la transaction en cas d'erreur.
     */
    private void rollback(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (RuntimeException ex) {
                LOGGER.log(
                        Level.SEVERE,
                        "Erreur lors de l'annulation de la transaction.",
                        ex
                );
            }
        }
    }

    /**
     * Fermer la SessionFactory à la fin de l'application.
     */
    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            LOGGER.info("SessionFactory fermée.");
        }
    }
}
