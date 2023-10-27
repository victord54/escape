package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.GestionFichier;
import fr.ul.acl.escape.outils.Noeud;
import javafx.scene.Node;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

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

        if (!collisionAvec(tmp)) this.getHeros().deplacer(typeMouvement, deltaTime);

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

    public ArrayList<Personnage> getPersonnages() {
        return personnages;
    }

    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    public boolean collisionAvec(Personnage pers){
        for (Terrain t : terrains ){
            if (collision(pers, t)) return true;
        }
        for (Personnage p : personnages) {
            if (pers.getId() != p.getId()) {
                if (collision(pers, p)) return true;
            }
        }
        return false;
    }

    public Graph<Noeud, DefaultEdge> creerGraphe() {
        Graph<Noeud, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        for (double i = 0. ; i < Donnees.WINDOW_DEFAULT_WIDTH; i+= 0.5) {
            for (double j = 0. ; j < Donnees.WINDOW_DEFAULT_HEIGHT; j+= 0.5) {
                graph.addVertex(new Noeud(i, j));
            }
        }
        return graph;
    }

    public TypeMouvement getDeplacement(Monstre m) {
        Graph<Noeud, DefaultEdge> tmp = new SimpleGraph<>(DefaultEdge.class);
        double width = m.getLargeur();
        double height = m.getHauteur();
        Noeud source = null;
        Noeud heros = null;
        for (double i = 0. ; i < Donnees.WINDOW_DEFAULT_WIDTH; i+= 0.5) {
            for (double j = 0.; j < Donnees.WINDOW_DEFAULT_HEIGHT; j += 0.5) {
                Noeud courant = new Noeud(i, j);
                Noeud droite = new Noeud(i+0.5, j);
                Noeud bas = new Noeud(i, j+0.5);
                tmp.addVertex(courant);
                Walker w = new Walker(i, j, m.getLargeur(), m.getHauteur(), m.getVitesse(), m.getId());
                if (i + 0.5 < Donnees.WINDOW_DEFAULT_WIDTH) {
                    tmp.addVertex(droite);
                    tmp.addEdge(courant, droite);
                }

                if (j + 0.5 < Donnees.WINDOW_DEFAULT_HEIGHT) {
                    tmp.addVertex(bas);
                    tmp.addEdge(courant, bas);
                }
                Walker tmpCol = new Walker(i,j,0,0);
                if (source == null) {
                    if (collision(tmpCol,m)) source = courant;
                }
                if (heros == null) {
                    if (collision(tmpCol, getHeros())) heros = courant;
                }
            }
        }
        System.out.println(tmp.edgeSet());
/*        AStarShortestPath<Noeud, DefaultEdge> shortestPath = new AStarShortestPath<>(tmp, new AStarAdmissibleHeuristic<Noeud>() {
            @Override
            public double getCostEstimate(Noeud noeud, Noeud v1) {
                return sqrt(pow(v1.getX() - noeud.getX(), 2) + pow(v1.getY() - noeud.getY(), 2));
            }
        });*/
        BFSShortestPath<Noeud, DefaultEdge> shortestPath = new BFSShortestPath<>(tmp);



        System.out.println(source.getX());
        System.out.println(source.getY());
        System.out.println(heros.getX());
        System.out.println(heros.getY());
        GraphPath<Noeud, DefaultEdge> shortest = shortestPath.getPath(source, heros);
        List<Noeud> list = shortest.getVertexList();
        Noeud first = list.get(0);



        if (first.getX() < m.getX())
            return TypeMouvement.LEFT;
        else if (first.getX() > m.getX())
            return TypeMouvement.RIGHT;
        else if (first.getY() > m.getY())
            return TypeMouvement.DOWN;
        else return TypeMouvement.UP;
    }
}
