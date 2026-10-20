package fr.miage.projet.exemple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.miage.projet.exemple.application.port.in.GestionNotesUseCase;
import fr.miage.projet.exemple.application.port.out.NoteRepository;
import fr.miage.projet.exemple.application.service.GestionNotesService;

/**
 * Assemblage de l'hexagone : le domaine et l'application ignorent Spring,
 * c'est ici que les services sont branchés sur les adaptateurs.
 */
@Configuration
class ExempleConfiguration {

    @Bean
    GestionNotesUseCase gestionNotesUseCase(NoteRepository notes) {
        return new GestionNotesService(notes);
    }
}
