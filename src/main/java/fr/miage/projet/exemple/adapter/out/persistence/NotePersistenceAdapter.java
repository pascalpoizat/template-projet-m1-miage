package fr.miage.projet.exemple.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import fr.miage.projet.exemple.application.port.out.NoteRepository;
import fr.miage.projet.exemple.domain.model.Note;
import fr.miage.projet.exemple.domain.model.NoteId;
import fr.miage.projet.exemple.domain.model.NouvelleNote;

/** Adaptateur : implémente le port {@link NoteRepository} avec Spring Data JPA. */
@Component
class NotePersistenceAdapter implements NoteRepository {

    private final NoteJpaRepository jpa;

    NotePersistenceAdapter(NoteJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Note ajouter(NouvelleNote note) {
        return versDomaine(jpa.save(new NoteJpaEntity(note.titre(), note.contenu())));
    }

    @Override
    public Optional<Note> trouver(NoteId id) {
        return jpa.findById(id.valeur()).map(NotePersistenceAdapter::versDomaine);
    }

    @Override
    public List<Note> toutes() {
        return jpa.findAll().stream().map(NotePersistenceAdapter::versDomaine).toList();
    }

    private static Note versDomaine(NoteJpaEntity entite) {
        return new Note(new NoteId(entite.getId()), entite.getTitre(), entite.getContenu());
    }
}
