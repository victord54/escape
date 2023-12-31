package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.GameMode;
import fr.ul.acl.escape.LevelData;
import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Monstre;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.environment.BordureMonde;
import fr.ul.acl.escape.monde.environment.Terrain;
import fr.ul.acl.escape.monde.objects.Coeur;
import fr.ul.acl.escape.monde.objects.Objet;
import fr.ul.acl.escape.monde.objects.Trappe;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.FileManager;
import fr.ul.acl.escape.outils.ProceduralGenerator;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fr.ul.acl.escape.outils.FileManager.FileType.JSON;
import static fr.ul.acl.escape.outils.ProceduralGenerator.genererSeed;
import static java.io.File.separator;


public class Monde {
    private int height;
    private int width;
    private ArrayList<Personnage> personnages;
    private ArrayList<Terrain> terrains;
    private ArrayList<Objet> objets;
    /**
     * Last map loaded
     */
    private String carte;
    /**
     * Game mode of the last map loaded
     */
    private GameMode gameMode;

    /**
     * Last procedural levels properties
     */
    private long currentLevelSeed;
    private int currentLevelDifficulty;

    /**
     * Create a new world with no elements.
     */
    public Monde(int height, int width, long seed, int difficulty) {
        this.height = height;
        this.width = width;
        personnages = new ArrayList<>();
        terrains = new ArrayList<>();
        objets = new ArrayList<>();
        gameMode = GameMode.CAMPAIGN;

        this.currentLevelSeed = seed;
        this.currentLevelDifficulty = difficulty;
    }

    /**
     * Create a Monde from a map
     *
     * @param map  the map to load
     * @param mode the game mode
     * @return the Monde
     * @throws Exception if the map cannot be loaded
     */
    public static Monde fromMap(String map, GameMode mode) throws Exception {
        JSONObject json = mode == GameMode.CAMPAIGN ? FileManager.readResourceFile("maps/" + map) // campaign: load from resources
                : FileManager.readFile(LevelData.FOLDER + separator + map, false); // custom: load from custom maps folder
        if (json == null) throw new Exception("Map not found");

        Monde monde = fromJSON(json);
        monde.carte = map;
        monde.gameMode = mode;

        return monde;
    }

    /**
     * Create a Monde from a JSON representation
     *
     * @param json the JSON representation
     * @return the Monde
     * @throws Exception if the JSON is invalid
     */
    public static Monde fromJSON(JSONObject json) throws Exception {
        Monde monde;
        if (json.has("mode")) {
            // it's a save, it doesn't contain environment
            GameMode mode = GameMode.valueOf(json.getString("mode"));
            if (mode == GameMode.CAMPAIGN) {
                //it's a campaign save, it has a seed and a difficulty
                ProceduralGenerator generator = new ProceduralGenerator(json.getLong("seed"), json.getInt("difficulty"));
                monde = generator.getMonde();
            } else {
                //it's a custom save, it has a map
                monde = fromMap(json.getString("map"), mode);
            }
            monde.personnages.clear();
            monde.objets.clear();
        } else {
            // it's a map file
            JSONObject jsonWorld = json.getJSONObject("world");
            monde = new Monde(jsonWorld.getInt("height"), jsonWorld.getInt("width"), 0, 1);
            if (monde.width < 3 || monde.height < 3)
                throw new IllegalArgumentException("World too small: " + jsonWorld);

            // add border
            for (int i = 0; i < monde.width; i++) {
                monde.terrains.add(new BordureMonde(i, 0));
                monde.terrains.add(new BordureMonde(i, monde.height - 1));
            }
            for (int i = 0; i < monde.height; i++) {
                monde.terrains.add(new BordureMonde(0, i));
                monde.terrains.add(new BordureMonde(monde.width - 1, i));
            }
            // add terrains
            json.getJSONArray("environment").forEach(jsonTerrain -> {
                Terrain terrain = Terrain.fromJSON((JSONObject) jsonTerrain);
                validateElement(terrain, monde, false);
                monde.terrains.add(terrain);
            });
        }

        // add entities
        json.getJSONArray("entities").forEach(jsonEntity -> {
            Personnage entity = Personnage.fromJSON((JSONObject) jsonEntity);
            validateElement(entity, monde, true);
            monde.personnages.add(entity);
        });

        //add objects
        json.getJSONArray("objects").forEach(jsonObject -> {
            Objet objet = Objet.fromJSON((JSONObject) jsonObject);
            validateElement(objet, monde, false);
            monde.objets.add(objet);
        });

        // validate world
        if (monde.personnages.isEmpty()) throw new IllegalArgumentException("No entities in the world");
        if (monde.personnages.stream().filter(Personnage::estUnHeros).count() != 1)
            throw new IllegalArgumentException("There must be exactly one hero in the world");

        return monde;
    }

