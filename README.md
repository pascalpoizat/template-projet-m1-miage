# NOM DU PROJET

<!-- Remplacer ORG/REPO et SONAR_KEY une fois le dépôt créé -->
[![CI](https://github.com/ORG/REPO/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/ORG/REPO/actions/workflows/ci.yml)
[![Release](https://img.shields.io/github/v/release/ORG/REPO?include_prereleases)](https://github.com/ORG/REPO/releases)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![codecov](https://codecov.io/gh/ORG/REPO/graph/badge.svg)](https://codecov.io/gh/ORG/REPO)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=SONAR_KEY&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=SONAR_KEY)

Projet M1 MIAGE Apprentissage. **Page de projet : https://ORG.github.io/REPO/** · Le dossier de projet est dans [`doc/dossier.md`](doc/dossier.md), les règles de travail avec l'IA dans [`CLAUDE.md`](CLAUDE.md).

## Démarrage (dépôt modèle)

> **Guide complet : [`documentation.pdf`](documentation.pdf)** (source : `documentation.md`). Rien n'y est imposé : ni ce modèle, ni le `CLAUDE.md`, ni l'usage de l'IA.
>
> **Aperçu de la page de projet** générée par ce modèle (squelette à compléter) : https://pascalpoizat.github.io/template-projet-m1-miage/
>
> **Exemple complet** construit à partir de ce modèle (équipe fictive) : [premiere-impression-exemple](https://github.com/pascalpoizat/premiere-impression-exemple), avec son [dossier](https://github.com/pascalpoizat/premiere-impression-exemple/releases/tag/v0.1.0), sa [page de projet](https://pascalpoizat.github.io/premiere-impression-exemple/) et sa release v0.1.0.

Ce dépôt est un **point de départ clé en main** : back Java hexagonal testé, front web, CI, dossier compilé en PDF, suivi de la part de l'IA. Un **module d'exemple** (`exemple` : des notes et leurs mots les plus fréquents) montre toutes les couches et tous les types de tests ; ce n'est pas un modèle de feature, son algorithme est volontairement minuscule.

1. **Une personne de l'équipe** crée le dépôt GitHub à partir de ce modèle, puis invite les autres membres.
2. Chaque membre clone le dépôt et **configure son identité Git**, celle de son compte GitHub :
   `git config user.name "Prénom Nom"` et `git config user.email "email-du-compte-github"`.
3. **Initialiser une seule fois** (une personne, puis commit et push) :
   ```bash
   ./outils/initialiser.sh mon-projet "MonProjet"                       # garde le module exemple pour l'étudier
   ./outils/initialiser.sh mon-projet "MonProjet" --supprimer-exemple   # ou le retire directement
   ./gradlew build
   ```
   Après ce commit sur `main`, créer la branche d'intégration `develop` : le modèle ne fournit que `main`.
   ```bash
   git switch -c develop && git push -u origin develop
   ```
4. Remplacer `ORG/REPO` et `SONAR_KEY` dans ce README, et renseigner `gradle.properties`.
5. Brancher les services (facultatif mais attendu pour les badges) : secrets `CODECOV_TOKEN` et `SONAR_TOKEN` dans *Settings → Secrets and variables → Actions*. Sans eux, la CI tourne quand même, sans ces deux étapes.
6. Activer GitHub Pages (*Settings → Pages → Source : GitHub Actions*), puis rédiger `doc/dossier.md` (`make -C doc`) et la page de projet `site/index.html`.
7. **Supprimer le module exemple avant la première feature**, si ce n'est pas déjà fait : la première migration de feature prendra le numéro `V2`.

## Architecture

Front → **API REST** → **Back** → **BD**

| Couche | Technologie |
|--------|-------------|
| Front | HTML/CSS/JavaScript natif, sans build (remplaçable : natif, CLI…) |
| API REST | Spring Boot 4 (Spring MVC), documentée par OpenAPI / Swagger UI |
| Back | Java 21, Spring Data JPA, Bean Validation, **architecture hexagonale** |
| BD | PostgreSQL 17, schéma versionné par Flyway (H2 pour les tests) |
| Qualité | JUnit 5, AssertJ, ArchUnit, JaCoCo, Codecov, SonarCloud, GitHub Actions |

```
src/main/java/fr/miage/projet/
├── api/                             endpoint technique /api/v1/info
├── config/                          configuration transverse (CORS)
└── <module>/                        un domaine fonctionnel = un module (ex. exemple/)
    ├── domain/                      Java pur : modèle, règles métier, algorithmes
    │   ├── model/   ├── algo/   └── exception/
    ├── application/                 cas d'utilisation, sans framework
    │   ├── port/in/                 interfaces appelées par les adaptateurs entrants
    │   ├── port/out/                interfaces implémentées par les adaptateurs sortants
    │   └── service/
    ├── adapter/
    │   ├── in/web/                  contrôleurs REST, DTO, erreurs problem+json
    │   └── out/persistence/         entités et dépôts JPA
    └── config/                      assemblage Spring de l'hexagone
src/main/resources/db/migration/     migrations Flyway V1__..., V2__...
src/main/resources/db/demo/          jeu de démonstration (profil demo)
front/                               client web, ne parle qu'à l'API
doc/                                 dossier (Markdown → PDF), fiches de features, araignée part IA
site/                                page de projet (GitHub Pages)
outils/initialiser.sh                renommage initial du projet
```

Les dépendances vont toujours vers le domaine : `ArchitectureHexagonaleTest` fait échouer le build sinon.

## Lancer

```bash
docker compose up -d                                              # PostgreSQL sur localhost:5432
./gradlew bootRun                                                 # ou, sans Docker :
./gradlew bootRun --args='--spring.profiles.active=h2,demo'       # H2 en mémoire + jeu de démonstration
python3 -m http.server 8000 -d front                              # front sur http://localhost:8000
```

- API : http://localhost:8080/api/v1/info
- Swagger UI : http://localhost:8080/swagger-ui.html
- Santé : http://localhost:8080/actuator/health

## Tester

```bash
./gradlew build          # compile, tests unitaires + intégration + architecture, couverture
open build/reports/jacoco/test/html/index.html
```

Conventions : `*Test` pour les tests unitaires (sans Spring), `*IT` pour les tests d'intégration.

## Dossier PDF

```bash
make -C doc              # pandoc + WeasyPrint (pip install weasyprint) ; mise en page dans doc/style.css
make -C doc documentation   # ce guide d'utilisation, à la racine
```

L'araignée « part IA / part humaine » est générée depuis `doc/part-ia.json` à chaque compilation.

## Livrer une version

Pousser un tag suffit : la CI construit l'application et la documentation, puis publie la release avec le jar et `dossier.pdf`.

```bash
git tag -a v0.1.0 -m "Livraison v0.1.0" && git push origin v0.1.0
```

## Features

| Version | Feature | Algorithmes | Fiche |
|---|---|---|---|
| v0.1.0 | à définir | | |

## Contribuer

Voir [CONTRIBUTING.md](CONTRIBUTING.md) et [CLAUDE.md](CLAUDE.md).

## Licence

[Apache 2.0](LICENSE)
