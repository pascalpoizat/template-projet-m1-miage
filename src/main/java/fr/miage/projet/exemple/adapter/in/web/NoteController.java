package fr.miage.projet.exemple.adapter.in.web;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.miage.projet.exemple.application.port.in.GestionNotesUseCase;
import fr.miage.projet.exemple.domain.model.Note;
import fr.miage.projet.exemple.domain.model.NoteId;

/** Adaptateur entrant : traduit HTTP en appels du port d'entrée. Aucune logique métier ici. */
@RestController
@RequestMapping("/api/v1/notes")
class NoteController {

    private final GestionNotesUseCase gestionNotes;

    NoteController(GestionNotesUseCase gestionNotes) {
        this.gestionNotes = gestionNotes;
    }

    @PostMapping
    ResponseEntity<NoteReponse> creer(@Valid @RequestBody NoteRequete requete) {
        Note note = gestionNotes.creer(requete.versCommande());
        URI emplacement = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(note.id().valeur()).toUri();
        return ResponseEntity.created(emplacement).body(NoteReponse.depuis(note));
    }

    @GetMapping
    List<NoteReponse> lister() {
        return gestionNotes.lister().stream().map(NoteReponse::depuis).toList();
    }

    @GetMapping("/{id}")
    NoteReponse trouver(@PathVariable long id) {
        return NoteReponse.depuis(gestionNotes.trouver(new NoteId(id)));
    }

    @GetMapping("/{id}/mots-frequents")
    List<MotFrequentReponse> motsFrequents(@PathVariable long id, @RequestParam(defaultValue = "5") int k) {
        return gestionNotes.motsFrequents(new NoteId(id), k).stream().map(MotFrequentReponse::depuis).toList();
    }
}
