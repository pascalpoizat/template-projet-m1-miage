# Front

Client web statique (HTML, CSS, JavaScript natif), sans étape de build. Il ne communique qu'avec l'API REST : toutes les URL sont dans `api.js`.

```bash
./gradlew bootRun --args='--spring.profiles.active=h2,demo'   # back, depuis la racine du dépôt
python3 -m http.server 8000 -d front                           # front, sur l'origine autorisée par le CORS du back
```

Puis ouvrir http://localhost:8000. Une section par module (`exemple.js` est à supprimer). Le choix du front est libre (web, natif, CLI) : remplacez ce dossier si besoin, la seule règle est de ne passer que par l'API.
