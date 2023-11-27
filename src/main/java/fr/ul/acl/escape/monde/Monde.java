package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Monstre;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.environment.BordureMonde;
import fr.ul.acl.escape.monde.environment.Terrain;
import fr.ul.acl.escape.monde.objects.Coeur;
import fr.ul.acl.escape.monde.objects.Objet;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.FileManager;
import javafx.geometry.Point2D;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fr.ul.acl.escape.outils.Donnees.HERO_HIT_COUNTDOWN;


public class Monde {
    private final int height;
    private final int width;
    private final ArrayList<Personnage> personnages;
    private final ArrayList<Terrain> terrains;
    private final ArrayList<Objet> objets;
    /**
     * Last map loaded
     */
    private String carte;

    private long dernierCoupsEffectueParHero = System.currentTimeMillis();

    /**
     * Create a new world with no elements.
     */
    public Monde(int height, int width) {
        this.height = height;
        this.width = width;
        personnages = new ArrayList<>();
        terrains = new ArrayList<>();
        objets = new ArrayList<>();
    }

    /**
     * Create a Monde from a map
     *
     * @param map the map to load
     * @return the Monde
     * @throws Exception if the map cannot be loaded
     */
    public static Monde fromMap(String map) throws Exception {
        JSONObject json = FileManager.readResourceFile("maps/" + map);
        if (json == null) throw new Exception("Map not found");
        return fromJSON(json, map);
    }

    /**
     * Create a Monde from a JSON representation
     *
     * @param json the JSON representation
     * @return the Monde
     * @throws Exception if the JSON is invalid
     */
    public static Monde fromJSON(JSONObject json) throws Exception {
        return fromJSON(json, null);
    }

