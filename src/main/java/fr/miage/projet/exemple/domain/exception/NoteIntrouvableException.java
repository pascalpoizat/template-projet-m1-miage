package fr.miage.projet.exemple.domain.exception;

import fr.miage.projet.exemple.domain.model.NoteId;

public class NoteIntrouvableException extends ExempleException {

    public NoteIntrouvableException(NoteId id) {
        super("Note introuvable : " + id.valeur());
    }
}
