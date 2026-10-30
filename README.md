
# Gestion de Scolarité (myCyScolarité - J2EE Project)

**myCyScolarité** est une plateforme de gestion académique développée en Java JEE. Elle vise à fournir aux étudiants, professeurs, et administrateurs une solution complète pour gérer efficacement les inscriptions, les cours, les notes, et autres aspects liés à la scolarité. Ce projet exploite des technologies modernes et des outils avancés pour garantir une expérience utilisateur fluide et des fonctionnalités robustes.

## Fonctionnalités

### **Visiteur**
- Accès à la page d'accueil avec présentation de la plateforme.
- Possibilité de se connecter en tant qu'étudiant, professeur, ou administrateur.

### **Étudiant**
- Consultation des **notes**, **résultats**, et **relevés de notes**.
- Visualisation de l’**agenda des cours**.
- Téléchargement de relevés de notes au format PDF.

### **Professeur**
- Gestion des **notes des étudiants** : ajout, modification, et mise à jour.
- Notifications par email aux étudiants pour informer des changements de notes.

### **Administrateur**
- **Gestion des utilisateurs** : inscription, réinscription, et modification des informations des étudiants et professeurs.
- **Gestion des cours** : création, modification, suppression des cours.
- Attribution des cours aux **professeurs** et aux **étudiants**.
- Export des données sous format CSV (listes d’étudiants et de professeurs).

## Architecture et Technologies

### **Architecture**
Le projet suit l'architecture **MVC (Model-View-Controller)** pour une séparation claire des responsabilités :
- **Model** : Entités JPA (ex. Étudiant, Professeur, Cours).
- **View** : Pages JSP pour l’interface utilisateur.
- **Controller** : Servlets pour la gestion des requêtes HTTP et de la logique métier.

### **Technologies utilisées**
- **Langages** : Java 21 ou supérieur (J2EE), HTML5, CSS3, JavaScript.
- **Frameworks** : Hibernate (ORM), Spring (Injection de dépendances et gestion).
- **Serveur d'applications** : Apache Tomcat.
- **Base de données** : MySQL.
- **IDE** : IntelliJ IDEA.
- **Contrôle de version** : Git.

## Installation

1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/abdemeh/gestionScolarite_JEE.git
   ```

2. **Installer Java 21 ou supérieur** et Maven.

3. **Configurer la base de données** :
   - Importer le fichier SQL disponible dans `sql/schema.sql` pour créer la base et les tables.

4. **Configurer le projet** :
   - Modifier le fichier `hibernate.cfg.xml` pour inclure les informations d'identification de votre base de données.

5. **Déployer l’application** :
   - Copier le projet dans le répertoire de déploiement d’Apache Tomcat.
   - Lancer le serveur Tomcat.

6. **Accéder à l’application** :
   - Ouvrez le navigateur et accédez à `http://localhost:8080/projetJEE`.

## Équipe de Développement

- **Ali Sekaner Alif**
- **El-Mahdaoui Abdellatif**
- **Kone Mohamed Lamine**
- **N’Gokoly Gloddy**
- **Vautrin Baptiste**

## Perspectives d’Amélioration

1. Ajouter des notifications en temps réel pour les utilisateurs.
2. Intégrer un tableau de bord analytique pour les administrateurs.
3. Migrer le backend vers Spring Boot pour améliorer la scalabilité.
4. Implémenter un système de messagerie interne pour faciliter la communication entre les rôles.

## Conclusion

La réalisation de ce projet a permis de consolider nos compétences techniques en **Programmation JEE** (Servlets, JSP, Hibernate, Spring), tout en renforçant notre capacité à collaborer et gérer efficacement un projet de grande envergure. Ce projet illustre l’importance de la planification et de l’utilisation d’outils modernes pour répondre aux besoins des utilisateurs.
