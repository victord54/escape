package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.entities.Fantome;
import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.entities.Walker;
import fr.ul.acl.escape.monde.environment.BordureMonde;
import fr.ul.acl.escape.monde.environment.Mur;
import fr.ul.acl.escape.monde.objects.*;

import java.util.*;

/**
 * This class facilitates the instantiation of a ProceduralGenerator, which is responsible for creating a complete map for a given level.
 * It generates various terrain elements, places character spawn points, and positions objects based on an integer difficulty value provided in the constructor.
 * The higher the difficulty value, the more challenging the level becomes.
 *
 * To achieve this, the class employs a maze generation algorithm initiated by a seed. This algorithm creates interconnected corridors.
 * Subsequently, another algorithm is applied to the maze (named splash here) to carve into specific walls, enabling the creation of expansive spaces while ensuring
 * all open areas are interconnected.
 */
public class ProceduralGenerator {

    private static final boolean WALL = true;
    private static final boolean EMPTY = false;

    private final boolean[][] level;

    private final List<int[]> visited;

    private final int width;  // Largeur du niveau
    private final int height; // Hauteur du niveau

    private final List<Personnage> personnages = new ArrayList<>();
    private final List<Objet> objets = new ArrayList<>();

    private final long seed;

    private final int difficulty;

    /**
     * Constructs a ProceduralGenerator with the specified seed and difficulty level.
     *
     * @param seed           The seed for random number generation.
     * @param difficultLevel The difficulty level used to determine the size and complexity of the generated map.
     */
    public ProceduralGenerator(long seed, int difficultLevel) {
        this.seed = seed;
        this.difficulty = difficultLevel;

        //Génération de l'aléatoire par rapport à la seed
        Random random = new Random(seed);

        //Création d'une largeur et d'une hauteur aléatoire
        this.width = random.nextInt(14,18);
        this.height = random.nextInt(8,14);

        level = new boolean[height][width];
        visited = new ArrayList<>();

        // Initialiser le niveau avec des murs
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                level[i][j] = WALL;
            }
        }

        // Appeler l'algorithme de génération de labyrinthe
        generateMaze(random, 0, 0, width - 1, height - 1);

        //Appeler l'algorithme de génération de grands espaces.
        splash(random);

        //Placement des entités et des objets.
        Map<String, Integer> stats = getDifficultyStatistics(difficultLevel);

        //Hero
        int[] choosen = caseVisiteAleatoire(random);
        personnages.add(new Heros(choosen[1]+1, choosen[0]+1, 0.8, 0.7, 4, 5, 8 , 8, 1, 0, FabriqueId.getInstance().getId()));

        //Trappe
        choosen = caseVisiteAleatoire(random);
        objets.add(new Trappe(choosen[1]+1, choosen[0]+1, 1, 1, true));

        //Training
        choosen = caseVisiteAleatoire(random);
        objets.add(new Training(choosen[1]+1, choosen[0]+1, 1, 1));

        //Walkers
        for(int i = 0; i<stats.get("walkers"); i++){
            choosen = caseVisiteAleatoire(random);
            personnages.add(new Walker(choosen[1]+1, choosen[0]+1, 0.7, 0.5, 3, 3, 3 , 3, 1, FabriqueId.getInstance().getId()));
        }

        //Fantomes
        for(int i = 0; i<stats.get("fantomes"); i++){
            choosen = caseVisiteAleatoire(random);
            personnages.add(new Fantome(choosen[1]+1, choosen[0]+1, 0.7, 0.5, 3, 3, 3 , 3, 1, FabriqueId.getInstance().getId()));
        }

        //Pièges
        for(int i = 0; i<stats.get("pieges"); i++){
            choosen = caseVisiteAleatoire(random);
            objets.add(new Piege(choosen[1]+1, choosen[0]+1,0.4,0.8,1));
        }

        //Coeurs
        for(int i = 0; i<stats.get("coeurs"); i++){
            choosen = caseVisiteAleatoire(random);
            objets.add(new Coeur(choosen[1]+1, choosen[0]+1,0.5,0.5,1));
        }

    }

    /**
     * Generates a non-zero seed based on the current system time.
     * <p>
     * This method generates a seed using the current system time, ensuring
     * that the generated seed is non-zero. It utilizes a loop to handle the unlikely
     * case where the initial seed is zero.
     * </p>
     *
     * @return A non-zero seed based on the current system time.
     */
    public static long genererSeed(){
        long seed;
        do {
            seed = System.currentTimeMillis();
        } while (seed == 0);

        return seed;
    }

    /**
     * Retrieves difficulty-specific statistics based on the provided difficulty level.
     * <p>
     * This method calculates and returns a map containing difficulty-specific statistics
     * based on the provided difficulty level. The statistics include the number of hearts,
     * traps, walkers, and ghosts.
     * </p>
     *
     * @param difficulty The difficulty level for which statistics are calculated.
     * @return A map containing difficulty-specific statistics.
     */
    private Map<String, Integer> getDifficultyStatistics(int difficulty){

        Map<String, Integer> res = new HashMap<>();

        res.put("coeurs", (int)Math.floor(Math.sqrt(difficulty)));

        res.put("pieges", difficulty);

        res.put("walkers", (int)Math.floor(difficulty*0.7)+1);
        res.put("fantomes", (int)Math.floor(difficulty*0.3));

        return res;
    }

    /**
     * Generates a maze within the specified boundaries using a recursive division algorithm.
     *
     * @param random A Random object used for generating random values.
     * @param x      The starting x-coordinate of the area to generate the maze within.
     * @param y      The starting y-coordinate of the area to generate the maze within.
     * @param width  The width of the area to generate the maze within.
     * @param height The height of the area to generate the maze within.
     */
    private void generateMaze(Random random, int x, int y, int width, int height) {
        if (width < 2 || height < 2) {
            return;
        }

        // Choisir une position de mur
        int wallX = x + 1 + random.nextInt(Math.max(1, (width - 1) / 2)) * 2;
        int wallY = y + 1 + random.nextInt(Math.max(1, (height - 1) / 2)) * 2;

        // Créer un passage à travers le mur
        for (int i = x; i <= x + width; i++) {
            level[wallY][i] = EMPTY;
            visited.add(new int[]{wallY, i});
        }
        for (int i = y; i <= y + height; i++) {
            level[i][wallX] = EMPTY;
            visited.add(new int[]{i, wallX});
        }

        // Récursivement diviser les quatre sections
        generateMaze(random, x, y, wallX - x - 1, wallY - y - 1);                 // En haut à gauche
        generateMaze(random, wallX + 1, y, x + width - wallX - 1, wallY - y - 1); // En haut à droite
        generateMaze(random, x, wallY + 1, wallX - x - 1, y + height - wallY - 1); // En bas à gauche
        generateMaze(random, wallX + 1, wallY + 1, x + width - wallX - 1, y + height - wallY - 1); // En bas à droite
    }

    /**
     * Generates empty spaces on the map, randomly emptying a portion of unvisited cells.
     *
     * @param random A Random object used for generating random values.
     */
    private void splash(Random random) {
        int i = 0;
        int borne = (int) Math.floor(((height * width) - visited.size()) * 0.7);
        while (i < borne) {
            List<int[]> neighbors = new ArrayList<>();
            while (neighbors.size() <= 0) {
                int[] chosen = caseVisiteAleatoire(random);
                neighbors = getVoisinsEligibles(chosen[0], chosen[1]);
            }

            int[] neighborAVider = neighbors.get(random.nextInt(neighbors.size()));
            visited.add(neighborAVider);
            level[neighborAVider[0]][neighborAVider[1]] = EMPTY;

            i++;
        }
    }

    /**
     * Retrieves eligible neighbors for a given cell coordinate on the map.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return An ArrayList containing eligible neighboring cell coordinates.
     */
    private List<int[]> getVoisinsEligibles(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();
        int[][] offsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] offset : offsets) {
            int nx = x + offset[0];
            int ny = y + offset[1];

            if (nx >= 0 && nx < level.length && ny >= 0 && ny < level[0].length
                    && !containsCoordinate(visited, nx, ny)) {
                neighbors.add(new int[]{nx, ny});
            }
        }

        return neighbors;
    }

    /**
     * Checks if the specified coordinates are present in the given list.
     *
     * @param list The list of coordinates to check.
     * @param x    The x-coordinate to check.
     * @param y    The y-coordinate to check.
     * @return True if the coordinates are present, false otherwise.
     */
    private boolean containsCoordinate(List<int[]> list, int x, int y) {
        for (int[] coord : list) {
            if (coord[0] == x && coord[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the coordinates of a randomly chosen visited cell.
     *
     * @param random A Random object used for generating random values.
     * @return The coordinates of a randomly chosen visited cell.
     */
    private int[] caseVisiteAleatoire(Random random) {
        int randomized = random.nextInt(visited.size());

        int[] res = visited.get(randomized);
        visited.remove(randomized);

        return res;
    }


    /**
     * Retrieves the world represented by the current state of the map.
     *
     * @return A 'Monde' object representing the world with appropriate borders, walls, characters, and objects.
     */
    public Monde getMonde() {
        Monde monde = new Monde(height + 2, width + 2, this.seed, this.difficulty);

        // Ajout des bordures
        for (int i = 0; i < monde.getWidth(); i++) {
            monde.addTerrains(new BordureMonde(i, 0));
            monde.addTerrains(new BordureMonde(i, monde.getHeight() - 1));
        }
        for (int i = 0; i < monde.getHeight(); i++) {
            monde.addTerrains(new BordureMonde(0, i));
            monde.addTerrains(new BordureMonde(monde.getWidth() - 1, i));
        }

        // Ajout des murs par rapport à la map construite
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (level[j][i]) {
                    monde.addTerrains(new Mur(i + 1, j + 1, 1, 1));
                }
            }
        }

        // Ajout des personnages
        for (Personnage p : personnages) {
            monde.addPersonnage(p);
        }

        // Ajout des objets
        for (Objet o : objets) {
            monde.addObjet(o);
        }

        return monde;
    }
}
