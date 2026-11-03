package controller.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AdminAccessInterceptor adminAccessInterceptor;

    public WebMvcConfig(AdminAccessInterceptor adminAccessInterceptor) {
        this.adminAccessInterceptor = adminAccessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAccessInterceptor)
                .addPathPatterns("/admin/**", "/listeEtudiants", "/supprimerEtudiant",
                        "/listeProfesseurs", "/supprimerProfesseur", "/listeCours",
                        "/attribuerCours", "/rechercherProfesseur", "/listeInscriptions",
                        "/rechercherEtudiant", "/rechercherCours", "/inscrireCours",
                        "/telechargerEtudiantsCSV", "/telechargerProfesseursCSV");
    }
}
