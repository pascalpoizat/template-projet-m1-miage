package fr.miage.projet.exemple.domain.algo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.miage.projet.exemple.domain.exception.DonneeInvalideException;
import fr.miage.projet.exemple.domain.model.MotFrequent;

/** Tests de l'algorithme : cas nominaux, cas limites, et une propriété vérifiée sur des données aléatoires. */
class MotsFrequentsTest {

    private final MotsFrequents algo = new MotsFrequents();

    @Test
    void classeParNombreDOccurrencesDecroissant() {
        assertThat(algo.calculer("b a b c b a", 3)).containsExactly(
                new MotFrequent("b", 3), new MotFrequent("a", 2), new MotFrequent("c", 1));
    }

    @Test
    void departageLesEgalitesParOrdreAlphabetique() {
        assertThat(algo.calculer("zebre ane chat", 2))
                .containsExactly(new MotFrequent("ane", 1), new MotFrequent("chat", 1));
    }

    @Test
    void ignoreLaCasseLesAccentsEtLaPonctuation() {
        assertThat(algo.calculer("Été, ete ! ÉTÉ.", 5)).containsExactly(new MotFrequent("ete", 3));
    }

    @Test
    void renvoieMoinsDeKMotsSiLeTexteEnContientMoins() {
        assertThat(algo.calculer("un deux", 10)).hasSize(2);
    }

    @Test
    void unTexteVideNeDonneAucunMot() {
        assertThat(algo.calculer("  ... ", 3)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void refuseUnKNonStrictementPositif(int k) {
        assertThatThrownBy(() -> algo.calculer("texte", k)).isInstanceOf(DonneeInvalideException.class);
    }

    static LongStream graines() {
        return LongStream.rangeClosed(1, 20);
    }

    /** Propriété : le tas de taille k donne le même résultat qu'un tri complet (graines fixes = reproductible). */
    @ParameterizedTest(name = "graine {0}")
    @MethodSource("graines")
    void donneLeMemeResultatQuUnTriComplet(long graine) {
        Random hasard = new Random(graine);
        StringBuilder texte = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            texte.append("mot").append(hasard.nextInt(40)).append(' ');
        }
        int k = 1 + hasard.nextInt(15);

        List<MotFrequent> parTriComplet = algo.compter(texte.toString()).entrySet().stream()
                .map(entree -> new MotFrequent(entree.getKey(), entree.getValue()))
                .sorted(MotsFrequents.MEILLEUR_D_ABORD)
                .limit(k)
                .toList();

        assertThat(algo.calculer(texte.toString(), k)).isEqualTo(parTriComplet);
    }
}
