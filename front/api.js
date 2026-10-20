// Client de l'API REST : seul module du front qui connaît les URL du back.
export const API_URL = 'http://localhost:8080/api/v1';

async function appeler(chemin, options = {}) {
  const reponse = await fetch(`${API_URL}${chemin}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  const corps = await reponse.json().catch(() => null);
  if (!reponse.ok) {
    // erreurs au format problem+json (RFC 9457)
    throw new Error(corps?.detail ?? corps?.title ?? `Erreur HTTP ${reponse.status}`);
  }
  return corps;
}

const envoyer = (methode, chemin, corps) => appeler(chemin, { method: methode, body: JSON.stringify(corps) });

export const info = () => appeler('/info');

// DEBUT EXEMPLE (module exemple, à supprimer)
export const listerNotes = () => appeler('/notes');
export const creerNote = (note) => envoyer('POST', '/notes', note);
export const motsFrequents = (id, k) => appeler(`/notes/${id}/mots-frequents?k=${k}`);
// FIN EXEMPLE
