package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Monstre;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.entities.Walker;
import fr.ul.acl.escape.monde.environment.Mur;
import fr.ul.acl.escape.monde.environment.Terrain;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.GestionFichier;
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
    private final ArrayList<Personnage> personnages;
    private final ArrayList<Terrain> terrains;
    /**
     * Last map loaded
     */
    private String carte;

    private long dernierCoupsEffectueParHero = System.currentTimeMillis();

    /**
     * Create a new world with no elements.
     */
    public Monde() {
        personnages = new ArrayList<>();
        terrains = new ArrayList<>();
    }

    /**
     * Create a Monde from a map
     *
     * @param map
     * @return
     * @throws Exception
     */
    public static Monde fromMap(String map) throws Exception {
        Monde monde = new Monde();
        monde.chargerCarte(map);
        return monde;
    }

    /**
     * Create a Monde from a JSON representation
     *
     * @param json
     * @return
     * @throws Exception
     */
    public static Monde fromJSON(JSONObject json) throws Exception {
        Monde monde = new Monde();
        monde.chargerCarte(json.getString("map"));
        monde.personnages.clear();
        json.getJSONArray("entities").forEach(entity -> {
            monde.personnages.add(Personnage.fromJSON((JSONObject) entity));
        });
        return monde;
    }

    /**
     * Load map's informations in the world
     * Charge les information de la carte dans le monde
     *
     * @param carte nom de la carte à charger
     */
    private void chargerCarte(String carte) throws Exception {
        this.carte = carte;

        // Variable de vérification
        boolean heroExiste = false;

        // On récupère les informations de la carte
        char[][] donnees = GestionFichier.lireFichierCarte(carte);

        // On parcourt les données
        for (int j = 0; j < Donnees.WORLD_HEIGHT; j++) {
            for (int i = 0; i < Donnees.WORLD_WIDTH; i++) {
                if (donnees[j][i] != '0') {
                    // Elements du terrain comme les murs et les trous
                    if (donnees[j][i] == Donnees.SYMBOL_WALL) {
                        this.terrains.add(new Mur(i * Donnees.WALL_WIDTH, j * Donnees.WALL_HEIGHT, Donnees.WALL_HEIGHT, Donnees.WALL_WIDTH));
                    }

                    // Personnages pose sur la carte hero et monstre
                    else if (donnees[j][i] == Donnees.SYMBOL_HERO && !heroExiste) {
                        this.personnages.add(new Heros(i * Donnees.WALL_WIDTH, j * Donnees.WALL_HEIGHT, Donnees.HERO_HEIGHT, Donnees.HERO_WIDTH));
                        heroExiste = true;
                    } else if (donnees[j][i] == Donnees.SYMBOL_WALKER) {
                        this.personnages.add(new Walker(i * Donnees.WALL_WIDTH, j * Donnees.WALL_HEIGHT, Donnees.WALKER_HEIGHT, Donnees.WALKER_WIDTH));
                    }
                }
            }
        }
        if (!heroExiste) throw new Exception("Where hero?");
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
     * Function that check if the Heros can be deplaced and deplaced it in the right direction if there is no collision.
     *
     * @param typeMouvement The Type of mouvement the Heros wants to make.
     */
    public void deplacementHeros(TypeMouvement typeMouvement, double deltaTime) {
        Heros h = getHeros();
        Heros tmp = new Heros(h.getX(), h.getY(), h.getHauteur(), h.getLargeur());
        tmp.deplacer(typeMouvement, deltaTime);

        if (!collisionAvec(tmp, false)) h.deplacer(typeMouvement, deltaTime);
        else h.setOrientation(typeMouvement); // Pour pouvoir se tourner même quand on ne peut pas se déplacer
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
     * Function that check if a personnage is on collision with an element of the world.
     *
     * @param pers           The Personnage which we want to check if he is on collision.
     * @param checkAvecHeros True if check with the Hero too, false otherwise.
     * @return true if collision, false otherwise.
     */
    public boolean collisionAvec(Personnage pers, boolean checkAvecHeros) {
        for (Terrain t : terrains) {
            if (!t.estTraversable()) {
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
     * @param m The Monstre that we want to move.
     */
    public void deplacementMonstre(Monstre m, double deltaTime) {
        Graph<Point2D, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int pas = 2500; // Incrémentation pour construire les noeuds
        int conversionFactor = Donnees.CONVERSION_FACTOR; // Facteur de conversion pour convertir les double en int
        for (int i = 0; i < Donnees.WORLD_WIDTH * conversionFactor; i += pas) {
            for (int j = 0; j < Donnees.WORLD_HEIGHT * conversionFactor; j += pas) {
                Point2D courant = new Point2D(i, j);
                Point2D droite = new Point2D(i + pas, j);
                Point2D bas = new Point2D(i, j + pas);
                graph.addVertex(courant);

                // Personnage tmp pour tester si le noeud est atteignable (pas sur un terrain ou un monstre)
                Walker w = new Walker((double) i / conversionFactor, (double) j / conversionFactor, m.getLargeur(), m.getHauteur(), m.getVitesse(), m.getId());

                // Test si noeud à droite est atteignable (pas en dehors du monde et pas dans un terrain ou un personnage), si oui ajout du noeud au graphe et création d'un arc
                if (i + pas + ((int) ((m.getLargeur() - 0.1) * conversionFactor)) < Donnees.WORLD_WIDTH * conversionFactor && !collisionAvec(w, false)) {
                    graph.addVertex(droite);
                    graph.addEdge(courant, droite);
                }

                // Test si noeud à gauche est atteignable (pas en dehors du monde et pas dans un terrain ou un personnage), si oui ajout du noeud au graphe et création d'un arc
                if (j + pas + ((int) ((m.getHauteur() - 0.1) * conversionFactor)) < Donnees.WORLD_HEIGHT * conversionFactor && !collisionAvec(w, false)) {
                    graph.addVertex(bas);
                    graph.addEdge(courant, bas);
                }
            }
        }

        // Check si le noeud source est pas en dehors en haut ou à gauche
        int sourceX = intLePlusProche((int) (m.getX() * conversionFactor), pas);
        int sourceY = intLePlusProche((int) (m.getY() * conversionFactor), pas);
        Point2D source = new Point2D(sourceX, sourceY);

        Point2D heros = new Point2D(intLePlusProche((int) (getHeros().getX() * conversionFactor), pas), intLePlusProche((int) (getHeros().getY() * conversionFactor), pas));


        Walker tmpWalkerAPorteHeros = new Walker(m.getX() - 0.2, m.getY() - 0.2, m.getHauteur() + 0.4, m.getLargeur() + 0.4);
        if (collision(getHeros(), tmpWalkerAPorteHeros)) return;

        pathfinding(m, pas, graph, source, heros, deltaTime);

    }

    /**
     * Pathfinding algorithm for a Monstre.
     *
     * @param m         The Monstre that we want to move.
     * @param pas       The increment for the construction of the alternate graph.
     * @param graph     The graph.
     * @param source    The node source.
     * @param heros     The node target.
     * @param deltaTime The time difference since the last iteration.
     */
    public void pathfinding(Monstre m, int pas, Graph<Point2D, DefaultEdge> graph, Point2D source, Point2D heros, double deltaTime) {
        DijkstraShortestPath<Point2D, DefaultEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Point2D, DefaultEdge> shortest = shortestPath.getPath(source, heros);
        m.reinitialiseListMouvementsEssayes();

        boolean random = false;

        if (shortest == null) {
            // Pas de chemins vers le héros = trop de monstres directement autour de lui. Génération d'un graphe qui ne prend pas en compte les monstres
            // dans ce cas afin que les monstres se dirigent quand même vers le héros.
            Graph<Point2D, DefaultEdge> graphAlternatif = this.grapheAlternatif(m, pas);
            shortestPath = new DijkstraShortestPath<>(graphAlternatif);
            shortest = shortestPath.getPath(source, heros);

            if (shortest == null) random = true;
        }

        // déplacement random car pas de chemin = monstre bloqué
        if (random) {
            this.mouvementRandom(m, deltaTime);
            return;
        }
        List<Point2D> list = shortest.getVertexList();

        if (list.size() == 1) return;
        Point2D first = list.get(1);
        TypeMouvement typeMouvement = getMouvement(source, first, m);
        if (typeMouvement == null) return;

        Walker tmpWalker = new Walker(m.getX(), m.getY(), m.getHauteur(), m.getLargeur(), m.getVitesse(), m.getId());
        tmpWalker.deplacer(typeMouvement, deltaTime);

        // On fait le mouvement prévu si il est réalisable
        if (!collisionAvec(tmpWalker, true)) {
            m.deplacer(typeMouvement, deltaTime);
            return;
        }

        // le mouvement n'a pas pu être effectué donc on essaye le dernier qui avait réussi
        tmpWalker = new Walker(m.getX(), m.getY(), m.getHauteur(), m.getLargeur(), m.getVitesse(), m.getId());
        tmpWalker.deplacer(m.getOrientation(), deltaTime);
        if (!collisionAvec(tmpWalker, true)) {
            m.deplacer(m.getOrientation(), deltaTime);
            return;
        }

        // Le dernier mouvement n'était pas possible non plus donc on regarde si c'était à cause du héros ou non.
        // Si non, on regarde dans la liste des noeuds du chemin quel prochain mouvement est faisable
        // On garde en mémoire tous les mouvements que le Monstre a essayé.
        if (collisionAvec(tmpWalker, false)) {
            m.addMouvementEssayes(typeMouvement);
            for (int i = 2; i < list.size(); i++) {
                typeMouvement = getMouvement(source, list.get(i), m);
                if (typeMouvement == null) continue;

                // Le mouvement est pas dans la liste donc on peut l'essayer
                if (!m.mouvementDansList(typeMouvement)) {

                    tmpWalker = new Walker(m.getX(), m.getY(), m.getHauteur(), m.getLargeur(), m.getVitesse(), m.getId());
                    tmpWalker.deplacer(typeMouvement, deltaTime);

                    if (!collisionAvec(tmpWalker, true)) {
                        m.deplacer(typeMouvement, deltaTime);
                        return;
                    }
                    m.addMouvementEssayes(typeMouvement);
                }

            }
        }

        // Si vraiment aucun mouvement n'a été fait, mouvement random
        this.mouvementRandom(m, deltaTime);

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
     * @param m         The Monstre that we want to move.
     * @param deltaTime The time difference since the last iteration.
     */
    public void mouvementRandom(Monstre m, double deltaTime) {
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
            Walker tmpWalker = new Walker(m.getX(), m.getY(), m.getHauteur(), m.getLargeur(), m.getVitesse(), m.getId());
            tmpWalker.deplacer(typeMouvement, deltaTime);

            if (!collisionAvec(tmpWalker, true)) {
                m.deplacer(typeMouvement, deltaTime);
                return;
            }
        }
    }

    /**
     * Method that create an alternative graph for the Monstre with node that can be in a Monstre.
     *
     * @param m   The Monstre we want to move.
     * @param pas The increment for the construction of the graph.
     * @return A Graph<Point2, DefaultEdge> for m.
     **/
    public Graph<Point2D, DefaultEdge> grapheAlternatif(Monstre m, int pas) {
        Graph<Point2D, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int conversionFactor = Donnees.CONVERSION_FACTOR;
        for (int i = 0; i < Donnees.WORLD_WIDTH * conversionFactor; i += pas) {
            for (int j = 0; j < Donnees.WORLD_HEIGHT * conversionFactor; j += pas) {
                Point2D courant = new Point2D(i, j);
                Point2D droite = new Point2D(i + pas, j);
                Point2D bas = new Point2D(i, j + pas);
                graph.addVertex(courant);
                Walker w = new Walker((double) i / conversionFactor, (double) j / conversionFactor, m.getLargeur(), m.getHauteur(), m.getVitesse(), m.getId());
                // On ne teste pas si le noeud est sur un Personnage
                if (i + pas + ((int) ((m.getLargeur() - 0.1) * conversionFactor)) < Donnees.WORLD_WIDTH * conversionFactor && !collisionAvecTerrains(w)) {
                    graph.addVertex(droite);
                    graph.addEdge(courant, droite);
                }

                if (j + pas + ((int) ((m.getLargeur() - 0.1) * conversionFactor)) < Donnees.WORLD_HEIGHT * conversionFactor && !collisionAvecTerrains(w)) {
                    graph.addVertex(bas);
                    graph.addEdge(courant, bas);
                }
            }
        }

        return graph;
    }

    /**
     * Method that check if there is collision between a Personnage and one of the Terrain.
     *
     * @param p The Personnage we need to check.
     * @return true if there is a collision, false otherwise.
     */
    public boolean collisionAvecTerrains(Personnage p) {
        for (Terrain t : terrains) {
            if (!t.estTraversable()) {
                if (collision(p, t)) return true;
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
     * Erases the specified character from the world
     *
     * @param p The character to be destroyed.
     */
    public void detruirePersonnage(Personnage p) {
        this.personnages.remove(p);
    }

    /**
     * @return a JSON representation of the world
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("map", carte);
        json.put("entities", personnages.stream().map(Personnage::toJSON).toArray());
        // TODO: add collectibles
        return json;
    }
}
