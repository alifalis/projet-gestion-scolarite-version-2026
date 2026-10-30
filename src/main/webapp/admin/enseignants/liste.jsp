<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Professeurs</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>
<c:set var="activeAdminPage" value="enseignants" scope="request" />
<jsp:include page="/WEB-INF/jsp/admin/sidebar.jsp" />

<!-- Main Content -->
<main>
    <header>
        <h1>Liste des Professeurs</h1>
    </header>

    <!-- Professors Table -->
    <div class="container">
        <!-- Search Bar -->
        <div class="search-bar">
            <form action="<%= request.getContextPath() %>/listeProfesseurs" method="get">
                <label for="searchQuery">Recherche</label>
                <input id="searchQuery" style="width: 88%;" type="search"
                       name="searchQuery"
                       placeholder="Nom, prénom, adresse, email ou spécialité"
                       value="<c:out value='${param.searchQuery}' />">
                <button type="submit">Rechercher</button>
            </form>
        </div>
        <table>
            <thead>
            <tr>
                <th>ID Professeur</th>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Adresse</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Spécialité</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty professeurs}">
                    <c:forEach items="${professeurs}" var="professeur">
                        <tr>
                            <td><c:out value="${professeur.idEnseignant}" /></td>
                            <td><c:out value="${professeur.utilisateur.nom}" /></td>
                            <td><c:out value="${professeur.utilisateur.prenom}" /></td>
                            <td><c:out value="${professeur.utilisateur.adresse}" /></td>
                            <td><c:out value="${professeur.utilisateur.email}" /></td>
                            <td><c:out value="${professeur.contact}" /></td>
                            <td><c:out value="${empty professeur.specialite ? 'N/A' : professeur.specialite.nom}" /></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/supprimerProfesseur"
                                      method="post"
                                      onsubmit="return confirm('Supprimer ce professeur ?')">
                                    <input type="hidden" name="id"
                                           value="<c:out value='${professeur.idEnseignant}' />">
                                    <button type="submit" aria-label="Supprimer le professeur">
                                        Supprimer
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="8">
                            <c:choose>
                                <c:when test="${not empty param.searchQuery}">Aucun professeur ne correspond à votre recherche.</c:when>
                                <c:otherwise>Aucun professeur trouvé.</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <form action="<%= request.getContextPath() %>/telechargerProfesseursCSV" method="get">
            <button type="submit">Télécharger la liste des professeurs (CSV)</button>
        </form>
    </div>
    <footer>
        <p>© 2024 - Gestion Scolaire</p>
    </footer>
</main>

</body>
</html>
