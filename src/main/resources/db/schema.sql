CREATE DATABASE IF NOT EXISTS school_data CHARACTER SET utf8mb4;
USE school_data;
-- 1. Roles
CREATE TABLE IF NOT EXISTS roles (
    id_role INT AUTO_INCREMENT PRIMARY KEY,
    nom_role VARCHAR(50) NOT NULL
);
-- 2. Utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    adresse VARCHAR(200) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    id_role INT NOT NULL,
    FOREIGN KEY (id_role) REFERENCES roles(id_role) ON DELETE CASCADE
);
-- 3. Classes
CREATE TABLE IF NOT EXISTS classes (
    id_classe INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(15) NOT NULL UNIQUE
);
-- 4. Specialites
CREATE TABLE IF NOT EXISTS specialites (
    id_specialite INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);
-- 5. Etudiants
CREATE TABLE IF NOT EXISTS etudiants (
    id_etudiant INT AUTO_INCREMENT PRIMARY KEY,
    date_naissance DATE NOT NULL,
    contact VARCHAR(15),
    id_classe INT,
    filiere VARCHAR(50),
    id_utilisateur INT NOT NULL UNIQUE,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id_utilisateur) ON DELETE CASCADE,
    FOREIGN KEY (id_classe) REFERENCES classes(id_classe) ON DELETE
    SET NULL
);
-- 6. Enseignants
CREATE TABLE IF NOT EXISTS enseignants (
    id_enseignant INT AUTO_INCREMENT PRIMARY KEY,
    date_naissance DATE NOT NULL,
    contact VARCHAR(15),
    id_specialite INT,
    id_utilisateur INT NOT NULL UNIQUE,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id_utilisateur) ON DELETE CASCADE,
    FOREIGN KEY (id_specialite) REFERENCES specialites(id_specialite) ON DELETE
    SET NULL
);
-- 7. Cours
CREATE TABLE IF NOT EXISTS cours (
    id_cours INT AUTO_INCREMENT PRIMARY KEY,
    nom_cours VARCHAR(100) NOT NULL,
    description TEXT,
    id_enseignant INT,
    FOREIGN KEY (id_enseignant) REFERENCES enseignants(id_enseignant) ON DELETE
    SET NULL
);
-- 8. Inscriptions a l'annee
CREATE TABLE IF NOT EXISTS inscriptions_annee (
    id_inscription INT AUTO_INCREMENT PRIMARY KEY,
    id_etudiant INT NOT NULL,
    annee_scolaire VARCHAR(9) NOT NULL,
    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_etudiant) REFERENCES etudiants(id_etudiant) ON DELETE CASCADE,
    UNIQUE KEY unique_inscription_annee (id_etudiant, annee_scolaire)
);
-- 9. Inscriptions aux cours
CREATE TABLE IF NOT EXISTS inscriptions_cours (
    id_inscription INT AUTO_INCREMENT PRIMARY KEY,
    id_etudiant INT NOT NULL,
    id_cours INT NOT NULL,
    debut_cours DATETIME NOT NULL,
    fin_cours DATETIME NOT NULL,
    FOREIGN KEY (id_etudiant) REFERENCES etudiants(id_etudiant) ON DELETE CASCADE,
    FOREIGN KEY (id_cours) REFERENCES cours(id_cours) ON DELETE CASCADE,
    UNIQUE KEY unique_inscription_cours (id_etudiant, id_cours)
);
-- 10. Resultats
CREATE TABLE IF NOT EXISTS resultats (
    id_resultat INT AUTO_INCREMENT PRIMARY KEY,
    id_etudiant INT NOT NULL,
    id_cours INT NOT NULL,
    note DECIMAL(4, 2),
    FOREIGN KEY (id_etudiant) REFERENCES etudiants(id_etudiant) ON DELETE CASCADE,
    FOREIGN KEY (id_cours) REFERENCES cours(id_cours) ON DELETE CASCADE,
    UNIQUE KEY unique_etudiant_cours (id_etudiant, id_cours),
    CHECK (
        note IS NULL
        OR note BETWEEN 0 AND 20
    )
);
-- Donnees de reference
INSERT IGNORE INTO roles (id_role, nom_role)
VALUES (1, 'Administrateur'),
    (2, 'Enseignant'),
    (3, 'Etudiant');
INSERT IGNORE INTO classes (id_classe, nom)
VALUES (1, 'ING1');
INSERT IGNORE INTO specialites (id_specialite, nom)
VALUES (1, 'Mathematiques');
-- Utilisateurs de test
INSERT IGNORE INTO utilisateurs (
        id_utilisateur,
        nom,
        prenom,
        adresse,
        email,
        mot_de_passe,
        id_role
    )
VALUES (
        1,
        'admin',
        'User',
        '1 Rue de l''Admin, Paris',
        'admin@domain.com',
        'password123',
        1
    ),
    (
        2,
        'John',
        'Doe',
        '5 Rue de l''Enseignant, Paris',
        'john.doe@domain.com',
        'password123',
        2
    ),
    (
        3,
        'Jane',
        'Smith',
        '10 Rue de l''Etudiant, Paris',
        'jane.smith@domain.com',
        'password123',
        3
    ),
    (
        4,
        'Super',
        'Admin',
        '99 Avenue de l''Admin, Paris',
        'admin@jee.com',
        'admin_password',
        1
    );
-- Etudiant de test
INSERT IGNORE INTO etudiants (
        id_etudiant,
        date_naissance,
        contact,
        id_classe,
        filiere,
        id_utilisateur
    )
VALUES (1, '2000-05-15', '0601010203', 1, 'GI', 3);
-- Enseignant de test
INSERT IGNORE INTO enseignants (
        id_enseignant,
        date_naissance,
        contact,
        id_specialite,
        id_utilisateur
    )
VALUES (1, '1985-09-12', '0610101010', 1, 2);
-- Cours de test
INSERT IGNORE INTO cours (id_cours, nom_cours, description, id_enseignant)
VALUES (
        1,
        'Programmation Java',
        'Introduction a Java',
        1
    ),
    (2, 'Bases de donnees', 'Conception et SQL', 1);
-- Inscription a l'annee scolaire
INSERT IGNORE INTO inscriptions_annee (id_inscription, id_etudiant, annee_scolaire)
VALUES (1, 1, '2026-2027');
-- Inscriptions aux cours
INSERT IGNORE INTO inscriptions_cours (
        id_inscription,
        id_etudiant,
        id_cours,
        debut_cours,
        fin_cours
    )
VALUES (
        1,
        1,
        1,
        '2026-09-01 09:00:00',
        '2027-06-30 12:00:00'
    ),
    (
        2,
        1,
        2,
        '2026-09-01 14:00:00',
        '2027-06-30 17:00:00'
    );
-- Notes
INSERT INTO resultats (id_etudiant, id_cours, note)
VALUES (1, 1, 18.50),
    (1, 2, 16.00) ON DUPLICATE KEY
UPDATE note =
VALUES(note);