    /**
     * Function that check if an ElementMonde is valid.
     *
     * @param element      The ElementMonde to be checked.
     * @param monde        The Monde.
     * @param isPersonnage true if the ElementMonde is a Personnage, false otherwise.
     * @throws IllegalArgumentException if the ElementMonde is not valid.
     */
    private static void validateElement(ElementMonde element, Monde monde, boolean isPersonnage) throws IllegalArgumentException {
        if (element == null) throw new IllegalArgumentException("Element is null");
        // check if the position is valid
        if (element.getX() < 1 || element.getX() > monde.width - 1 || element.getY() < 1 || element.getY() > monde.height - 1)
            throw new IllegalArgumentException("Element out of bounds: " + element);
        // check if the size is valid
        if (element.getLargeur() <= 0 || element.getHauteur() <= 0)
            throw new IllegalArgumentException("Element too small: " + element);
        if (element.getX() + element.getLargeur() > monde.width - 1 || element.getY() + element.getHauteur() > monde.height - 1)
            throw new IllegalArgumentException("Element too big: " + element);
        // check if the element is in collision with another element
        if (isPersonnage) {
            if (monde.collisionAvec((Personnage) element, true))
                throw new IllegalArgumentException("Element in collision with another element: " + element);
        } else {
            if (monde.collisionAvecTerrains(element))
                throw new IllegalArgumentException("Element in collision with a terrain: " + element);
        }
    }

    /**
     * Function that check if the Heros can be moved and that moved it in the right direction if there is no collision.
     *
     * @param typeMouvement The Type of mouvement the Heros wants to make.
     */
    public void deplacementHeros(TypeMouvement typeMouvement, double deltaTime) {
        Heros h = getHeros();
        Heros tmp = h.clone();
        tmp.deplacer(typeMouvement, deltaTime);

        if (!collisionAvec(tmp, false)) h.deplacer(typeMouvement, deltaTime);
        else h.setOrientation(typeMouvement); // Pour pouvoir se tourner même quand on ne peut pas se déplacer

        herosDeclencheObjet();
    }

    /**
     * Method that move all the Monstre of the world.
     */
    public void deplacementMonstres(double deltaTime) {
        for (Personnage p : personnages) {
            if (!p.estUnHeros()) {
                deplacementMonstre((Monstre) p, deltaTime);
            }
        }
    }

