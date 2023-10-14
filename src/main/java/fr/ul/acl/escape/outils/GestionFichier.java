package fr.ul.acl.escape.outils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * manages interactions between external files and the program
 */
public class GestionFichier {

    /**
     * lireFichierCarte is used to extract map information from a .txt file
     *
     * @param nomCarte Name of the map (without .txt)
     * @return tab of element size of world
     */
    public static char[][] lireFichierCarte(String nomCarte) {

        // Le tableau sera de la taille de la du monde
        char[][] tableau = new char[Donnees.WORLD_HEIGHT][Donnees.WORLD_WIDTH];

        try {
            // Le fichier d'entrée se trouve dans le dossier "maps" des ressources
            InputStream inputStream = Resources.getAsStream("maps/" + nomCarte + ".txt");

            // Créer l'objet Reader qui permet de lire le fichier
            InputStreamReader reader = new InputStreamReader(inputStream);

            // Créer l'objet BufferedReader qui récupère les informations du file reader
            BufferedReader bufferedReader = new BufferedReader(reader);

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
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableau;
    }
}
