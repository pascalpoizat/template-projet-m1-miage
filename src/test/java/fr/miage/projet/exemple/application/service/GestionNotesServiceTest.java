package fr.miage.projet.exemple.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import fr.miage.projet.exemple.application.port.in.CreerNoteCommande;
import fr.miage.projet.exemple.domain.exception.DonneeInvalideException;
import fr.miage.projet.exemple.domain.exception.NoteIntrouvableException;
import fr.miage.projet.exemple.domain.model.MotFrequent;
import fr.miage.projet.exemple.domain.model.Note;
import fr.miage.projet.exemple.domain.model.NoteId;

class GestionNotesServiceTest {

    private final NoteRepositoryEnMemoire depot = new NoteRepositoryEnMemoire();
    private final GestionNotesService service = new GestionNotesService(depot);

    @Test
    void creeEtRetrouveUneNote() {
        Note note = service.creer(new CreerNoteCommande("Titre", "texte"));

        assertThat(service.trouver(note.id())).isEqualTo(note);
        assertThat(service.lister()).containsExactly(note);
    }

    @Test
    void nEnregistreRienSiLaNoteEstInvalide() {
        assertThatThrownBy(() -> service.creer(new CreerNoteCommande(" ", "texte")))
                .isInstanceOf(DonneeInvalideException.class);
        assertThat(depot.toutes()).isEmpty();
    }

    @Test
    void calculeLesMotsFrequentsDUneNote() {
        Note note = service.creer(new CreerNoteCommande("Titre", "chat chien chat"));

        assertThat(service.motsFrequents(note.id(), 1)).containsExactly(new MotFrequent("chat", 2));
    }

    @Test
    void signaleUneNoteIntrouvable() {
        NoteId inconnue = new NoteId(42);
        assertThatThrownBy(() -> service.motsFrequents(inconnue, 3)).isInstanceOf(NoteIntrouvableException.class);
    }
}
