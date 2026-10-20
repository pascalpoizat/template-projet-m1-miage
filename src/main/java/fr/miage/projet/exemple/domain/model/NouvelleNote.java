package fr.miage.projet.exemple.domain.model;

import fr.miage.projet.exemple.domain.exception.DonneeInvalideException;

/** Note pas encore enregistrée : c'est ici, à l'entrée du système, que les règles métier sont vérifiées. */
public record NouvelleNote(String titre, String contenu) {

    static final int LONGUEUR_MAX_TITRE = 120;

    public NouvelleNote {
        if (titre == null || titre.isBlank()) {
            throw new DonneeInvalideException("Le titre est obligatoire");
        }
        titre = titre.strip();
        if (titre.length() > LONGUEUR_MAX_TITRE) {
            throw new DonneeInvalideException("Le titre dépasse " + LONGUEUR_MAX_TITRE + " caractères");
        }
        contenu = contenu == null ? "" : contenu;
    }
}
