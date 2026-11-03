<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav id="sidebar" aria-label="Navigation administrateur">
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
        <li class="${requestScope.activeAdminPage eq 'home' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/admin/pageAdmin.jsp">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M12 3 2 12h3v9h6v-6h2v6h6v-9h3z"/>
                </svg><span>Accueil</span>
            </a>
        </li>
        <li class="${requestScope.activeAdminPage eq 'enseignants' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/listeProfesseurs">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8m-7 9a7 7 0 0 1 14 0z"/>
                </svg><span>Liste professeurs</span>
            </a>
        </li>
        <li class="${requestScope.activeAdminPage eq 'etudiants' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/listeEtudiants">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M16 11a3 3 0 1 0 0-6 3 3 0 0 0 0 6M8 11a3 3 0 1 0 0-6 3 3 0 0 0 0 6m0 2a5 5 0 0 0-5 5v2h10v-2a5 5 0 0 0-5-5m8 0h-1.1a6.9 6.9 0 0 1 2.1 5v2h4v-2a5 5 0 0 0-5-5"/>
                </svg><span>Liste étudiants</span>
            </a>
        </li>
        <li class="${requestScope.activeAdminPage eq 'cours' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/listeCours">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M5 3h14a2 2 0 0 1 2 2v16H5a3 3 0 0 1-3-3V6a3 3 0 0 1 3-3m1 4v2h12V7zm0 4v2h12v-2zm0 4v2h8v-2z"/>
                </svg><span>Attribuer cours</span>
            </a>
        </li>
        <li class="${requestScope.activeAdminPage eq 'inscriptions' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/listeInscriptions">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M7 2h2v2h6V2h2v2h3a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h3zm13 8H4v10h16zM7 13h3v3H7z"/>
                </svg><span>Attribuer cours aux étudiants</span>
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/logout">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" width="24"
                     viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M10 3H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h6v-2H4V5h6zm5 4-1.4 1.4 2.6 2.6H8v2h8.2l-2.6 2.6L15 17l5-5z"/>
                </svg><span>Se déconnecter</span>
            </a>
        </li>
    </ul>
</nav>
