package fr.miage.projet.exemple.domain.algo;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import fr.miage.projet.exemple.domain.exception.DonneeInvalideException;
import fr.miage.projet.exemple.domain.model.MotFrequent;

/**
 * Les k mots les plus fréquents d'un texte.
 * <p>
 * Comptage dans une table de hachage en O(n), puis sélection des k meilleurs avec un tas
 * de taille k en O(m log k), m étant le nombre de mots distincts : plus efficace qu'un tri complet
 * en O(m log m) quand k est petit.
 */
public class MotsFrequents {

    /** Ordre total : plus d'occurrences d'abord, puis ordre alphabétique (résultat déterministe). */
    static final Comparator<MotFrequent> MEILLEUR_D_ABORD = Comparator
            .comparingInt(MotFrequent::occurrences).reversed()
            .thenComparing(MotFrequent::mot);

    public List<MotFrequent> calculer(String texte, int k) {
        if (k <= 0) {
            throw new DonneeInvalideException("k doit être strictement positif");
        }
        return meilleurs(compter(texte), k);
    }

    Map<String, Integer> compter(String texte) {
        Map<String, Integer> occurrences = new HashMap<>();
        for (String mot : normaliser(texte).split("[^a-z0-9]+")) {
            if (!mot.isEmpty()) {
                occurrences.merge(mot, 1, Integer::sum);
            }
        }
        return occurrences;
    }

    /** Minuscules et sans accents : « Été » et « ete » sont le même mot. */
    private static String normaliser(String texte) {
        return Normalizer.normalize(texte.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    private static List<MotFrequent> meilleurs(Map<String, Integer> occurrences, int k) {
        // tas « le pire en tête » : on évince le pire dès que le tas dépasse k éléments
        PriorityQueue<MotFrequent> tas = new PriorityQueue<>(MEILLEUR_D_ABORD.reversed());
        occurrences.forEach((mot, nombre) -> {
            tas.add(new MotFrequent(mot, nombre));
            if (tas.size() > k) {
                tas.poll();
            }
        });
        List<MotFrequent> resultat = new ArrayList<>(tas);
        resultat.sort(MEILLEUR_D_ABORD);
        return resultat;
    }
}
