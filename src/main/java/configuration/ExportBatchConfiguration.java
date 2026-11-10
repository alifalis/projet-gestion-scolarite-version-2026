package configuration;

import modele.Enseignant;
import modele.Etudiant;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import java.nio.charset.StandardCharsets;

@Configuration
public class ExportBatchConfiguration {

    private static final int PAGE_SIZE = 500;

    @Bean
    public Job etudiantsExportJob(JobRepository jobRepository, Step etudiantsExportStep) {
        return new JobBuilder("etudiantsExportJob", jobRepository)
                .start(etudiantsExportStep)
                .build();
    }

    @Bean
    public Job enseignantsExportJob(JobRepository jobRepository, Step enseignantsExportStep) {
        return new JobBuilder("enseignantsExportJob", jobRepository)
                .start(enseignantsExportStep)
                .build();
    }

    @Bean
    public Step etudiantsExportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<Etudiant> etudiantsExportReader,
            FlatFileItemWriter<Etudiant> etudiantsExportWriter) {
        return new StepBuilder("etudiantsExportStep", jobRepository)
                .<Etudiant, Etudiant>chunk(PAGE_SIZE, transactionManager)
                .reader(etudiantsExportReader)
                .writer(etudiantsExportWriter)
                .build();
    }

    @Bean
    public Step enseignantsExportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<Enseignant> enseignantsExportReader,
            FlatFileItemWriter<Enseignant> enseignantsExportWriter) {
        return new StepBuilder("enseignantsExportStep", jobRepository)
                .<Enseignant, Enseignant>chunk(PAGE_SIZE, transactionManager)
                .reader(enseignantsExportReader)
                .writer(enseignantsExportWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Etudiant> etudiantsExportReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Etudiant>()
                .name("etudiantsExportReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        select e from Etudiant e
                        join fetch e.utilisateur
                        left join fetch e.classe
                        order by e.idEtudiant
                        """)
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Enseignant> enseignantsExportReader(
            EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Enseignant>()
                .name("enseignantsExportReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        select e from Enseignant e
                        join fetch e.utilisateur
                        left join fetch e.specialite
                        order by e.idEnseignant
                        """)
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Etudiant> etudiantsExportWriter(
            @Value("#{jobParameters['outputPath']}") String outputPath) {
        return new FlatFileItemWriterBuilder<Etudiant>()
                .name("etudiantsExportWriter")
                .resource(new FileSystemResource(outputPath))
                .encoding(StandardCharsets.UTF_8.name())
                .headerCallback(writer -> writer.write("\uFEFFID,Nom,Prénom,Email,Contact,Classe,Filière"))
                .lineAggregator(etudiantLineAggregator())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Enseignant> enseignantsExportWriter(
            @Value("#{jobParameters['outputPath']}") String outputPath) {
        return new FlatFileItemWriterBuilder<Enseignant>()
                .name("enseignantsExportWriter")
                .resource(new FileSystemResource(outputPath))
                .encoding(StandardCharsets.UTF_8.name())
                .headerCallback(writer -> writer.write("\uFEFFID,Nom,Prénom,Email,Spécialité"))
                .lineAggregator(enseignantLineAggregator())
                .build();
    }

    private LineAggregator<Etudiant> etudiantLineAggregator() {
        return e -> String.join(",",
                csv(e.getIdEtudiant()),
                csv(e.getUtilisateur().getNom()),
                csv(e.getUtilisateur().getPrenom()),
                csv(e.getUtilisateur().getEmail()),
                csv(e.getContact()),
                csv(e.getClasse() == null ? "N/A" : e.getClasse().getNom()),
                csv(e.getFiliere()));
    }

    private LineAggregator<Enseignant> enseignantLineAggregator() {
        return e -> String.join(",",
                csv(e.getIdEnseignant()),
                csv(e.getUtilisateur().getNom()),
                csv(e.getUtilisateur().getPrenom()),
                csv(e.getUtilisateur().getEmail()),
                csv(e.getSpecialite() == null ? "N/A" : e.getSpecialite().getNom()));
    }

    private String csv(Object value) {
        String text = value == null ? "" : value.toString();
        if (!text.isEmpty() && "=+-@".indexOf(text.charAt(0)) >= 0) {
            text = "'" + text;
        }
        return "\"" + text.replace("\"", "\"\"") + "\"";
    }
}
