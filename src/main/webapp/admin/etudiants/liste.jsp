<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Étudiants</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>
<c:set var="activeAdminPage" value="etudiants" scope="request" />
<jsp:include page="/WEB-INF/jsp/admin/sidebar.jsp" />

<!-- Main Content -->
<main>
    <header>
        <h1>Liste des Étudiants</h1>
    </header>

    <!-- Students Table -->
    <div class="container">
        <!-- Search Bar -->
        <div class="search-bar">
            <form action="<%= request.getContextPath() %>/listeEtudiants" method="get">
                <label for="searchQuery">Recherche</label>
                <input id="searchQuery" style="width: 88%;" type="search"
                       name="searchQuery"
                       placeholder="Nom, prénom, adresse, email, classe ou filière"
                       value="<c:out value='${param.searchQuery}' />">
                <button type="submit">Rechercher</button>
            </form>
        </div>
        <table>
            <thead>
            <tr>
                <th>ID Étudiant</th>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Adresse</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Classe</th>
                <th>Filière</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty etudiantList}">
                    <c:forEach items="${etudiantList}" var="etudiant">
                        <tr>
                            <td><c:out value="${etudiant.idEtudiant}" /></td>
                            <td><c:out value="${etudiant.utilisateur.nom}" /></td>
                            <td><c:out value="${etudiant.utilisateur.prenom}" /></td>
                            <td><c:out value="${etudiant.utilisateur.adresse}" /></td>
                            <td><c:out value="${etudiant.utilisateur.email}" /></td>
                            <td><c:out value="${etudiant.contact}" /></td>
                            <td><c:out value="${empty etudiant.classe ? 'N/A' : etudiant.classe.nom}" /></td>
                            <td><c:out value="${etudiant.filiere}" /></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/supprimerEtudiant"
                                      method="post"
                                      onsubmit="return confirm('Supprimer cet étudiant ?')">
                                    <input type="hidden" name="id"
                                           value="<c:out value='${etudiant.idEtudiant}' />">
                                    <button type="submit" aria-label="Supprimer l'étudiant">
                                        Supprimer
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="9">
                            <c:choose>
                                <c:when test="${not empty param.searchQuery}">Aucun étudiant ne correspond à votre recherche.</c:when>
                                <c:otherwise>Aucun étudiant trouvé.</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <form action="<%= request.getContextPath() %>/telechargerEtudiantsCSV" method="get">
            <button type="submit">Télécharger la liste des étudiants (CSV)</button>
        </form>
    </div>
    <footer>
        <p>© 2024 - Gestion Scolaire</p>
    </footer>
</main>

</body>
</html>