    /**
     * Method that create a graph and then move a Monstre with a pathfinding.
     *
     * @param monstre The Monstre that we want to move.
     */
    protected void deplacementMonstre(Monstre monstre, double deltaTime) {
        monstre.setMoving(false);
        Graph<Point2D, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int pas = 5; // Incrémentation pour construire les noeuds
        int conversionFactor = Donnees.CONVERSION_FACTOR; // Facteur de conversion pour convertir les double en int
        for (int i = 0; i < this.width * conversionFactor; i += pas) {
            for (int j = 0; j < this.height * conversionFactor; j += pas) {
                Point2D courant = new Point2D(i, j);
                Point2D droite = new Point2D(i + pas, j);
                Point2D bas = new Point2D(i, j + pas);
                graph.addVertex(courant);

                // Personnage tmp pour tester si le noeud est atteignable (pas sur un terrain ou un monstre)
                Monstre tmpMonstre = (Monstre) monstre.clone();
                tmpMonstre.setX((double) i / conversionFactor);
                tmpMonstre.setY((double) j / conversionFactor);

                // Test si noeud à droite est atteignable (pas en dehors du monde et pas dans un terrain ou un personnage), si oui ajout du noeud au graphe et création d'un arc
                if (i + pas  < this.width * conversionFactor && !collisionAvec(tmpMonstre, false)) {
                    graph.addVertex(droite);
                    graph.addEdge(courant, droite);
                }

                // Test si noeud à gauche est atteignable (pas en dehors du monde et pas dans un terrain ou un personnage), si oui ajout du noeud au graphe et création d'un arc
                if (j + pas < this.height * conversionFactor && !collisionAvec(tmpMonstre, false)) {
                    graph.addVertex(bas);
                    graph.addEdge(courant, bas);
                }
            }
        }

        // Check si le noeud source n'est pas en dehors en haut ou à gauche
        int sourceX = intLePlusProche((int) (monstre.getX() * conversionFactor), pas);
        int sourceY = intLePlusProche((int) (monstre.getY() * conversionFactor), pas);
        Point2D source = new Point2D(sourceX, sourceY);

        Point2D heros = new Point2D(intLePlusProche((int) (getHeros().getX() * conversionFactor), pas), intLePlusProche((int) (getHeros().getY() * conversionFactor), pas));

        Monstre tmpMontreAPorteHeros = (Monstre) monstre.clone();
        tmpMontreAPorteHeros.setX(monstre.getX() - 0.2);
        tmpMontreAPorteHeros.setY(monstre.getY() - 0.2);
        tmpMontreAPorteHeros.setHauteur(monstre.getHauteur() + 0.4);
        tmpMontreAPorteHeros.setLargeur(monstre.getLargeur() + 0.4);
        if (collision(getHeros(), tmpMontreAPorteHeros)) return;

        pathfinding(monstre, pas, graph, source, heros, deltaTime);
    }

    /**
     * Method that check if there is collision between an ElementMonde and a Terrain.
     *
     * @param element The ElementMonde.
     * @return true if there is a collision, false otherwise.
     */
    public boolean collisionAvecTerrains(ElementMonde element) {
        for (Terrain t : terrains) {
            if (!t.estTraversable()) {
                if (collision(element, t)) return true;
            }
        }
        return false;
    }

    /**
     * Function that return if there is a collision between two element of the world.
     *
     * @param e1 The first element.
     * @param e2 The second element.
     * @return true if there is a collision between e1 and e2, false otherwise.
     */
    protected boolean collision(ElementMonde e1, ElementMonde e2) {
        // e1 gauche de e2 -> pt droit de e1 < pt gauche e2
        // e1 en dessous de e2 -> pt haut de e1 -> > pt bas e2
        // e1 à droite de e2 -> pt gauche de e1 > pt droit e2
        // e1 au dessus de e2 -> pt bas de e < pt haut e2

        return ((e1.getX() < e2.getX() + e2.getLargeur()) && (e1.getY() < e2.getY() + e2.getHauteur()) && (e1.getX() + e1.getLargeur() > e2.getX()) && ((e1.getY() + e1.getHauteur() > e2.getY())));
    }

    /**
     * Function that check if a personnage is on collision with an element of the world.
     *
     * @param pers           The Personnage which we want to check if he is on collision.
     * @param checkAvecHeros True if check with the Hero too, false otherwise.
     * @return true if collision, false otherwise.
     */
    protected boolean collisionAvec(Personnage pers, boolean checkAvecHeros) {
        for (Terrain t : terrains) {
            if (!t.estTraversable() && !pers.peutTraverserObstacles()) {
                if (collision(pers, t)) return true;
            } else if (t.estTraversable() && pers.peutTraverserObstacles()) {
                if (collision(pers, t)) return true;
            }
        }
        for (Personnage p : personnages) {
            if (checkAvecHeros) {
                if (pers.getId() != p.getId()) {
                    if (collision(pers, p)) {
                        return true;
                    }
                }
            } else if (pers.getId() != p.getId() && !p.estUnHeros()) if (collision(pers, p)) return true;

        }
        return false;
    }

