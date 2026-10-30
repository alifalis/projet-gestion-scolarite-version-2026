<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription Enseignant</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>
<nav id="sidebar">
    <ul>
        <li>
            <span class="logo">
                <img src="<%= request.getContextPath() %>/images/logo.png"
                     height="36" alt="myCY Scolarité">
            </span>
            <button onclick="toggleSidebar()" id="toggle-btn" aria-label="Réduire le menu">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24" fill="#e8eaed">
                    <path d="m313-480 155 156q11 11 11.5 27.5T468-268q-11 11-28 11t-28-11L228-452q-6-6-8.5-13t-2.5-15q0-8 2.5-15t8.5-13l184-184q11-11 27.5-11.5T468-692q11 11 11 28t-11 28L313-480Z"/>
                </svg>
            </button>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/index.jsp">
                <span>Accueil</span>
            </a>
        </li>
        <li>
            <button onclick="toggleSubMenu(this)" class="dropdown-btn rotate">
                <span>Inscription</span>
                <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24" fill="#e8eaed">
                    <path d="M480-361q-8 0-15-2.5t-13-8.5L268-556q-11-11-11-28t11-28q11-11 28-11t28 11l156 156 156-156q11-11 28-11t28 11q11 11 11 28t-11 28L508-372q-6 6-13 8.5t-15 2.5Z"/>
                </svg>
            </button>
            <ul class="sub-menu show">
                <div>
                    <li>
                        <a href="${pageContext.request.contextPath}/creer_eleve">
                            <span>Inscription Élève</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/mettreAJourEtudiant">
                            <span>Réinscrire Élève</span>
                        </a>
                    </li>
                    <li class="active">
                        <a href="${pageContext.request.contextPath}/creer_enseignant"
                           aria-current="page">
                            <span>Inscription Enseignant</span>
                        </a>
                    </li>
                </div>
            </ul>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/connexionProf">
                <span>Connexion Prof</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/loginController">
                <span>Connexion Élève</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/connexionAdmin">
                <span>Connexion Admin</span>
            </a>
        </li>
    </ul>
</nav>

<main>
    <header class="page-header">
        <h1>Bienvenue sur le formulaire d'inscription d'enseignant</h1>
    </header>

    <c:if test="${param.success eq 'true'}">
        <output class="success-message">
            L'inscription de l'enseignant a été enregistrée.
        </output>
    </c:if>
    <c:if test="${not empty requestScope.errorMessage}">
        <div class="error-message" role="alert">
            <c:out value="${requestScope.errorMessage}" />
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/creer_enseignant"
          method="post" class="registration-form">
        <div class="form-container">
            <div class="form-column">
                <label for="nom">Nom :</label>
                <input type="text" name="nom" id="nom" autocomplete="family-name"
                       maxlength="100" value="<c:out value='${param.nom}' />"
                       required>

                <label for="prenom">Prénom :</label>
                <input type="text" name="prenom" id="prenom"
                       autocomplete="given-name" maxlength="100"
                       value="<c:out value='${param.prenom}' />" required>

                <label for="telephone">Téléphone :</label>
                <input type="tel" name="telephone" id="telephone"
                       autocomplete="tel" maxlength="15"
                       value="<c:out value='${param.telephone}' />" required>

                <label for="email">Adresse e-mail :</label>
                <input type="email" name="email" id="email" autocomplete="email"
                       maxlength="100" value="<c:out value='${param.email}' />"
                       required>

                <label for="mot_de_passe">Mot de passe :</label>
                <input type="password" name="mot_de_passe" id="mot_de_passe"
                       autocomplete="new-password" minlength="8" maxlength="128"
                       required>

                <label for="date_naissance">Date de naissance :</label>
                <input type="date" name="date_naissance" id="date_naissance"
                       autocomplete="bday"
                       value="<c:out value='${param.date_naissance}' />"
                       required>
            </div>

            <div class="form-column">
                <label for="numero_adresse">Numéro d'adresse :</label>
                <input type="text" name="numero_adresse" id="numero_adresse"
                       autocomplete="address-line2" maxlength="20"
                       value="<c:out value='${param.numero_adresse}' />">

                <label for="adresse">Adresse :</label>
                <input type="text" name="adresse" id="adresse"
                       autocomplete="address-line1" maxlength="200"
                       value="<c:out value='${param.adresse}' />" required>

                <label for="commune">Commune :</label>
                <input type="text" name="commune" id="commune"
                       autocomplete="address-level2" maxlength="100"
                       value="<c:out value='${param.commune}' />" required>

                <label for="code_postal">Code postal :</label>
                <input type="text" name="code_postal" id="code_postal"
                       autocomplete="postal-code" inputmode="numeric"
                       pattern="[0-9]{5}" maxlength="5"
                       title="Veuillez saisir un code postal à 5 chiffres."
                       value="<c:out value='${param.code_postal}' />" required>

                <label for="specialite">Spécialité :</label>
                <select name="specialite" id="specialite" required>
                    <option value="">-- Sélectionnez une spécialité --</option>
                    <c:forEach items="${specialites}" var="specialiteOption">
                        <option value="<c:out value='${specialiteOption.nom}' />"
                            ${param.specialite eq specialiteOption.nom ? 'selected' : ''}>
                            <c:out value="${specialiteOption.nom}" />
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="submit-container">
            <input type="submit" value="Soumettre">
        </div>
    </form>
</main>
</body>
</html>
