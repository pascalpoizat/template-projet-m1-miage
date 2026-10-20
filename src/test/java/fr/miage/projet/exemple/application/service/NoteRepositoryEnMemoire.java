package fr.miage.projet.exemple.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.miage.projet.exemple.application.port.out.NoteRepository;
import fr.miage.projet.exemple.domain.model.Note;
import fr.miage.projet.exemple.domain.model.NoteId;
import fr.miage.projet.exemple.domain.model.NouvelleNote;

/** Faux adaptateur en mémoire : grâce au port de sortie, le service se teste sans base ni Spring. */
final class NoteRepositoryEnMemoire implements NoteRepository {

    private final List<Note> notes = new ArrayList<>();

    @Override
    public Note ajouter(NouvelleNote nouvelle) {
        Note note = new Note(new NoteId(notes.size() + 1L), nouvelle.titre(), nouvelle.contenu());
        notes.add(note);
        return note;
    }

    @Override
    public Optional<Note> trouver(NoteId id) {
        return notes.stream().filter(note -> note.id().equals(id)).findFirst();
    }

    @Override
    public List<Note> toutes() {
        return List.copyOf(notes);
    }
}
