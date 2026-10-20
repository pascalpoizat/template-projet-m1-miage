package fr.miage.projet.exemple.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import fr.miage.projet.exemple.domain.model.Note;
import fr.miage.projet.exemple.domain.model.NouvelleNote;

/** Test d'intégration : l'adaptateur JPA sur le schéma réel créé par les migrations Flyway. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // garde la base H2 en mode PostgreSQL de src/test/resources
@Import(NotePersistenceAdapter.class)
class PersistanceNoteIT {

    @Autowired
    private NotePersistenceAdapter adaptateur;

    @Autowired
    private NoteJpaRepository jpa;

    @Test
    void enregistreEtRelitUneNote() {
        Note note = adaptateur.ajouter(new NouvelleNote("Titre", "texte"));

        assertThat(adaptateur.trouver(note.id())).contains(note);
        assertThat(adaptateur.toutes()).containsExactly(note);
    }

    @Test
    void leSchemaRefuseUnTitreVide() {
        // la base est le dernier rempart, même si le domaine est contourné
        assertThatThrownBy(() -> jpa.saveAndFlush(new NoteJpaEntity("   ", "texte")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
