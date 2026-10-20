package fr.miage.projet.exemple.adapter.in.web;

import fr.miage.projet.exemple.domain.model.MotFrequent;

record MotFrequentReponse(String mot, int occurrences) {

    static MotFrequentReponse depuis(MotFrequent motFrequent) {
        return new MotFrequentReponse(motFrequent.mot(), motFrequent.occurrences());
    }
}
