// Point d'entrée : initialise chaque section du front.
import * as api from './api.js';
import { $, tenter } from './commun.js';
// DEBUT EXEMPLE
import { initialiserExemple } from './exemple.js';
// FIN EXEMPLE

tenter(async () => {
  const { name, version } = await api.info();
  $('#version').textContent = `${name} ${version}`;
});
// DEBUT EXEMPLE
initialiserExemple();
// FIN EXEMPLE