    /**
     * Create a Monde from a JSON representation
     *
     * @param json the JSON representation
     * @param map  the map name
     * @return the Monde
     * @throws Exception if the JSON is invalid
     */
    public static Monde fromJSON(JSONObject json, String map) throws Exception {
        Monde monde;
        if (json.has("map")) {
            // it's a save, it doesn't contain environment
            monde = fromMap(json.getString("map"));
            monde.personnages.clear();
            monde.objets.clear();
        } else {
            // it's a map file
            JSONObject jsonWorld = json.getJSONObject("world");
            monde = new Monde(jsonWorld.getInt("height"), jsonWorld.getInt("width"));
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
        if (map != null) monde.carte = map;

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
        if (element.getX() < 1 || element.getX() > monde.width - 2 || element.getY() < 1 || element.getY() > monde.height - 2)
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
     * Function that check if the Heros can be deplaced and deplaced it in the right direction if there is no collision.
     *
     * @param typeMouvement The Type of mouvement the Heros wants to make.
     */
    public void deplacementHeros(TypeMouvement typeMouvement, double deltaTime) {
        Heros h = getHeros();
        Heros tmp = h.clone();
        tmp.deplacer(typeMouvement, deltaTime);

        if (!collisionAvec(tmp, false)) h.deplacer(typeMouvement, deltaTime);
        else h.setOrientation(typeMouvement); // Pour pouvoir se tourner même quand on ne peut pas se déplacer

        herosDeclenchePiege();
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

    /**
     * Function that check if a personnage is on collision with an element of the world.
     *
     * @param pers           The Personnage which we want to check if he is on collision.
     * @param checkAvecHeros True if check with the Hero too, false otherwise.
     * @return true if collision, false otherwise.
     */
    public boolean collisionAvec(Personnage pers, boolean checkAvecHeros) {
        if (collisionAvecTerrains(pers)) return true;
        for (Personnage p : personnages) {
            if (checkAvecHeros) {
                if (pers.getId() != p.getId() && collision(pers, p)) {
                    return true;
                }
            } else if (pers.getId() != p.getId() && !p.estUnHeros() && collision(pers, p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that returns the nearest integer greater than value, which is a multiple of multiple
     *
     * @param value    the value.
     * @param multiple the multiple.
     * @return nearest integer grater than value and which is a multiple of multiple.
     */
    public int intLePlusProche(int value, int multiple) {
        if ((value % multiple) == 0) return value;
        return (value / multiple + 1) * multiple;
    }

    /**
     * Method that create a graph and then move a Monstre with a pathfinding.
     *
     * @param monstre The Monstre that we want to move.
     */
    public void deplacementMonstre(Monstre monstre, double deltaTime) {
        monstre.setMoving(false);
        Graph<Point2D, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int pas = 2500; // Incrémentation pour construire les noeuds
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
                if (i + pas + ((int) ((monstre.getLargeur() - 0.1) * conversionFactor)) < this.width * conversionFactor && !collisionAvec(tmpMonstre, false)) {
                    graph.addVertex(droite);
                    graph.addEdge(courant, droite);
                }

                // Test si noeud à gauche est atteignable (pas en dehors du monde et pas dans un terrain ou un personnage), si oui ajout du noeud au graphe et création d'un arc
                if (j + pas + ((int) ((monstre.getHauteur() - 0.1) * conversionFactor)) < this.height * conversionFactor && !collisionAvec(tmpMonstre, false)) {
                    graph.addVertex(bas);
                    graph.addEdge(courant, bas);
                }
            }
        }

        // Check si le noeud source est pas en dehors en haut ou à gauche
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
     * Pathfinding algorithm for a Monstre.
     *
     * @param monstre   The Monstre that we want to move.
     * @param pas       The increment for the construction of the alternate graph.
     * @param graph     The graph.
     * @param source    The node source.
     * @param heros     The node target.
     * @param deltaTime The time difference since the last iteration.
     */
    public void pathfinding(Monstre monstre, int pas, Graph<Point2D, DefaultEdge> graph, Point2D source, Point2D heros, double deltaTime) {
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

        // On fait le mouvement prévu si il est réalisable
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
        // On garde en mémoire tous les mouvements que le Monstre a essayé.
        if (collisionAvec(tmpMonstre, false)) {
            monstre.addMouvementEssayes(typeMouvement);
            for (int i = 2; i < list.size(); i++) {
                typeMouvement = getMouvement(source, list.get(i), monstre);
                if (typeMouvement == null) continue;

                // Le mouvement est pas dans la liste donc on peut l'essayer
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
     * Method that return the TypeMouvement that the Monstre need to do to go from source to target.
     *
     * @param source The node source.
     * @param target The node target.
     * @param m      The Monstre.
     * @return The TypeMouvement that the Monstre need to do to go from source to target.
     */
    public TypeMouvement getMouvement(Point2D source, Point2D target, Monstre m) {
        if (target.getX() < source.getX() && !m.mouvementDansList(TypeMouvement.LEFT)) return TypeMouvement.LEFT;
        else if ((target.getX()) > source.getX() && !m.mouvementDansList(TypeMouvement.RIGHT))
            return TypeMouvement.RIGHT;
        else if ((target.getY()) > source.getY() && !m.mouvementDansList(TypeMouvement.DOWN)) return TypeMouvement.DOWN;
        else if ((target.getY()) < source.getY() && !m.mouvementDansList(TypeMouvement.UP)) return TypeMouvement.UP;

        return null;
    }

    /**
     * Method that move a Monstre randomly.
     *
     * @param monstre   The Monstre that we want to move.
     * @param deltaTime The time difference since the last iteration.
     */
    public void mouvementRandom(Monstre monstre, double deltaTime) {
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
     * Method that create an alternative graph for the Monstre with node that can be in a Monstre.
     *
     * @param monstre The Monstre we want to move.
     * @param pas     The increment for the construction of the graph.
     * @return A Graph<Point2, DefaultEdge> for m.
     **/
    public Graph<Point2D, DefaultEdge> grapheAlternatif(Monstre monstre, int pas) {
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
                if (i + pas + ((int) ((monstre.getLargeur() - 0.1) * conversionFactor)) < this.width * conversionFactor && !collisionAvecTerrains(tmpMonstre)) {
                    graph.addVertex(droite);
                    graph.addEdge(courant, droite);
                }

                if (j + pas + ((int) ((monstre.getLargeur() - 0.1) * conversionFactor)) < this.height * conversionFactor && !collisionAvecTerrains(tmpMonstre)) {
                    graph.addVertex(bas);
                    graph.addEdge(courant, bas);
                }
            }
        }

        return graph;
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
     * Initiates an attack action for the hero.
     * Checks for a cooldown period and performs an attack if the cooldown has elapsed.
     * Detects and targets enemies within the hero's attack hitbox, inflicts damage, and
     * removes defeated enemies.
     */
    public void heroAttaque() {
        if (System.currentTimeMillis() - dernierCoupsEffectueParHero < HERO_HIT_COUNTDOWN) return;
        dernierCoupsEffectueParHero = System.currentTimeMillis();

        List<Personnage> monstresDansHitBoxAttaque = new ArrayList<>();
        Heros hero = getHeros();
        for (Personnage p : personnages) {
            if (hero.getHitBoxAttaque().intersects(p.getHitBoxCollision()) && !p.estUnHeros())
                monstresDansHitBoxAttaque.add(p);
        }
        hero.attaquer(monstresDansHitBoxAttaque);
        for (Personnage p : monstresDansHitBoxAttaque) {
            if (!p.estVivant()) detruirePersonnage(p);
        }
    }

    /**
     * Initiates an attack action for monsters.
     * Checks for a cooldown period and the hero's vitality before performing an attack.
     * Detects and targets the hero within the monsters' attack hitboxes, inflicts damage,
     * and updates the hero's health.
     */
    public void monstreAttaque() {
        if (System.currentTimeMillis() - dernierCoupsEffectueParHero < 500 || !getHeros().estVivant()) return;
        dernierCoupsEffectueParHero = System.currentTimeMillis();

        Heros hero = getHeros();
        for (Personnage p : personnages) {
            if (p.getHitBoxAttaque().intersects(hero.getHitBoxCollision()) && !p.estUnHeros())
                p.attaquer(List.of(hero));
        }
    }

    /**
     * Erases the specified character from the world
     *
     * @param p The character to be destroyed.
     */
    public void detruirePersonnage(Personnage p) {
        this.personnages.remove(p);
        double random = Math.random();
        if (random < Donnees.CHANCE_OF_HEART_DROP) {
            this.objets.add(new Coeur(p.getX(), p.getY(), Donnees.HEART_HEIGHT, Donnees.HEART_WIDTH, Donnees.HEART_VALUE));
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
            objetRamasse.consommePar(h);
            objets.remove(objetRamasse);
        }
    }

    /**
     * Method that check if the Hero is on collision with an Objet that can be triggered. If he is, the object is triggered.
     */
    public void herosDeclenchePiege() {
        Heros h = this.getHeros();
        for (Objet o : objets) {
            if (o.estDeclenchable()) {
                if (collision(h, o)) {
                    o.consommePar(h);
                }
            }
        }
    }

    /**
     * @return a JSON representation of the world
     */
    public JSONObject toJSONSave() {
        JSONObject json = new JSONObject();
        json.put("map", carte);
        json.put("entities", personnages.stream().map(Personnage::toJSON).toArray());
        json.put("objects", objets.stream().map(Objet::toJSON).toArray());
        return json;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