    /**
     * Pathfinding algorithm for a Monstre.
     *
     * @param monstre   The Monstre that we want to move.
     * @param pas       The increment for the construction of the alternate graph.
     * @param graph     The graph.
     * @param source    The node source.
     * @param heros     The node target.
     * @param deltaTime The time difference since the last iteration.
     */
    private void pathfinding(Monstre monstre, int pas, Graph<Point2D, DefaultEdge> graph, Point2D source, Point2D heros, double deltaTime) {
        DijkstraShortestPath<Point2D, DefaultEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Point2D, DefaultEdge> shortest = shortestPath.getPath(source, heros);
        monstre.reinitialiseListMouvementsEssayes();

        boolean random = false;

        if (shortest == null) {
            // Pas de chemins vers le héros = trop de monstres directement autour de lui. Génération d'un graphe qui ne prend pas en compte les monstres
            // dans ce cas afin que les monstres se dirigent quand même vers le héros.
            Graph<Point2D, DefaultEdge> graphAlternatif = this.grapheAlternatif(monstre, pas);
            shortestPath = new DijkstraShortestPath<>(graphAlternatif);
            shortest = shortestPath.getPath(source, heros);

            if (shortest == null) random = true;
        }

        // déplacement random car pas de chemin = monstre bloqué
        if (random) {
            this.mouvementRandom(monstre, deltaTime);
            return;
        }
        List<Point2D> list = shortest.getVertexList();

        if (list.size() == 1) return;
        Point2D first = list.get(1);
        TypeMouvement typeMouvement = getMouvement(source, first, monstre);
        if (typeMouvement == null) return;

        Monstre tmpMonstre = (Monstre) monstre.clone();
        tmpMonstre.deplacer(typeMouvement, deltaTime);

        // On fait le mouvement prévu s'il est réalisable
        if (!collisionAvec(tmpMonstre, true)) {
            monstre.deplacer(typeMouvement, deltaTime);
            monstre.setMoving(true);
            return;
        }

        // le mouvement n'a pas pu être effectué donc on essaye le dernier qui avait réussi
        tmpMonstre = (Monstre) monstre.clone();
        tmpMonstre.deplacer(monstre.getOrientation(), deltaTime);
        if (!collisionAvec(tmpMonstre, true)) {
            monstre.deplacer(monstre.getOrientation(), deltaTime);
            monstre.setMoving(true);
            return;
        }

        // Le dernier mouvement n'était pas possible non plus donc on regarde si c'était à cause du héros ou non.
        // Si non, on regarde dans la liste des noeuds du chemin quel prochain mouvement est faisable
        // On garde en mémoire tous les mouvements que le Monstre a essayés.
        if (collisionAvec(tmpMonstre, false)) {
            monstre.addMouvementEssayes(typeMouvement);
            for (int i = 2; i < list.size(); i++) {
                typeMouvement = getMouvement(source, list.get(i), monstre);
                if (typeMouvement == null) continue;

                // Le mouvement n'est pas dans la liste donc on peut l'essayer
                if (!monstre.mouvementDansList(typeMouvement)) {

                    tmpMonstre = (Monstre) monstre.clone();
                    tmpMonstre.deplacer(typeMouvement, deltaTime);

                    if (!collisionAvec(tmpMonstre, true)) {
                        monstre.deplacer(typeMouvement, deltaTime);
                        monstre.setMoving(true);
                        return;
                    }
                    monstre.addMouvementEssayes(typeMouvement);
                }

            }
        }

        // Si vraiment aucun mouvement n'a été fait, mouvement random
        this.mouvementRandom(monstre, deltaTime);
    }

    /**
     * Method that create an alternative graph for the Monstre with node that can be in a Monstre.
     *
     * @param monstre The Monstre we want to move.
     * @param pas     The increment for the construction of the graph.
     * @return A Graph<Point2, DefaultEdge> for m.
     **/
    private Graph<Point2D, DefaultEdge> grapheAlternatif(Monstre monstre, int pas) {
        Graph<Point2D, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int conversionFactor = Donnees.CONVERSION_FACTOR;
        for (int i = 0; i < this.width * conversionFactor; i += pas) {
            for (int j = 0; j < this.height * conversionFactor; j += pas) {
                Point2D courant = new Point2D(i, j);
                Point2D droite = new Point2D(i + pas, j);
                Point2D bas = new Point2D(i, j + pas);
                graph.addVertex(courant);

                Monstre tmpMonstre = (Monstre) monstre.clone();
                tmpMonstre.setX((double) i / conversionFactor);
                tmpMonstre.setY((double) j / conversionFactor);
                // On ne teste pas si le noeud est sur un Personnage
                if (i + pas  < this.width * conversionFactor && (monstre.peutTraverserObstacles() || !collisionAvecTerrains(tmpMonstre))) {
                    graph.addVertex(droite);
                    graph.addEdge(courant, droite);
                }

                if (j + pas  < this.height * conversionFactor && (monstre.peutTraverserObstacles() || !collisionAvecTerrains(tmpMonstre))) {
                    graph.addVertex(bas);
                    graph.addEdge(courant, bas);
                }
            }
        }

        return graph;
    }

