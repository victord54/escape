package fr.ul.acl.escape.outils;

import java.io.*;

public class GestionFichier {

    /*
    Constructeur pour moi inutile car les fonctions de gestion de fichier seront toutes indépendantes
     */
    public GestionFichier(){}

    /*
    Fonction qui renvoie un tableau de char du contenu du fichier
     */
    public char[] lireFichierCarte(String nomFichier){

        char [] tableau;

        try
        {
            // Le fichier d'entrée se trouve dans le dossier cartes des ressources
            File file = new File("./src/main/ressources/cartes/"+nomFichier+".txt");

            // Créer l'objet File Reader qui permet de lire le fichier texte
            FileReader fileReader = new FileReader(file);

            // Créer l'objet BufferedReader qui récupère les informations du file reader
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Créer l'objet stringbuffer qui stocke les caractères luent
            StringBuilder stringBuilder = new StringBuilder();

            // Actuellement, le fichier texte est lu ligne par ligne ce qui peut permettre de facilement modifier le code
            // Pour qu'il renvoie un tableau à 2 dimensions.
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                // ajoute la ligne au buffer
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            fileReader.close();

            // On rempli l'objet tableau avec le contenu du fichier texte
            tableau = new char[stringBuilder.length()];
            for (int i = 0; i < stringBuilder.length(); i++) {
                tableau[i] = stringBuilder.charAt(i);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
