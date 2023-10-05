package fr.ul.acl.escape.outils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * manages interactions between external files and the program
 */
public class GestionFichier {

    /**
     * lireFichierCarte is use for extract informations of map for load it in game
     *
     * @param nomFichier Name of the map (without .txt)
     * @return tab of element size of world
     */
    public static char[][] lireFichierCarte(String nomFichier) {

        // Le tableau sera de la taille de la du monde
        char[][] tableau = new char[Donnees.WORLD_HEIGHT][Donnees.WORLD_WIDTH];

        try {
            // Le fichier d'entrée se trouve dans le dossier cartes des ressources
            File file = new File(Resources.get("maps/" + nomFichier + ".txt").getPath());

            // Créer l'objet File Reader qui permet de lire le fichier texte
            FileReader fileReader = new FileReader(file);

            // Créer l'objet BufferedReader qui récupère les informations du file reader
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Actuellement, le fichier texte est lu ligne par ligne pour remplir de manière
            String line;

            // numéro de ligne dans la carte
            int j = 0;

            // On remplie le tableau 2D avec chaque element du txt
            while ((line = bufferedReader.readLine()) != null && j < Donnees.WORLD_HEIGHT) {
                for (int i = 0; i < Donnees.WORLD_WIDTH; i++) {
                    tableau[j][i] = line.charAt(i);
                }

                // A la fin de ligne on passe à la suivante
                j++;
            }
            // ATTENTION : On ferme le Reader
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableau;
    }
}
