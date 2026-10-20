package fr.miage.projet.exemple.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface NoteJpaRepository extends JpaRepository<NoteJpaEntity, Long> {
}
