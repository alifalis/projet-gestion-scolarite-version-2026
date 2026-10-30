<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    // Session verification
    String userRole = (String) session.getAttribute("user");
    if (userRole == null || !"prof".equals(userRole)) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return; // Ensure the rest of the page is not processed
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des Notes</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>
<nav id="sidebar">
    <ul>
        <li>
            <span class="logo"><img src="<%= request.getContextPath() %>/images/logo.png" height="36px" alt="myCY Scolarité"></span>
            <button onclick="toggleSidebar()" id="toggle-btn">
                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#e8eaed">
                    <path d="m313-480 155 156q11 11 11.5 27.5T468-268q-11 11-28 11t-28-11L228-452q-6-6-8.5-13t-2.5-15q0-8 2.5-15t8.5-13l184-184q11-11 27.5-11.5T468-692q11 11 11 28t-11 28L313-480Zm264 0 155 156q11 11 11.5 27.5T732-268q-11 11-28 11t-28-11L492-452q-6-6-8.5-13t-2.5-15q0-8 2.5-15t8.5-13l184-184q11-11 27.5-11.5T732-692q11 11 11 28t-11 28L577-480Z"/>
                </svg>
            </button>
        </li>
        <li class="active"><a href="#">
            <svg xmlns="http://www.w3.org/2000/svg" height="24px" width="24px" viewBox="0 0 576 512"><path d="M575.8 255.5c0 18-15 32.1-32 32.1l-32 0 .7 160.2c0 2.7-.2 5.4-.5 8.1l0 16.2c0 22.1-17.9 40-40 40l-16 0c-1.1 0-2.2 0-3.3-.1c-1.4 .1-2.8 .1-4.2 .1L416 512l-24 0c-22.1 0-40-17.9-40-40l0-24 0-64c0-17.7-14.3-32-32-32l-64 0c-17.7 0-32 14.3-32 32l0 64 0 24c0 22.1-17.9 40-40 40l-24 0-31.9 0c-1.5 0-3-.1-4.5-.2c-1.2 .1-2.4 .2-3.6 .2l-16 0c-22.1 0-40-17.9-40-40l0-112c0-.9 0-1.9 .1-2.8l0-69.7-32 0c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24z"/></svg>
            <span>Accueil</span>
        </a></li>
        <li><a href="<%= request.getContextPath() %>/logout">
            <svg xmlns="http://www.w3.org/2000/svg" height="24px" width="24px" viewBox="0 0 512 512"><path d="M288 32c0-17.7-14.3-32-32-32s-32 14.3-32 32l0 224c0 17.7 14.3 32 32 32s32-14.3 32-32l0-224zM143.5 120.6c13.6-11.3 15.4-31.5 4.1-45.1s-31.5-15.4-45.1-4.1C49.7 115.4 16 181.8 16 256c0 132.5 107.5 240 240 240s240-107.5 240-240c0-74.2-33.8-140.6-86.6-184.6c-13.6-11.3-33.8-9.4-45.1 4.1s-9.4 33.8 4.1 45.1c38.9 32.3 63.5 81 63.5 135.4c0 97.2-78.8 176-176 176s-176-78.8-176-176c0-54.4 24.7-103.1 63.5-135.4z"/></svg>
            <span>Se déconnecter</span>
        </a></li>
    </ul>
</nav>

<main>
    <header>
        <h1>Gestion des Notes</h1>
    </header>
    <div class="container">
        <form action="modifierNotes" method="post">
            <table>
                <thead>
                <tr>
                    <th>ID Étudiant</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Nom du Cours</th>
                    <th>Note</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty resultats}">
                        <c:forEach items="${resultats}" var="resultat">
                            <tr>
                                <td><c:out value="${resultat.etudiant.idEtudiant}" /></td>
                                <td><c:out value="${resultat.etudiant.utilisateur.nom}" /></td>
                                <td><c:out value="${resultat.etudiant.utilisateur.prenom}" /></td>
                                <td><c:out value="${resultat.cours.nomCours}" /></td>
                                <td>
                                    <input type="hidden" name="id_resultat"
                                           value="<c:out value='${resultat.idResultat}' />" />
                                     <label for="note_${resultat.idResultat}">
                                         Note /20
                                     </label>
                                    <input type="number"
                                         id="note_${resultat.idResultat}"
                                           name="note_${resultat.idResultat}"
                                           value="<c:out value='${resultat.note}' />"
                                           min="0" max="20" step="0.01" />
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5">Aucun résultat trouvé pour ce professeur.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <button type="submit">Enregistrer les Modifications</button>
        </form>
        <form action="notifierEtudiants" method="post" style="margin-top: 5px;">
            <c:forEach items="${resultats}" var="resultat">
                <input type="hidden" name="emails"
                       value="<c:out value='${resultat.etudiant.utilisateur.email}' />" />
            </c:forEach>
            <button type="submit">Notifier les Étudiants par email</button>
        </form>
        <c:if test="${not empty requestScope.message}">
            <output class="success-message">
                <c:out value="${requestScope.message}" />
            </output>
        </c:if>
        <c:if test="${not empty requestScope.errorMessage}">
            <div class="error-message" role="alert">
                <c:out value="${requestScope.errorMessage}" />
            </div>
        </c:if>
    </div>
    <footer>
        <p>© 2024 - Gestion des Notes</p>
    </footer>
</main>
</body>
</html>
