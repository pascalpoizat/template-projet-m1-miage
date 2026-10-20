package fr.miage.projet.exemple.adapter.in.web;

import fr.miage.projet.exemple.domain.model.Note;

record NoteReponse(long id, String titre, String contenu) {

    static NoteReponse depuis(Note note) {
        return new NoteReponse(note.id().valeur(), note.titre(), note.contenu());
    }
}
