
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Confirmation de l'inscription</title>
</head>
<body>

<h1>Confirmation des informations</h1>

<p>Veuillez vérifier les informations avant de confirmer l'inscription.</p>

<ul>
    <li><strong>Nom :</strong> <c:out value="${nom}" /></li>
    <li><strong>Prénom :</strong> <c:out value="${prenom}" /></li>
    <li><strong>Téléphone :</strong> <c:out value="${telephone}" /></li>
    <li><strong>Email :</strong> <c:out value="${email}" /></li>
    <li><strong>Numéro d'adresse :</strong> <c:out value="${numero_adresse}" /></li>
    <li><strong>Adresse :</strong> <c:out value="${adresse}" /></li>
    <li><strong>Code postal :</strong> <c:out value="${code_postal}" /></li>
    <li><strong>Commune :</strong> <c:out value="${commune}" /></li>
    <li><strong>Date de naissance :</strong> <c:out value="${date_naissance}" /></li>
    <li><strong>Classe :</strong> <c:out value="${classe}" /></li>
    <li><strong>Spécialité :</strong> <c:out value="${specialite}" /></li>
</ul>

<form action="${pageContext.request.contextPath}/enregistrer_eleve"
      method="post">

              <input type="hidden" name="inscriptionToken"
                                    value="<c:out value='${inscriptionToken}' />">
    <input type="hidden" name="nom" value="<c:out value='${nom}' />">
    <input type="hidden" name="prenom" value="<c:out value='${prenom}' />">
    <input type="hidden" name="telephone" value="<c:out value='${telephone}' />">
    <input type="hidden" name="email" value="<c:out value='${email}' />">

    <input type="hidden" name="numero_adresse"
           value="<c:out value='${numero_adresse}' />">
    <input type="hidden" name="adresse" value="<c:out value='${adresse}' />">
    <input type="hidden" name="code_postal"
           value="<c:out value='${code_postal}' />">
    <input type="hidden" name="commune" value="<c:out value='${commune}' />">
    <input type="hidden" name="date_naissance"
           value="<c:out value='${date_naissance}' />">
    <input type="hidden" name="classe" value="<c:out value='${classe}' />">
    <input type="hidden" name="specialite"
           value="<c:out value='${specialite}' />">

    <button type="submit">Confirmer l'inscription</button>
</form>

</body>
</html>
