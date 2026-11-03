<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu Administratif</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>
<c:set var="activeAdminPage" value="home" scope="request" />
<jsp:include page="/WEB-INF/jsp/admin/sidebar.jsp" />

<!-- Main Content -->
<main style="background-image: url('<%= request.getContextPath() %>/images/bg-school.jpg');">
    <div class="menu-container">
        <h2>Bienvenue à la page admin!</h2>
        <p>
            L'interface d'administration de CY Scolarité est conçue pour fournir aux responsables scolaires les outils nécessaires
            pour gérer efficacement tous les aspects de la vie académique. Du suivi des inscriptions à la gestion des enseignants et des cours,
            cette interface offre une vue complète et centralisée de toutes les activités administratives.
        </p>
        <p>
            Avec un tableau de bord intuitif et des fonctionnalités avancées, les administrateurs peuvent gérer rapidement les comptes utilisateurs,
            superviser les notes des étudiants, organiser les plannings des enseignants, et garantir le bon fonctionnement des opérations scolaires.
        </p>
        <p>
            Notre objectif est de simplifier votre quotidien en vous proposant des outils qui améliorent la productivité et la précision, tout en réduisant les tâches manuelles.
            CY Scolarité permet également une communication transparente avec les étudiants et les professeurs, assurant un environnement académique harmonieux et efficace.
        </p>
        <p>
            Explorez les différentes sections du menu pour accéder aux fonctionnalités clés : gestion des inscriptions, mise à jour des résultats, création de cours,
            et bien plus encore. Tout ce dont vous avez besoin pour gérer votre établissement se trouve ici, à portée de clic.
        </p>
    </div>

    <footer>
        <p>© 2024 - Gestion Scolaire</p>
    </footer>
</main>
</body>
</html>
