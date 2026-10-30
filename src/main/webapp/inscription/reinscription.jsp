
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Réinscription étudiant</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <script src="${pageContext.request.contextPath}/js/app.js" defer></script>
</head>

<body>

<!-- Barre latérale -->
<nav id="sidebar">
    <ul>
        <li>
            <span class="logo">
                <img
                    src="${pageContext.request.contextPath}/images/logo.png"
                    height="36"
                    alt="myCY Scolarité">
            </span>

            <button
                type="button"
                onclick="toggleSidebar()"
                id="toggle-btn"
                aria-label="Réduire ou développer le menu">

                <svg xmlns="http://www.w3.org/2000/svg"
                     height="24px"
                     viewBox="0 -960 960 960"
                     width="24px"
                     fill="#e8eaed">
                    <path d="m313-480 155 156q11 11 11.5 27.5T468-268q-11 11-28 11t-28-11L228-452q-6-6-8.5-13t-2.5-15q0-8 2.5-15t8.5-13l184-184q11-11 27.5-11.5T468-692q11 11 11 28t-11 28L313-480Zm264 0 155 156q11 11 11.5 27.5T732-268q-11 11-28 11t-28-11L492-452q-6-6-8.5-13t-2.5-15q0-8 2.5-15t8.5-13l184-184q11-11 27.5-11.5T732-692q11 11 11 28t-11 28L577-480Z"/>
                </svg>
            </button>
        </li>

        <!-- Accueil -->
        <li>
            <a href="${pageContext.request.contextPath}/index.jsp">
                <svg xmlns="http://www.w3.org/2000/svg"
                     height="24px"
                     width="24px"
                     viewBox="0 0 576 512">
                    <path d="M575.8 255.5c0 18-15 32.1-32 32.1l-32 0 .7 160.2c0 2.7-.2 5.4-.5 8.1l0 16.2c0 22.1-17.9 40-40 40l-16 0c-1.1 0-2.2 0-3.3-.1c-1.4 .1-2.8 .1-4.2 .1L416 512l-24 0c-22.1 0-40-17.9-40-40l0-24 0-64c0-17.7-14.3-32-32-32l-64 0c-17.7 0-32 14.3-32 32l0 64 0 24c0 22.1-17.9 40-40 40l-24 0-31.9 0c-1.5 0-3-.1-4.5-.2c-1.2 .1-2.4 .2-3.6 .2l-16 0c-22.1 0-40-17.9-40-40l0-112c0-.9 0-1.9 .1-2.8l0-69.7-32 0c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24z"/>
                </svg>
                <span>Accueil</span>
            </a>
        </li>

        <!-- Inscriptions -->
        <li>
            <button
                type="button"
                onclick="toggleSubMenu(this)"
                class="dropdown-btn rotate">

                <svg xmlns="http://www.w3.org/2000/svg"
                     height="24px"
                     width="24px"
                     viewBox="0 0 512 512">
                    <path d="M471.6 21.7c-21.9-21.9-57.3-21.9-79.2 0L362.3 51.7l97.9 97.9 30.1-30.1c21.9-21.9 21.9-57.3 0-79.2L471.6 21.7zm-299.2 220c-6.1 6.1-10.8 13.6-13.5 21.9l-29.6 88.8c-2.9 8.6-.6 18.1 5.8 24.6s15.9 8.7 24.6 5.8l88.8-29.6c8.2-2.7 15.7-7.4 21.9-13.5L437.7 172.3 339.7 74.3 172.4 241.7zM96 64C43 64 0 107 0 160L0 416c0 53 43 96 96 96l256 0c53 0 96-43 96-96l0-96c0-17.7-14.3-32-32-32s-32 14.3-32 32l0 96c0 17.7-14.3 32-32 32L96 448c-17.7 0-32-14.3-32-32l0-256c0-17.7 14.3-32 32-32l96 0c17.7 0 32-14.3 32-32s-14.3-32-32-32L96 64z"/>
                </svg>

                <span>Inscription</span>

                <svg xmlns="http://www.w3.org/2000/svg"
                     height="24px"
                     viewBox="0 -960 960 960"
                     width="24px"
                     fill="#e8eaed">
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

                    <li class="active">
                        <a href="${pageContext.request.contextPath}/mettreAJourEtudiant"
                           aria-current="page">
                            <span>Réinscrire Élève</span>
                        </a>
                    </li>

                    <li>
                        <a href="${pageContext.request.contextPath}/creer_enseignant">
                            <span>Inscription Prof</span>
                        </a>
                    </li>
                </div>
            </ul>
        </li>

        <!-- Connexion professeur -->
        <li>
            <a href="${pageContext.request.contextPath}/connexionProf">
                <svg xmlns="http://www.w3.org/2000/svg"
                     height="24px"
                     width="24px"
                     viewBox="0 0 448 512">
                    <path d="M96 128a128 128 0 1 0 256 0A128 128 0 1 0 96 128zm94.5 200.2l18.6 31L175.8 483.1l-36-146.9c-2-8.1-9.8-13.4-17.9-11.3C51.9 342.4 0 405.8 0 481.3c0 17 13.8 30.7 30.7 30.7l131.7 0c0 0 0 0 .1 0l5.5 0 112 0 5.5 0c0 0 0 0 .1 0l131.7 0c17 0 30.7-13.8 30.7-30.7c0-75.5-51.9-138.9-121.9-156.4c-8.1-2-15.9 3.3-17.9 11.3l-36 146.9L238.9 359.2l18.6-31c6.4-10.7-1.3-24.2-13.7-24.2L224 304l-19.7 0c-12.4 0-20.1 13.6-13.7 24.2z"/>
                </svg>
                <span>Connexion Prof</span>
            </a>
        </li>

        <!-- Connexion étudiant -->
        <li>
            <a href="${pageContext.request.contextPath}/loginController">
                <svg xmlns="http://www.w3.org/2000/svg"
                     height="24px"
                     width="24px"
                     viewBox="0 0 448 512">
                    <path d="M0 96C0 43 43 0 96 0l96 0 0 190.7c0 13.4 15.5 20.9 26 12.5L272 160l54 43.2c10.5 8.4 26 .9 26-12.5L352 0l32 0 32 0c17.7 0 32 14.3 32 32l0 320c0 17.7-14.3 32-32 32l0 64c17.7 0 32 14.3 32 32s-14.3 32-32 32l-32 0L96 512c-53 0-96-43-96-96L0 96zM64 416c0 17.7 14.3 32 32 32l256 0 0-64L96 384c-17.7 0-32 14.3-32 32z"/>
                </svg>
                <span>Connexion Élève</span>
            </a>
        </li>

        <!-- Connexion administrateur -->
        <li>
            <a href="${pageContext.request.contextPath}/connexionAdmin">
                <svg xmlns="http://www.w3.org/2000/svg"
                     height="24px"
                     width="24px"
                     viewBox="0 0 512 512">
                    <path d="M256 0c4.6 0 9.2 1 13.4 2.9L457.7 82.8c22 9.3 38.4 31 38.3 57.2c-.5 99.2-41.3 280.7-213.6 363.2c-16.7 8-36.1 8-52.8 0C57.3 420.7 16.5 239.2 16 140c-.1-26.2 16.3-47.9 38.3-57.2L242.7 2.9C246.8 1 251.4 0 256 0zm0 66.8l0 378.1C394 378 431.1 230.1 432 141.4L256 66.8s0 0 0 0z"/>
                </svg>
                <span>Connexion Admin</span>
            </a>
        </li>
    </ul>
