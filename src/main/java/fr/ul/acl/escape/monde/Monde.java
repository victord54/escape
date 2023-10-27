package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.Donnees;
import fr.ul.acl.escape.outils.GestionFichier;
import javafx.geometry.Point2D;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.alg.shortestpath.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        if (!collisionAvec(tmp,false)) this.getHeros().deplacer(typeMouvement, deltaTime);
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

    public boolean collisionAvec(Personnage pers, boolean checkAvecHeros){
        for (Terrain t : terrains ){
            if (collision(pers, t)) return true;
        }
        for (Personnage p : personnages) {
            if (checkAvecHeros){
                if (pers.getId() != p.getId()) {
                    if (collision(pers, p)) return true;
                }
            }
            if (pers.getId() != p.getId() && !p.estUnHeros()) if (collision(pers,p)) return true;

        }
        return false;
    }

    public void deplacementMonstre(Monstre m, double timeInDouble) {
        Graph<Point2D, DefaultEdge> tmp = new SimpleGraph<>(DefaultEdge.class);
        Point2D source = null;
        Point2D heros = null;
        //double v = 0.05;
        int v = 500;
        int mult = 10000;
        for (int i = 0 ; i < Donnees.WORLD_WIDTH*mult; i+= v) {
            for (int j = 0; j < Donnees.WORLD_HEIGHT*mult; j += v) {
                Point2D courant = new Point2D(i, j);
                Point2D droite = new Point2D(i+v, j);
                Point2D bas = new Point2D(i, j+v);
                tmp.addVertex(courant);
                Walker w = new Walker((double) i /mult, (double) j /mult, m.getLargeur(), m.getHauteur(), m.getVitesse(), m.getId());
                if (i + v < Donnees.WORLD_WIDTH*mult && !collisionAvec(w,false) ) {
                    tmp.addVertex(droite);
                    tmp.addEdge(courant, droite);
                }

                if (j + v < Donnees.WORLD_HEIGHT*mult && !collisionAvec(w,false) ) {
                    tmp.addVertex(bas);
                    tmp.addEdge(courant, bas);
                }

                Walker tmpCol = new Walker((double) i /mult, (double) j /mult,0,0,0,-1);
                if (source == null) {
                    //if (collision(tmpCol, m)) source = courant;
                    if (i == (int) (m.getX()*mult) && j == (int) (m.getY()*mult)) source = new Point2D(i,j);
                }
                if (heros == null) {
                    if (collision(tmpCol, getHeros())) heros = courant;
                }
            }
        }

        AStarShortestPath<Point2D, DefaultEdge> shortestPath = new AStarShortestPath<>(tmp, new AStarAdmissibleHeuristic<Point2D>() {
            @Override
            public double getCostEstimate(Point2D Point2D, Point2D v1) {
                return sqrt(pow(v1.getX() - Point2D.getX(), 2) + pow(v1.getY() - Point2D.getY(), 2));
            }
        });
        //BFSShortestPath<Point2D, DefaultEdge> shortestPath = new BFSShortestPath<>(tmp);
        //DijkstraShortestPath<Point2D, DefaultEdge> shortestPath = new DijkstraShortestPath<>(tmp);
        System.out.println("----------");
        System.out.println("S : " + source);
        System.out.println("H : " + heros);
        if (source == null){
            System.out.println("x:" +(int) (m.getX()*mult) + "y:" +(int) (m.getY()*mult) );
            return;
        }
        GraphPath<Point2D, DefaultEdge> shortest = shortestPath.getPath(source,heros);

        if (shortest == null) return;
        List<Point2D> list = shortest.getVertexList();
        if (list.size() == 1) return;
        Point2D first = list.get(1);

        try {
            System.out.println("first : " +first );
            System.out.println("m : " + m );
            TypeMouvement typeMouvement = null;
            if ((first.getX()) < (int) (m.getX()*mult)) {
                typeMouvement = TypeMouvement.LEFT;
                //m.deplacer(TypeMouvement.LEFT, timeInDouble);
            }
            else if ((first.getX()) > (int) (m.getX()*mult)) {
                typeMouvement = TypeMouvement.RIGHT;
                //m.deplacer(TypeMouvement.RIGHT, timeInDouble);
            }
            else if ((first.getY()) > (int) (m.getY()*mult)) {
                typeMouvement = TypeMouvement.DOWN;
                //m.deplacer(TypeMouvement.DOWN, timeInDouble);
            }
            else if ((first.getY()) < (int) (m.getY()*mult)) {
                typeMouvement = TypeMouvement.UP;
                //m.deplacer(TypeMouvement.UP, timeInDouble);
            }
            Walker tmpWalker = new Walker(m.getX(), m.getY(), m.getHauteur(), m.getLargeur(), m.getVitesse(), m.getId());
            tmpWalker.deplacer(typeMouvement, timeInDouble);

            if (!collisionAvec(tmpWalker,true)) m.deplacer(typeMouvement, timeInDouble);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }


    public void deplacementMonstres(double timeInDouble){
        List<Thread> threads = new ArrayList<>();
        for (Personnage p : personnages) {
            if (!p.estUnHeros()){
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deplacementMonstre((Monstre) p, timeInDouble);
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
                e.printStackTrace();
            }
        }


        /*for (Personnage p : personnages){
            if (!p.estUnHeros()){
                System.out.println("W : "+ p.toString());
                deplacementMonstre((Monstre) p, timeInDouble);

            }
        }*/
    }

}
