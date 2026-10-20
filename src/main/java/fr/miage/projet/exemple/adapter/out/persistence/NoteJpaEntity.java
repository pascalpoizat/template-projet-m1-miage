package fr.miage.projet.exemple.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Représentation en base d'une note. Ne sort jamais de l'adaptateur. */
@Entity
@Table(name = "note")
class NoteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    protected NoteJpaEntity() {
        // requis par JPA
    }

    NoteJpaEntity(String titre, String contenu) {
        this.titre = titre;
        this.contenu = contenu;
    }

    Long getId() {
        return id;
    }

    String getTitre() {
        return titre;
    }

    String getContenu() {
        return contenu;
    }
}
