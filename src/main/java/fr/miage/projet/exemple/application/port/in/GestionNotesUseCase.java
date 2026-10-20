package fr.miage.projet.exemple.application.port.in;

import java.util.List;

import fr.miage.projet.exemple.domain.model.MotFrequent;
import fr.miage.projet.exemple.domain.model.Note;
import fr.miage.projet.exemple.domain.model.NoteId;

/** Port d'entrée : ce que le module offre aux adaptateurs entrants (API REST, CLI…). */
public interface GestionNotesUseCase {

    Note creer(CreerNoteCommande commande);

    Note trouver(NoteId id);

    List<Note> lister();

    List<MotFrequent> motsFrequents(NoteId id, int k);
}
