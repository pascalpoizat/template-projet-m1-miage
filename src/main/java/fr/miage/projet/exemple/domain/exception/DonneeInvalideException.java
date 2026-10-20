package fr.miage.projet.exemple.domain.exception;

/** Une règle métier est violée par les données fournies. */
public class DonneeInvalideException extends ExempleException {

    public DonneeInvalideException(String message) {
        super(message);
    }
}
