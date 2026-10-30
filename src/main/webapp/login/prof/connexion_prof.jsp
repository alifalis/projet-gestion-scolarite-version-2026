<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion Professeur</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>
<c:set var="activeLoginRole" value="prof" scope="request" />
<jsp:include page="/WEB-INF/jsp/login/role-navigation.jsp" />

<!-- Main Content -->
<main>
    <header class="page-header">
        <h1>Connexion Professeur</h1>
    </header>

    <!-- Login Form -->
    <c:if test="${not empty requestScope.error}">
        <div class="error-message" role="alert">
            <c:out value="${requestScope.error}" />
        </div>
    </c:if>
    <form action="${pageContext.request.contextPath}/connexionProf"
          method="post" class="login-form">
        <div class="form-container">
            <div class="form-column">
                <label for="email_enseignant">Email :</label>
                  <input type="email" id="email_enseignant" name="email_enseignant"
                      autocomplete="username" maxlength="100"
                      value="<c:out value='${param.email_enseignant}' />" required /><br />

                <label for="mot_de_passe">Mot de passe :</label>
                  <input type="password" name="mot_de_passe" id="mot_de_passe"
                      autocomplete="current-password" required /><br />
            </div>
        </div>
        <div class="submit-container">
            <input type="submit" value="Se connecter" />
        </div>
    </form>
</main>

<!-- Footer -->
<footer>
    <p>© 2024 - Gestion Scolaire</p>
</footer>
</body>
</html>
