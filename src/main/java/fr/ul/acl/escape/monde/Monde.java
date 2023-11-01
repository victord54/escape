package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.GestionFichier;
import javafx.geometry.Point2D;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Monde {
    private final ArrayList<Personnage> personnages;
    private final ArrayList<Terrain> terrains;

    public Monde() {
        personnages = new ArrayList<>();
        terrains = new ArrayList<>();
    }

    /**
     * Load map's informations in the world
     * Charge les information de la carte dans le monde
     *
     * @param carte nom de la carte à charger
     */
    public void chargerCarte(String carte) throws Exception {
        // Variable de vérification
        boolean heroExiste = false;

        // On récupère les informations de la carte
        char[][] donnees = GestionFichier.lireFichierCarte(carte);

        // On parcours les données
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
        if (!heroExiste) throw new Exception("Where hero ?");
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
     * @throws MouvementNullException Exception throw if typeMouvement is null.
     */
    public void deplacementHeros(TypeMouvement typeMouvement, double deltaTime) throws MouvementNullException {
        if (typeMouvement == null) throw new MouvementNullException();

        Heros h = getHeros();
        Heros tmp = new Heros(h.getX(), h.getY(), h.getHauteur(), h.getLargeur());
        tmp.deplacer(typeMouvement, deltaTime);

        if (!collisionAvec(tmp, false)) this.getHeros().deplacer(typeMouvement, deltaTime);
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
            if (collision(pers, t)) return true;
        }
        for (Personnage p : personnages) {
            if (checkAvecHeros) {
                if (pers.getId() != p.getId()) {
                    if (collision(pers, p)) return true;
                }
            }
            if (pers.getId() != p.getId() && !p.estUnHeros()) if (collision(pers, p)) return true;

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
    public static int intLePlusProche(int value, int multiple) {
        int remainder = value % multiple;
        if (remainder == 0) {
            return value;
        } else {
            return ((value / multiple) + 1) * multiple;
        }
    }

    /**
     * Method that move a Monstre.
     *
     * @param m The Monstre that we want to move.
     */
    public void deplacementMonstre(Monstre m, double deltaTime) {
        Graph<Point2D, DefaultEdge> tmp = new SimpleGraph<>(DefaultEdge.class);
        int pas = 950;
        int conversionFactor = Donnees.CONVERSION_FACTOR;
        for (int i = 0; i < Donnees.WORLD_WIDTH * conversionFactor; i += pas) {
            for (int j = 0; j < Donnees.WORLD_HEIGHT * conversionFactor; j += pas) {
                Point2D courant = new Point2D(i, j);
                Point2D droite = new Point2D(i + pas, j);
                Point2D bas = new Point2D(i, j + pas);
                tmp.addVertex(courant);
                Walker w = new Walker((double) i / conversionFactor, (double) j / conversionFactor, m.getLargeur(), m.getHauteur(), m.getVitesse(), m.getId());
                if (i + pas < Donnees.WORLD_WIDTH * conversionFactor && !collisionAvec(w, false)) {
                    tmp.addVertex(droite);
                    tmp.addEdge(courant, droite);
                }

                if (j + pas < Donnees.WORLD_HEIGHT * conversionFactor && !collisionAvec(w, false)) {
                    tmp.addVertex(bas);
                    tmp.addEdge(courant, bas);
                }
            }
        }

        /*AStarShortestPath<Point2D, DefaultEdge> shortestPath = new AStarShortestPath<>(tmp, new AStarAdmissibleHeuristic<Point2D>() {
            @Override
            public double getCostEstimate(Point2D Point2D, Point2D v1) {
                return sqrt(pow((int) (v1.getX()*conversionFactor) - (int) (Point2D.getX()*conversionFactor), 2) + pow((int) (v1.getY()*conversionFactor) - (int) (Point2D.getY()*conversionFactor), 2));
            }
        });*/
        //BFSShortestPath<Point2D, DefaultEdge> shortestPath = new BFSShortestPath<>(tmp);

        DijkstraShortestPath<Point2D, DefaultEdge> shortestPath = new DijkstraShortestPath<>(tmp);

        Point2D source = new Point2D(intLePlusProche((int) (m.getX() * conversionFactor), pas), intLePlusProche((int) (m.getY() * conversionFactor), pas));
        Point2D heros = new Point2D(intLePlusProche((int) (getHeros().getX() * conversionFactor), pas), intLePlusProche((int) (getHeros().getY() * conversionFactor), pas));

        GraphPath<Point2D, DefaultEdge> shortest = shortestPath.getPath(source, heros);

        if (shortest == null) {
            Graph<Point2D, DefaultEdge> graphAlternatif = this.grapheAlternatif(m, pas);
            shortestPath = new DijkstraShortestPath<>(graphAlternatif);
            shortest = shortestPath.getPath(source, heros);
            if (shortest == null) return;
        }
        List<Point2D> list = shortest.getVertexList();
        if (list.size() == 1) return;
        Point2D first = list.get(1);

        try {
            TypeMouvement typeMouvement = null;
            if (first.getX() < source.getX()) typeMouvement = TypeMouvement.LEFT;
            else if ((first.getX()) > source.getX()) typeMouvement = TypeMouvement.RIGHT;
            else if ((first.getY()) > source.getY()) typeMouvement = TypeMouvement.DOWN;
            else if ((first.getY()) < source.getY()) typeMouvement = TypeMouvement.UP;

            if (typeMouvement == null) return;

            Walker tmpWalker = new Walker(m.getX(), m.getY(), m.getHauteur(), m.getLargeur(), m.getVitesse(), m.getId());
            tmpWalker.deplacer(typeMouvement, deltaTime);

            if (!collisionAvec(tmpWalker, true)) m.deplacer(typeMouvement, deltaTime);
        } catch (Exception e) {

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
        Graph<Point2D, DefaultEdge> tmp = new SimpleGraph<>(DefaultEdge.class);
        int conversionFactor = Donnees.CONVERSION_FACTOR;
        for (int i = 0; i < Donnees.WORLD_WIDTH * conversionFactor; i += pas) {
            for (int j = 0; j < Donnees.WORLD_HEIGHT * conversionFactor; j += pas) {
                Point2D courant = new Point2D(i, j);
                Point2D droite = new Point2D(i + pas, j);
                Point2D bas = new Point2D(i, j + pas);
                tmp.addVertex(courant);
                Walker w = new Walker((double) i / conversionFactor, (double) j / conversionFactor, m.getLargeur(), m.getHauteur(), m.getVitesse(), m.getId());
                if (i + pas < Donnees.WORLD_WIDTH * conversionFactor && !collisionAvecTerrains(w)) {
                    tmp.addVertex(droite);
                    tmp.addEdge(courant, droite);
                }

                if (j + pas < Donnees.WORLD_HEIGHT * conversionFactor && !collisionAvecTerrains(w)) {
                    tmp.addVertex(bas);
                    tmp.addEdge(courant, bas);
                }
            }
        }

        return tmp;
    }

    /**
     * Method that check if there is collision between a Personnage and one of the Terrain.
     *
     * @param p The Personnage we need to check.
     * @return true if there is a collision, false otherwise.
     */
    public boolean collisionAvecTerrains(Personnage p) {
        for (Terrain t : terrains) {
            if (collision(p, t)) return true;
        }
        return false;
    }

    /**
     * Method that move all the Monstres of the world.
     */
    public void deplacementMonstres(double deltaTime) {
        List<Thread> threads = new ArrayList<>();
        for (Personnage p : personnages) {
            if (!p.estUnHeros()) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deplacementMonstre((Monstre) p, deltaTime);
                    }
                });
                t.start();
                threads.add(t);
            }
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

}
