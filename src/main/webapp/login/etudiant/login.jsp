<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion Étudiant</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>
<c:set var="activeLoginRole" value="etudiant" scope="request" />
<jsp:include page="/WEB-INF/jsp/login/role-navigation.jsp" />

<!-- Main Content -->
<main>
    <header class="page-header">
        <h1>Connexion Étudiant</h1>
    </header>

    <!-- Centered Login Form -->
    <div class="login-form-container">
        <c:if test="${not empty requestScope.error}">
            <div class="error-message" role="alert">
                <c:out value="${requestScope.error}" />
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/loginController" method="post" class="login-form">
            <div class="form-container">
                <div class="form-column">
                    <label for="email_etudiant">Email :</label>
                          <input type="email" id="email_etudiant" name="email_etudiant"
                              autocomplete="username" maxlength="100"
                              value="<c:out value='${param.email_etudiant}' />" required />

                    <label for="mot_de_passe">Mot de passe :</label>
                          <input type="password" id="mot_de_passe" name="mot_de_passe"
                              autocomplete="current-password" required />
                </div>
            </div>
            <div class="submit-container">
                <input type="submit" value="Se connecter" />
            </div>
        </form>
    </div>
    <br>
</main>

<!-- Footer -->
<footer>
    <p>© 2024 - Gestion Scolaire</p>
</footer>
</body>
</html>
