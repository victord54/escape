<div align="center">

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
// TODO

## Installation
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
> Replace X.Y.Z with the version number, e.g. `1.0.0`.
##### GUI mode
```bash
java -jar target/escape-X.Y.Z.jar
```

##### CLI mode
```bash
java -jar target/escape-X.Y.Z.jar --nowindow
```

## Docs
### Sprints
* [Sprint 0](docs/sprints/0)
* [Sprint 1](docs/sprints/1)

### Latest UML diagrams
* [Class diagram](docs/uml/class-diagram.svg)\
![Class diagram](docs/uml/class-diagram.svg)
* [Other diagrams](docs/uml)
