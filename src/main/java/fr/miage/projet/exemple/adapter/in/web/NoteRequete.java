package fr.miage.projet.exemple.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import fr.miage.projet.exemple.application.port.in.CreerNoteCommande;

/** Corps de POST /api/v1/notes. Validation de forme ici, règles métier dans le domaine. */
record NoteRequete(@NotBlank @Size(max = 120) String titre, @Size(max = 20_000) String contenu) {

    CreerNoteCommande versCommande() {
        return new CreerNoteCommande(titre, contenu);
    }
}