</nav>

<!-- Contenu principal -->
<main>
    <header class="page-header">
        <h1>Réinscription des étudiants</h1>
        <p>Modifiez les informations nécessaires à la réinscription.</p>
    </header>

    <!-- Message envoyé par la servlet -->
    <c:if test="${not empty requestScope.messageSucces}">
        <output class="success-message">
            <c:out value="${requestScope.messageSucces}" />
        </output>
    </c:if>
    <c:if test="${not empty requestScope.messageErreur}">
        <div class="error-message" role="alert">
            <c:out value="${requestScope.messageErreur}" />
        </div>
    </c:if>

    <!-- Formulaire de réinscription -->
    <form
        action="${pageContext.request.contextPath}/mettreAJourEtudiant"
        method="post"
        class="registration-form">

        <div class="form-container">

            <!-- Colonne de gauche -->
            <div class="form-column">

                <label for="id_etudiant">Identifiant étudiant *</label>
                <input
                    type="number"
                    name="id_etudiant"
                    id="id_etudiant"
                    min="1"
                    step="1"
                    required>

                <label for="numero_adresse">Numéro d'adresse *</label>
                <input
                    type="text"
                    name="numero_adresse"
                    id="numero_adresse"
                    maxlength="20"
                    required>

                <label for="adresse">Adresse *</label>
                <input
                    type="text"
                    name="adresse"
                    id="adresse"
                    maxlength="255"
                    autocomplete="address-line1"
                    required>

                <label for="code_postal">Code postal *</label>
                <input
                    type="text"
                    name="code_postal"
                    id="code_postal"
                    inputmode="numeric"
                    pattern="[0-9]{5}"
                    maxlength="5"
                    title="Saisissez un code postal français à 5 chiffres."
                    autocomplete="postal-code"
                    required>
            </div>

            <!-- Colonne de droite -->
            <div class="form-column">

                <label for="commune">Commune *</label>
                <input
                    type="text"
                    name="commune"
                    id="commune"
                    maxlength="100"
                    autocomplete="address-level2"
                    required>

                <label for="email">Adresse e-mail *</label>
                <input
                    type="email"
                    name="email"
                    id="email"
                    maxlength="100"
                    autocomplete="email"
                    required>

                <label for="contact">Contact *</label>
                <input
                    type="tel"
                    name="contact"
                    id="contact"
                    maxlength="20"
                    autocomplete="tel"
                    required>

                <label for="classe">Classe *</label>
                <select name="classe" id="classe" required>
                    <option value="">-- Choisir une classe --</option>
                    <c:forEach items="${classes}" var="classeOption">
                        <option value="<c:out value='${classeOption.nom}' />"
                            ${param.classe eq classeOption.nom ? 'selected' : ''}>
                            <c:out value="${classeOption.nom}" />
                        </option>
                    </c:forEach>
                </select>

                <label for="filiere">Spécialité *</label>
                <select name="filiere" id="filiere" required>
                    <option value="">-- Choisir une spécialité --</option>
                    <option value="GI">GI</option>
                    <option value="GM">GM</option>
                    <option value="GC">GC</option>
                </select>
            </div>
        </div>

        <div class="submit-container">
            <button type="submit" class="btn-primary">
                Réinscrire l'étudiant
            </button>
        </div>
    </form>
</main>

</body>
</html>
