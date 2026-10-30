<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Inscription des Étudiants aux Cours</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/app.js" defer></script>
</head>
<body>

<c:set var="activeAdminPage" value="inscriptions" scope="request" />
<jsp:include page="/WEB-INF/jsp/admin/sidebar.jsp" />
<main>
    <header>
        <h1>Inscrire un Étudiant à un Cours</h1>
    </header>
<div class="container">

    <!-- Partie gauche : Liste des inscriptions -->
    <div class="container">
        <label for="filtreInscriptions">Filtrer les inscriptions :</label>
        <input type="search" id="filtreInscriptions" name="filtreInscriptions"
               placeholder="Nom, prénom, cours, classe, professeur, identifiant…"
               aria-describedby="aideFiltreInscriptions" autocomplete="off" />
        <small id="aideFiltreInscriptions">La recherche s’applique à toutes les colonnes.</small>
        <table>
            <thead>
            <tr>
                <th>ID Étudiant</th>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Classe</th>
                <th>Filière</th>
                <th>Nom du Cours</th>
                <th>Professeur</th>
                <th>Période</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty inscriptions}">
                    <c:forEach items="${inscriptions}" var="inscription">
                        <tr data-inscription-row>
                            <td><c:out value="${inscription.etudiant.idEtudiant}" /></td>
                            <td><c:out value="${inscription.etudiant.utilisateur.nom}" /></td>
                            <td><c:out value="${inscription.etudiant.utilisateur.prenom}" /></td>
                            <td><c:out value="${empty inscription.etudiant.classe ? 'N/A' : inscription.etudiant.classe.nom}" /></td>
                            <td><c:out value="${inscription.etudiant.filiere}" /></td>
                            <td><c:out value="${inscription.cours.nomCours}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty inscription.cours.enseignant and not empty inscription.cours.enseignant.utilisateur}">
                                        <c:out value="${inscription.cours.enseignant.utilisateur.prenom}" />
                                        <c:out value="${inscription.cours.enseignant.utilisateur.nom}" />
                                    </c:when>
                                    <c:otherwise>Non attribué</c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${inscription.debutCours}" /> → <c:out value="${inscription.finCours}" /></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr><td colspan="8">Aucune inscription trouvée.</td></tr>
                </c:otherwise>
            </c:choose>
            <tr id="aucunResultatFiltre" hidden>
                <td colspan="8">Aucune inscription ne correspond à cette recherche.</td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- Partie droite : Inscription -->
    <div class="container" style="margin-left: 20px;">
        <div>
            <!-- Recherche d'étudiant -->
            <form action="${pageContext.request.contextPath}/rechercherEtudiant" method="get">
                <label for="searchEtudiant">Rechercher un étudiant :</label>
                <input type="search" name="searchEtudiant" id="searchEtudiant"
                       placeholder="ID ou nom de l'étudiant"
                       value="<c:out value='${param.searchEtudiant}' />" required />
                <button type="submit">Rechercher</button>
            </form>
            <c:if test="${not empty etudiantSelectionne}">
                <div>
                    <p><strong>ID :</strong> <c:out value="${etudiantSelectionne.idEtudiant}" /></p>
                    <p><strong>Nom :</strong> <c:out value="${etudiantSelectionne.utilisateur.nom}" /></p>
                    <p><strong>Prénom :</strong> <c:out value="${etudiantSelectionne.utilisateur.prenom}" /></p>
                    <p><strong>Classe :</strong> <c:out value="${empty etudiantSelectionne.classe ? 'N/A' : etudiantSelectionne.classe.nom}" /></p>
                    <p><strong>Filière :</strong> <c:out value="${etudiantSelectionne.filiere}" /></p>
                </div>
            </c:if>
            <c:if test="${not empty requestScope.message}">
                <output class="message"><c:out value="${requestScope.message}" /></output>
            </c:if>
        </div>
        <div>
            <!-- Recherche de cours -->
            <form action="${pageContext.request.contextPath}/rechercherCours" method="get">
                <label for="searchCours">Rechercher un cours :</label>
                <input type="search" name="searchCours" id="searchCours"
                       placeholder="Nom ou identifiant du cours"
                       value="<c:out value='${param.searchCours}' />" required />
                <c:if test="${not empty etudiantSelectionne}">
                    <input type="hidden" name="id_etudiant"
                           value="<c:out value='${etudiantSelectionne.idEtudiant}' />" />
                </c:if>
                <button type="submit">Rechercher</button>
            </form>

            <c:if test="${not empty cours and not empty etudiantSelectionne}">
            <form action="${pageContext.request.contextPath}/inscrireCours" method="post">
                <input type="hidden" name="id_etudiant"
                       value="<c:out value='${etudiantSelectionne.idEtudiant}' />" />
                <label for="selectCours">Sélectionner un cours :</label>
                <select name="id_cours" id="selectCours" required>
                    <c:forEach items="${cours}" var="coursOption">
                        <option value="<c:out value='${coursOption.idCours}' />">
                            <c:out value="${coursOption.nomCours}" /> |
                            <c:choose>
                                <c:when test="${not empty coursOption.enseignant and not empty coursOption.enseignant.utilisateur}">
                                    <c:out value="${coursOption.enseignant.utilisateur.nom}" />
                                </c:when>
                                <c:otherwise>Professeur non attribué</c:otherwise>
                            </c:choose>
                        </option>
                    </c:forEach>
                </select><br />

                <!-- Sélection de la date -->
                <label for="dateCours">Date du cours :</label>
                <input type="date" name="dateCours" id="dateCours" required /><br />

                <!-- Sélection de l'heure de début -->
                <label for="debutHeure">Heure de début :</label>
                <select name="debutHeure" id="debutHeure" required>
                    <option>08:00</option><option>09:00</option>
                    <option>10:00</option><option>11:00</option>
                    <option>12:00</option><option>13:00</option>
                    <option>14:00</option><option>15:00</option>
                    <option>16:00</option><option>17:00</option>
                    <option>18:00</option><option>19:00</option>
                    <option>20:00</option>
                </select><br />

                <!-- Sélection de l'heure de fin -->
                <label for="finHeure">Heure de fin :</label>
                <select name="finHeure" id="finHeure" required>
                    <option>09:00</option><option>10:00</option>
                    <option>11:00</option><option>12:00</option>
                    <option>13:00</option><option>14:00</option>
                    <option>15:00</option><option>16:00</option>
                    <option>17:00</option><option>18:00</option>
                    <option>19:00</option><option>20:00</option>
                    <option>21:00</option>
                </select><br />

                <button type="submit">Inscrire</button>
            </form>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty requestScope.message}">
        <output class="message"><c:out value="${requestScope.message}" /></output>
    </c:if>

<footer>
    <p>© 2024 - Gestion des Inscriptions</p>
</footer>
</main>
<script>
    (() => {
        const champ = document.getElementById('filtreInscriptions');
        const lignes = Array.from(document.querySelectorAll('[data-inscription-row]'));
        const messageVide = document.getElementById('aucunResultatFiltre');

        if (!champ || lignes.length === 0 || !messageVide) return;

        const normaliser = (texte) => texte
            .toLocaleLowerCase('fr')
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .trim();

        champ.addEventListener('input', () => {
            const termes = normaliser(champ.value).split(/\s+/).filter(Boolean);
            let visibles = 0;

            lignes.forEach((ligne) => {
                const contenu = normaliser(ligne.textContent);
                const correspond = termes.every((terme) => contenu.includes(terme));
                ligne.hidden = !correspond;
                if (correspond) visibles += 1;
            });

            messageVide.hidden = visibles !== 0;
        });
    })();
</script>
</html>
