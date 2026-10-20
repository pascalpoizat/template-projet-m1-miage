// SECTION D'EXEMPLE : à supprimer avec le module exemple du back.
import * as api from './api.js';
import { $, echapper, tenter } from './commun.js';

async function afficherNotes() {
  const notes = await api.listerNotes();
  $('#liste-notes').innerHTML = notes
    .map((note) => `<li><button class="lien" data-id="${note.id}">${echapper(note.titre)}</button></li>`)
    .join('');
}

async function afficherMotsFrequents(id) {
  const mots = await api.motsFrequents(id, 5);
  $('#mots-frequents').innerHTML = `<ol>${mots
    .map((m) => `<li>${echapper(m.mot)} <span class="discret">× ${m.occurrences}</span></li>`)
    .join('')}</ol>`;
}

export function initialiserExemple() {
  $('#form-note').addEventListener('submit', (evenement) => {
    evenement.preventDefault();
    tenter(async () => {
      await api.creerNote(Object.fromEntries(new FormData(evenement.target)));
      evenement.target.reset();
      await afficherNotes();
    });
  });
  $('#liste-notes').addEventListener('click', (evenement) => {
    const id = evenement.target.dataset.id;
    if (id) tenter(() => afficherMotsFrequents(id));
  });
  return tenter(afficherNotes);
}
