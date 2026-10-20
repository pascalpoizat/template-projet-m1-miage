package fr.miage.projet.exemple.application.port.out;

import java.util.List;
import java.util.Optional;

import fr.miage.projet.exemple.domain.model.Note;
import fr.miage.projet.exemple.domain.model.NoteId;
import fr.miage.projet.exemple.domain.model.NouvelleNote;

/** Port de sortie : ce dont le module a besoin, implémenté par un adaptateur (JPA, mémoire…). */
public interface NoteRepository {

    Note ajouter(NouvelleNote note);

    Optional<Note> trouver(NoteId id);

    List<Note> toutes();
}
