package fr.miage.projet.exemple.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.miage.projet.exemple.domain.exception.DonneeInvalideException;
import fr.miage.projet.exemple.domain.exception.ExempleException;
import fr.miage.projet.exemple.domain.exception.NoteIntrouvableException;

/** Traduit les erreurs métier en réponses HTTP au format RFC 9457 (application/problem+json). */
@RestControllerAdvice(basePackageClasses = ExempleExceptionHandler.class)
class ExempleExceptionHandler {

    @ExceptionHandler(NoteIntrouvableException.class)
    ProblemDetail introuvable(NoteIntrouvableException exception) {
        return probleme(HttpStatus.NOT_FOUND, "Note introuvable", exception);
    }

    @ExceptionHandler(DonneeInvalideException.class)
    ProblemDetail donneeInvalide(DonneeInvalideException exception) {
        return probleme(HttpStatus.BAD_REQUEST, "Donnée invalide", exception);
    }

    private static ProblemDetail probleme(HttpStatus statut, String titre, ExempleException exception) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(statut, exception.getMessage());
        probleme.setTitle(titre);
        return probleme;
    }
}
