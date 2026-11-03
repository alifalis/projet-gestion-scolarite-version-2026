<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Attribution de Cours</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>
<c:set var="activeAdminPage" value="cours" scope="request" />
<jsp:include page="/WEB-INF/jsp/admin/sidebar.jsp" />

<main>
    <header>
        <h1>Attribution de Cours aux Professeurs</h1>
    </header>

    <div class="container">
        <!-- Partie gauche : Liste des cours -->
        <div>
            <h2>Liste des Cours</h2>
            <table>
                <thead>
                <tr>
                    <th>ID Cours</th>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Professeur</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty cours}">
                        <c:forEach items="${cours}" var="coursItem">
                            <tr>
                                <td><c:out value="${coursItem.idCours}" /></td>
                                <td><c:out value="${coursItem.nomCours}" /></td>
                                <td><c:out value="${coursItem.description}" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty coursItem.enseignant}">
                                            <c:out value="${coursItem.enseignant.utilisateur.nom}" />
                                            <c:out value="${coursItem.enseignant.utilisateur.prenom}" />
                                        </c:when>
                                        <c:otherwise>Non attribué</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="4">Aucun cours trouvé.</td></tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
    <div class="container">
        <!-- Partie droite : Attribution de cours -->
        <div>
            <h2>Attribuer un Cours</h2>
            <!-- Recherche d'un professeur -->
            <form action="${pageContext.request.contextPath}/rechercherProfesseur" method="get">
                <label for="search">Rechercher un professeur :</label>
                <br>
                <input style="width: 70%" type="search" name="search" id="search"
                       placeholder="Nom, prénom ou e-mail"
                       value="<c:out value='${param.search}' />" required>
                <button type="submit">Rechercher</button>
            </form>

            <!-- Affichage des professeurs -->
            <c:if test="${not empty professeurs}">
            <form action="${pageContext.request.contextPath}/attribuerCours" method="post">
                <label for="selectProf">Sélectionner un professeur :</label>
                <select name="id_professeur" id="selectProf" required>
                    <c:forEach items="${professeurs}" var="professeur">
                        <option value="<c:out value='${professeur.idEnseignant}' />">
                            <c:out value="${professeur.utilisateur.nom}" />
                            <c:out value="${professeur.utilisateur.prenom}" />
                        </option>
                    </c:forEach>
                </select>
                <br>
                <!-- Formulaire pour ajouter un cours -->
                <label for="nom_cours">Nom du cours :</label>
                <input type="text" name="nom_cours" id="nom_cours" required><br>

                <label for="description">Description :</label>
                <br>
                <textarea name="description" id="description" rows="5" required></textarea><br>

                <button type="submit">Enregistrer</button>
            </form>
            </c:if>
            <c:if test="${not empty requestScope.message}">
                <output class="message">
                    <c:out value="${requestScope.message}" />
                </output>
            </c:if>
        </div>
    </div>
</main>
<footer>
    <p>© 2024 - Gestion des Cours</p>
</footer>
</body>
</html>
