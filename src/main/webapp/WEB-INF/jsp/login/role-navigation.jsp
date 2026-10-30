<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav id="sidebar" aria-label="Navigation principale">
    <ul>
        <li>
            <span class="logo">
                <img src="${pageContext.request.contextPath}/images/logo.png"
                     height="36" alt="myCY Scolarité">
            </span>
            <button type="button" onclick="toggleSidebar()" id="toggle-btn"
                    aria-label="Réduire ou développer le menu">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M4 6h16v2H4zm0 5h16v2H4zm0 5h16v2H4z"/>
                </svg>
            </button>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/index.jsp">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M12 3 2 12h3v9h6v-6h2v6h6v-9h3z"/>
                </svg><span>Accueil</span>
            </a>
        </li>
        <li>
            <button type="button" onclick="toggleSubMenu(this)" class="dropdown-btn">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M4 4h16v2H4zm0 5h16v2H4zm0 5h16v2H4zm0 5h16v2H4z"/>
                </svg><span>Inscription</span>
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="m7 10 5 5 5-5z"/>
                </svg>
            </button>
            <ul class="sub-menu">
                <div>
                    <li><a href="${pageContext.request.contextPath}/creer_eleve"><span>Inscription élève</span></a></li>
                    <li><a href="${pageContext.request.contextPath}/mettreAJourEtudiant"><span>Réinscription élève</span></a></li>
                    <li><a href="${pageContext.request.contextPath}/creer_enseignant"><span>Inscription professeur</span></a></li>
                </div>
            </ul>
        </li>
        <li class="${requestScope.activeLoginRole eq 'prof' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/connexionProf">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8m-7 9a7 7 0 0 1 14 0z"/>
                </svg><span>Connexion professeur</span>
            </a>
        </li>
        <li class="${requestScope.activeLoginRole eq 'etudiant' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/loginController">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M16 11a3 3 0 1 0 0-6 3 3 0 0 0 0 6M8 11a3 3 0 1 0 0-6 3 3 0 0 0 0 6m0 2a5 5 0 0 0-5 5v2h10v-2a5 5 0 0 0-5-5m8 0h-1.1a6.9 6.9 0 0 1 2.1 5v2h4v-2a5 5 0 0 0-5-5"/>
                </svg><span>Connexion étudiant</span>
            </a>
        </li>
        <li class="${requestScope.activeLoginRole eq 'admin' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/connexionAdmin">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M12 2 3 6v5c0 5.5 3.8 10.4 9 11 5.2-.6 9-5.5 9-11V6zm0 4 5 2v3c0 3.9-2.5 7.4-5 8-2.5-.6-5-4.1-5-8V8z"/>
                </svg><span>Connexion administrateur</span>
            </a>
        </li>
    </ul>
</nav>
