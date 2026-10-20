#!/usr/bin/env bash
# Initialise le dépôt modèle pour votre projet : renomme package, classes, artefact, base de données et titres.
#
# Usage : ./outils/initialiser.sh <nom-du-projet> ["Nom affiché"] [--supprimer-exemple]
#   <nom-du-projet>      en kebab-case, ex. premiere-impression
#                        -> package fr.miage.premiereimpression, classe PremiereImpressionApplication,
#                           artefact premiere-impression-back, base premiereimpression
#   "Nom affiché"        titre du README, du dossier et du front (par défaut : le nom du projet)
#   --supprimer-exemple  retire le module exemple (back, migration V2, tests, front, données de démo)
#
# À lancer une seule fois, sur un dépôt fraîchement cloné et sans modification.
set -euo pipefail

racine="$(cd "$(dirname "$0")/.." && pwd)"
cd "$racine"

supprimer_exemple=false
arguments=()
for argument in "$@"; do
  if [[ "$argument" == "--supprimer-exemple" ]]; then supprimer_exemple=true; else arguments+=("$argument"); fi
done
if [[ ${#arguments[@]} -lt 1 ]]; then
  sed -n '2,12p' "$0"; exit 1
fi

nom="${arguments[0]}"
affiche="${arguments[1]:-$nom}"
if [[ ! "$nom" =~ ^[a-z][a-z0-9]*(-[a-z0-9]+)*$ ]]; then
  echo "Nom invalide : « $nom ». Attendu : kebab-case, ex. premiere-impression" >&2; exit 1
fi
if [[ ! -d src/main/java/fr/miage/projet ]]; then
  echo "Le package fr.miage.projet n'existe plus : le dépôt semble déjà initialisé." >&2; exit 1
fi

paquet="${nom//-/}"
classe=""
IFS='-' read -ra morceaux <<< "$nom"
for morceau in "${morceaux[@]}"; do classe+="$(tr '[:lower:]' '[:upper:]' <<< "${morceau:0:1}")${morceau:1}"; done

deplacer() {
  if git ls-files --error-unmatch "$1" > /dev/null 2>&1; then git mv "$1" "$2"; else mv "$1" "$2"; fi
}

echo "→ package fr.miage.$paquet, classe ${classe}Application, artefact $nom-back, base $paquet"

if $supprimer_exemple; then
  echo "→ suppression du module exemple"
  rm -rf src/main/java/fr/miage/projet/exemple src/test/java/fr/miage/projet/exemple \
         src/main/resources/db/migration/V2__exemple_note.sql front/exemple.js
  cat > src/main/resources/db/demo/R__donnees_demo.sql <<'SQL'
-- Jeu de démonstration, chargé uniquement avec le profil « demo » (voir application-demo.properties).
-- Migration « répétable » (R__) et idempotente : écrire des INSERT ... WHERE NOT EXISTS (...).
SELECT 1;
SQL
  perl -0pi -e 's/\n?[ \t]*(<!--|\/\/) DEBUT EXEMPLE.*?FIN EXEMPLE( -->)?\n/\n/gs' front/index.html front/api.js front/app.js
fi

deplacer src/main/java/fr/miage/projet "src/main/java/fr/miage/$paquet"
deplacer src/test/java/fr/miage/projet "src/test/java/fr/miage/$paquet"
deplacer "src/main/java/fr/miage/$paquet/ProjetApplication.java" "src/main/java/fr/miage/$paquet/${classe}Application.java"
deplacer "src/test/java/fr/miage/$paquet/ProjetApplicationTests.java" "src/test/java/fr/miage/$paquet/${classe}ApplicationTests.java"

export PAQUET="$paquet" CLASSE="$classe" NOM="$nom" AFFICHE="$affiche"
find src settings.gradle compose.yaml README.md CONTRIBUTING.md doc/dossier.md front site -type f \
     \( -name '*.java' -o -name '*.properties' -o -name '*.sql' -o -name '*.gradle' -o -name '*.yaml' \
        -o -name '*.md' -o -name '*.html' -o -name '*.js' \) -print0 |
  xargs -0 perl -pi -e '
    s/fr\.miage\.projet\b/fr.miage.$ENV{PAQUET}/g;
    s{fr/miage/projet\b}{fr/miage/$ENV{PAQUET}}g;
    s/\bProjetApplication/$ENV{CLASSE}Application/g;
    s/\bprojet-back\b/$ENV{NOM}-back/g;
    s{localhost:5432/projet\b}{localhost:5432/$ENV{PAQUET}}g;
    s/^(spring\.datasource\.(username|password))=projet$/$1=$ENV{PAQUET}/;
    s/jdbc:h2:mem:projet;/jdbc:h2:mem:$ENV{PAQUET};/g;
    s/^(\s+POSTGRES_(DB|USER|PASSWORD)): projet$/$1: $ENV{PAQUET}/;
    s/NOM DU PROJET/$ENV{AFFICHE}/g;
    s{<title>Projet</title>}{<title>$ENV{AFFICHE}</title>}g;
    s{<h1>Projet</h1>}{<h1>$ENV{AFFICHE}</h1>}g;
  '

cat <<SUITE
✓ Projet initialisé. Étapes suivantes :
  1. ./gradlew build                      (tout doit être vert)
  2. git config user.name / user.email    (identité de VOTRE compte GitHub)
  3. remplacer ORG/REPO dans README.md et doc/dossier.md, puis renseigner gradle.properties (SonarCloud)
  4. compléter doc/dossier.md, puis : make -C doc
SUITE