    /**
     * Method that move a Monstre randomly.
     *
     * @param monstre   The Monstre that we want to move.
     * @param deltaTime The time difference since the last iteration.
     */
    private void mouvementRandom(Monstre monstre, double deltaTime) {
        // Liste des mouvements possibles
        ArrayList<TypeMouvement> list = new ArrayList<>();
        list.add(TypeMouvement.UP);
        list.add(TypeMouvement.DOWN);
        list.add(TypeMouvement.LEFT);
        list.add(TypeMouvement.RIGHT);

        Random r = new Random();
        int randomNb;
        ArrayList<TypeMouvement> mvtEssaye = new ArrayList<>();
        while (true) {
            randomNb = r.nextInt(4);
            TypeMouvement typeMouvement = list.get(randomNb);
            if (mvtEssaye.contains(typeMouvement)) continue;

            // Le mouvement n'a pas encore été essayé
            mvtEssaye.add(typeMouvement);
            Monstre tmpMonstre = (Monstre) monstre.clone();
            tmpMonstre.deplacer(typeMouvement, deltaTime);

            if (!collisionAvec(tmpMonstre, true)) {
                monstre.deplacer(typeMouvement, deltaTime);
                monstre.setMoving(true);
                return;
            }
        }
    }

    /**
     * Method that return the TypeMouvement that the Monstre need to do to go from source to target.
     *
     * @param source The node source.
     * @param target The node target.
     * @param m      The Monstre.
     * @return The TypeMouvement that the Monstre need to do to go from source to target.
     */
    private TypeMouvement getMouvement(Point2D source, Point2D target, Monstre m) {
        if (target.getX() < source.getX() && !m.mouvementDansList(TypeMouvement.LEFT)) return TypeMouvement.LEFT;
        else if ((target.getX()) > source.getX() && !m.mouvementDansList(TypeMouvement.RIGHT))
            return TypeMouvement.RIGHT;
        else if ((target.getY()) > source.getY() && !m.mouvementDansList(TypeMouvement.DOWN)) return TypeMouvement.DOWN;
        else if ((target.getY()) < source.getY() && !m.mouvementDansList(TypeMouvement.UP)) return TypeMouvement.UP;

        return null;
    }

    /**
     * Initiates an attack action for the hero.
     * Checks for a cooldown period and performs an attack if the cooldown has elapsed.
     * Detects and targets enemies within the hero's attack hitbox, inflicts damage, and
     * removes defeated enemies.
     */
    public void heroAttaque(long currentTimeNS) {
        List<Personnage> monstresDansHitBoxAttaque = new ArrayList<>();
        Heros hero = getHeros();
        for (Personnage p : personnages) {
            if (hero.getHitBoxAttaque().intersects(p.getHitBoxCollision()) && !p.estUnHeros()) {
                monstresDansHitBoxAttaque.add(p);
            }
        }
        hero.attaquer(monstresDansHitBoxAttaque, currentTimeNS);
        for (Personnage p : monstresDansHitBoxAttaque) {
            if (!p.estVivant()) {
                detruirePersonnage(p);
            }
        }

        if (monstresTousMorts()) {
            for (Objet o : objets) {
                if (o.estTrappe()) {
                    ((Trappe) o).ouvrir();
                }
            }
        }
    }

    /**
     * Initiates an attack action for monsters.
     * Checks for a cooldown period and the hero's vitality before performing an attack.
     * Detects and targets the hero within the monsters' attack hitboxes, inflicts damage,
     * and updates the hero's health.
     */
    public void monstreAttaque(long currentTimeNS) {
        if (!getHeros().estVivant()) return;

        Heros hero = getHeros();
        for (Personnage p : personnages) {
            if (p.getHitBoxAttaque().intersects(hero.getHitBoxCollision()) && !p.estUnHeros()) {
                p.attaquer(List.of(hero), currentTimeNS);
            }
        }
    }

