<div align="center">

<img src="./src/main/resources/fr/ul/acl/escape/assets/icon.png" alt="Escape icon" width="160" />

# Escape

![Java CI with Maven](https://github.com/victord54/escape/actions/workflows/maven.yml/badge.svg)
</div>

## Authors

* [Victor DALLÉ](https://github.com/victord54)
* [Claire KURTH](https://github.com/clairekth)
* [Dan DEMANGE](https://github.com/Hazvard)
* [Théo FAEDO](https://github.com/TheoFaedo)
* [Antoine CONTOUX](https://github.com/ActxLeToucan)

## Description

Escape est un jeu vidéo inspiré du genre d'Isaac, où l'objectif est de naviguer à travers des salles de
difficulté croissante afin d'aller le plus loin possible. Ces salles sont habitées par une variété de
monstres et de pièges qui vous en empêcheront.

Le jeu propose deux modes distincts : le mode campagne, considéré comme le mode "classique", génère aléatoirement
les différents niveaux, tandis que le mode personnalisé permet la création de cartes sur mesure grâce à l'édition
de fichiers JSON.

## Project management

* [Trello](https://trello.com/b/WUfGrD7d/escape)

## Installation

> [!IMPORTANT]
>
> Copy the file [`.env.dist`](.env.dist) to `.env` and fill in the values.

> [!NOTE]
>
> One way to easily generate a random secret key is to use the following command:
> ```bash
> openssl rand -hex 64
> ```

### Requirements

* Maven >= 3.4.1
* Java >= 17

### Development

#### Run

```bash
mvn clean javafx:run
```

#### Test

```bash
mvn clean test
```

### Production

#### Build

```bash
mvn clean package
```

#### Run

> [!NOTE]
> Replace X.Y.Z with the version number, e.g. `1.0.0`.

##### GUI mode

```bash
java -jar target/escape-X.Y.Z.jar
```

##### CLI mode

```bash
java -jar target/escape-X.Y.Z.jar --nowindow
```

##### Debug

To see debug messages, add the `--debug` option.

```bash
java -jar target/escape-X.Y.Z.jar --debug [other options]
```

## Docs

### Sprints

* [Sprint 0](docs/sprints/0)
* [Sprint 1](docs/sprints/1)
* [Sprint 2](docs/sprints/2)
* [Sprint 3](docs/sprints/3)

### Latest UML diagrams

* [Class diagram](docs/uml/class-diagram.svg)\
  ![Class diagram](docs/uml/class-diagram.svg)
* [Other diagrams](docs/uml)
