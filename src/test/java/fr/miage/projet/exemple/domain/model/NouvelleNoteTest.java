package fr.miage.projet.exemple.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.miage.projet.exemple.domain.exception.DonneeInvalideException;

/** Test unitaire d'un objet du domaine : règles métier et cas limites. */
class NouvelleNoteTest {

    @Test
    void retireLesEspacesAutourDuTitre() {
        assertThat(new NouvelleNote("  Titre  ", "texte").titre()).isEqualTo("Titre");
    }

    @Test
    void unContenuAbsentDevientVide() {
        assertThat(new NouvelleNote("Titre", null).contenu()).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "   " })
    void refuseUnTitreVide(String titre) {
        assertThatThrownBy(() -> new NouvelleNote(titre, "texte")).isInstanceOf(DonneeInvalideException.class);
    }

    @Test
    void accepteUnTitreDeLongueurMaximaleMaisPasAuDela() {
        String maximal = "x".repeat(NouvelleNote.LONGUEUR_MAX_TITRE);
        assertThat(new NouvelleNote(maximal, "").titre()).hasSize(NouvelleNote.LONGUEUR_MAX_TITRE);
        assertThatThrownBy(() -> new NouvelleNote(maximal + "x", "")).isInstanceOf(DonneeInvalideException.class);
    }
}