    /**
     * Method that check if the Hero is on collision with an Objet. If he is, the object is picked up.
     */
    public void heroRamassageObjet() {
        Heros h = this.getHeros();
        Objet objetRamasse = null;
        for (Objet o : objets) {
            if (o.estRamassable()) {
                if (collision(h, o)) {
                    objetRamasse = o;
                    break;
                }
            }

        }
        if (objetRamasse == null) return;
        if (objetRamasse.estConsommable()) {
            objetRamasse.consommePar(h, this);
            objets.remove(objetRamasse);
        }
    }

    /**
     * Method that check if the Hero is on collision with an Objet that can be triggered. If he is, the object is triggered.
     */
    private void herosDeclencheObjet() {
        Heros h = this.getHeros();
        for (Objet o : objets) {
            if (o.estDeclenchable()) {
                if (collision(h, o)) {
                    o.consommePar(h, this);
                }
            }
        }
    }

    public void activationObjetAvecDuree(long currentTimeNS) {
        Heros h = this.getHeros();
        for (Objet o : objets) {
            if (o.necessiteDureePourActivation()) {
                if (collision(h, o)) {
                    o.onObject(h, currentTimeNS);
                } else {
                    o.notOnObject(h);
                }
            }
        }
    }

    public boolean heroStillAlive() {
        return getHeros().estVivant();
    }

    /**
     * Erases the specified character from the world
     *
     * @param p The character to be destroyed.
     */
    private void detruirePersonnage(Personnage p) {
        this.personnages.remove(p);
        double random = Math.random();
        if (random < Donnees.CHANCE_OF_HEART_DROP) {
            this.objets.add(new Coeur(p.getX(), p.getY(), Donnees.HEART_HEIGHT, Donnees.HEART_WIDTH, Donnees.HEART_VALUE));
        }
    }

    /**
     * Function that add a Terrain to the ArrayList of Terrain.
     *
     * @param t The Terrain to be added.
     */
    public void addTerrains(Terrain t) {
        this.terrains.add(t);
    }

    /**
     * Function that add a Personnage to the ArrayList of Personnage.
     *
     * @param p The Personnage to be added.
     */
    public void addPersonnage(Personnage p) {
        this.personnages.add(p);
    }

    /**
     * Function that add an Objet to the ArrayList of Objet.
     *
     * @param o The Objet to be added.
     */
    public void addObjet(Objet o) {
        this.objets.add(o);
    }

    /**
     * Function that get the Heros.
     *
     * @return the Heros.
     */
    public Heros getHeros() {
        for (Personnage p : personnages) {
            if (p.estUnHeros()) return (Heros) p;
        }
        return null;
    }

    /**
     * Function that get all the Personnage of the world.
     *
     * @return ArrayList of Personnage.
     */
    public ArrayList<Personnage> getPersonnages() {
        return personnages;
    }

