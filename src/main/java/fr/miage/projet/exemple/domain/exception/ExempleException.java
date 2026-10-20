package fr.miage.projet.exemple.domain.exception;

/** Racine des erreurs métier du module. */
public abstract class ExempleException extends RuntimeException {

    protected ExempleException(String message) {
        super(message);
    }
}
