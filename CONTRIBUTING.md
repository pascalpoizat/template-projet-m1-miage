# Contribuer

## Une feature, une personne, un algorithme

Chaque membre porte **une feature par itération**. Une feature a un effet sur **le schéma de BD, le back, l'API REST et le front**, et repose sur **un algorithme identifié** (pas de CRUD).

## La démarche, dans l'ordre

On ne commence une étape que lorsque la précédente est terminée.

1. **Idéation** : issue (modèle « Feature ») et fiche `doc/features/<version>-F<n>-<nom>.md`, à partir de [`MODELE-feature.md`](doc/features/MODELE-feature.md). *Terminé quand* la fiche est validée par un autre membre.
2. **Back**, en architecture hexagonale (le module `exemple` montre chaque élément) :
   - migration Flyway `V<n>__<feature>.sql` ;
   - `domain` : modèle, règles métier, **algorithme**, en Java pur ;
   - `application` : ports `in` (cas d'utilisation) et `out` (persistance…), services sans Spring ;
   - `adapter/out` : implémentation des ports de sortie (JPA) ;
   - `config` : assemblage Spring.

   *Terminé quand* le code compile et ArchUnit passe.
3. **Tests** :
   - algorithme : cas nominaux, cas limites, invariants, **propriétés** sur données aléatoires à graine fixe ;
   - services avec des **faux adaptateurs en mémoire** ;
   - intégration de la persistance (`@DataJpaTest` sur le schéma Flyway).

   *Terminé quand* `./gradlew build` passe et chaque critère d'acceptation est couvert.
4. **API REST** : `adapter/in/web` (contrôleurs, DTO, erreurs `problem+json`) sous `/api/v1/...`, et tests d'intégration MockMvc. *Terminé quand* c'est visible dans Swagger UI.
5. **Front** : uniquement via l'API (`front/api.js`). *Terminé quand* le scénario se joue de bout en bout dans l'application lancée.

Règles d'architecture, vérifiées par ArchUnit :
- `domain` ne dépend que de Java ;
- `application` ne dépend ni des adaptateurs ni de Spring ;
- un module n'en utilise un autre que par ses ports.

## Branches et commits

- `main` : versions livrées et taguées (`v0.1.0-SNAPSHOT`, `v0.1.0`, …)
- `develop` : intégration, créée depuis `main` une fois pour toutes au démarrage (voir le README), car un dépôt créé à partir du modèle n'a que `main`
- `feature/<issue>-<nom-court>` : une branche par feature, fusionnée dans `develop` par une pull request relue par un autre membre

Avant ton premier commit, vérifie ton identité, sinon ta contribution risque de ne pas t'être attribuée :

```bash
git config user.name  "Prénom Nom"
git config user.email "email-associé-à-ton-compte-github"
```

## Migrations de BD

- Une migration poussée sur `develop` ou `main` n'est **jamais modifiée** : on en ajoute une nouvelle.
- Se coordonner dans l'issue pour éviter que deux personnes prennent le même `V<n>`.
- Données de démonstration : `src/main/resources/db/demo/R__donnees_demo.sql`, idempotentes.

## Livraisons

```bash
git tag -a v0.1.0-SNAPSHOT -m "Pré-livraison fin de séance"
git push origin v0.1.0-SNAPSHOT
```

À la livraison : mettre à jour `doc/part-ia.json`, le dossier et la page de projet, vérifier `./gradlew build` et `make -C doc`, puis pousser le tag `v0.1.0`. La CI publie la release, avec le jar et `dossier.pdf`, et sa version est celle du tag.

## Usage de l'IA

Voir [CLAUDE.md](CLAUDE.md) et le protocole du dossier. Chaque pull request renseigne sa section « Usage de l'IA » et sa part IA par compétence.