    /**
     * Function that get all the Terrain of the world.
     *
     * @return ArrayList of Terrain.
     */
    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    /**
     * Function that get all the Objet of the world.
     *
     * @return ArrayList of Objet.
     */
    public ArrayList<Objet> getObjets() {
        return objets;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getCurrentLevelDifficulty(){
        return currentLevelDifficulty;
    }

    /**
     * Method that returns the nearest integer greater than value, which is a multiple of multiple
     *
     * @param value    the value.
     * @param multiple the multiple.
     * @return nearest integer grater than value and which is a multiple of multiple.
     */
    protected int intLePlusProche(int value, int multiple) {
        if ((value % multiple) == 0) return value;
        return (value / multiple + 1) * multiple;
    }

    /**
     * @return a JSON representation of the world
     */
    public JSONObject toJSONSave() {
        JSONObject json = new JSONObject();
        if (gameMode == GameMode.CAMPAIGN) {
            json.put("seed", currentLevelSeed);
            json.put("difficulty", currentLevelDifficulty);
            json.put("level", currentLevelDifficulty);
        } else {
            json.put("map", carte);
        }
        json.put("mode", gameMode.toString());
        json.put("entities", personnages.stream().map(Personnage::toJSON).toArray());
        json.put("objects", objets.stream().map(Objet::toJSON).toArray());
        return json;
    }

    /**
     * Copies the contents of another world (Monde) to this world, preserving hero statistics.
     * <p>
     * This method copies the map, dimensions, objects, characters, and terrains
     * from the specified world to the current world. It also preserves the statistics
     * of the hero before the copy operation.
     * </p>
     *
     * @param m The world (Monde) to be copied.
     * @see Monde
     * @see Heros
     */
    public void copierMonde(Monde m) {
        Heros ancienHero = getHeros();

        carte = m.carte;
        currentLevelSeed = m.currentLevelSeed;
        currentLevelDifficulty = m.currentLevelDifficulty;
        height = m.height;
        width = m.width;
        objets = m.objets;
        personnages = m.personnages;
        terrains = m.terrains;

        getHeros().copierStatistique(ancienHero);
    }

    /**
     * Changes the current map of the world based on the specified map name.
     * <p>
     * This method loads a new map from the provided map name and replaces the current map in the world.
     * </p>
     *
     * @param nomMap The name of the new map to be loaded.
     * @throws RuntimeException If an exception occurs during the map loading process.
     * @see Monde
     * @see Heros
     */
    public void changerMap(String nomMap) {
        if (gameMode == GameMode.CAMPAIGN) {
            //Si on est en mode campagne alors on génère une nouvelle carte avec une difficultée augmentée de 1.
            ProceduralGenerator generator = new ProceduralGenerator(genererSeed(), currentLevelDifficulty + 1);
            Monde nouveauMonde = generator.getMonde();
            copierMonde(nouveauMonde);
        } else {
            //Si on est en mode custom alors on charge la carte dont le nom est donné en paramètre
            if (nomMap == null || nomMap.isEmpty()) return;
            try {
                Monde nouveauMonde = fromMap(nomMap + JSON.extension, this.gameMode);
                copierMonde(nouveauMonde);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks if all monsters in the world are defeated.
     * <p>
     * This method iterates through all characters in the world and returns true if
     * all characters are heroes, indicating that there are no remaining monsters.
     * </p>
     *
     * @return {@code true} if all monsters in the world are defeated, {@code false} otherwise.
     * @see Personnage
     */
    public boolean monstresTousMorts() {
        for (Personnage p : personnages) if (!p.estUnHeros()) return false;
        return true;
    }


    /**
     * Method that check if the Personnage of the game are in water and act in consequence.
     */
    public void verificationTerrainsSpeciaux() {
        for (Personnage p : personnages) {
            p.vitesseNormale();
            boolean piedGaucheDansEau = false;
            boolean piedDroitDansEau = false;
            for (Terrain t : terrains) {
                if (t.estTerrainSpecial()) {
                    if (!piedGaucheDansEau) piedGaucheDansEau = gaucheDansEau(t, p);
                    if (!piedDroitDansEau) piedDroitDansEau = droitDansEau(t, p);
                    if (piedGaucheDansEau && piedDroitDansEau) {
                        t.appliqueActionSpeciale(p);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Method that check if the left foot (left bottom point) of the Personnage is in a Terrain.
     *
     * @param terrain The terrain.
     * @param p       The Personnage.
     * @return true if terrain contains the left bottom point of the Personnage.
     */
    public boolean gaucheDansEau(Terrain terrain, Personnage p) {
        Rectangle2D rect = new Rectangle2D(terrain.getX(), terrain.getY(), terrain.getLargeur(), terrain.getHauteur());

        double leftBottomX = p.getX() + 0.3;
        double leftBottomY = p.getY() + p.getHauteur();
        Point2D piedGauche = new Point2D(leftBottomX, leftBottomY);

        return (rect.contains(piedGauche));
    }

    /**
     * Method that check if the right foot (right bottom point) of the Personnage is in a Terrain.
     *
     * @param terrain The terrain.
     * @param p       The Personnage.
     * @return true if terrain contains the right bottom point of the Personnage.
     */
    public boolean droitDansEau(Terrain terrain, Personnage p) {
        Rectangle2D rect = new Rectangle2D(terrain.getX(), terrain.getY(), terrain.getLargeur(), terrain.getHauteur());

        double rightBottomX = p.getX() + p.getLargeur() - 0.3;
        double rightBottomY = p.getY() + p.getHauteur();
        Point2D piedDroit = new Point2D(rightBottomX, rightBottomY);

        return (rect.contains(piedDroit));
    }
}
