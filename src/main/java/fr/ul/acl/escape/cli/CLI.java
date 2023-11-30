package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameInterface;
import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.outils.Donnees;

import java.util.ArrayList;
import java.util.Scanner;

public class CLI implements GameInterface {
    /**
     * The game engine.
     */
    private final CLIEngine engine;
    /**
     * The game controller.
     */
    private final CLIController controller;
    /**
     * The scanner to read user input.
     */
    private final Scanner scan = new Scanner(System.in);

    public CLI() {
        System.out.println("Bienvenue dans Escape !");

        controller = new CLIController();
        engine = new CLIEngine(this, controller);
        engine.start();
    }

    public void render() {
        for (int y = 0; y < controller.getHeight(); y++) {
            for (int x = 0; x < controller.getWidth(); x++) {
                String str = getStringRepresentationForPosition(x, y, controller.getTerrains());
                if (str != null) {
                    // Nothing should exist on a terrain
                    System.out.print(str);
                    continue;
                }

                str = getStringRepresentationForPosition(x, y, controller.getPersonnages());
                String[] strSplit = (str == null ? "[ ]" : str).split(" ");
                System.out.print(strSplit[0]);

                String strObjet = getStringRepresentationForPosition(x, y, controller.getObjets());
                System.out.print(strObjet == null ? " " : strObjet);

                System.out.print(strSplit[1]);
            }
            System.out.println();
        }

        if (Donnees.DEBUG) {
            controller.getPersonnages().forEach(System.out::println);
        } else {
            System.out.println(controller.getHeros().toString());
        }

        System.out.println("1: Gauche\n2: Droite\n3: Haut\n4: Bas\n5: Quitter");

        int action = scan.nextInt();
        if (action < 1 || action > 4) {
            engine.stop();
        }
        controller.setAction(action);
    }

    private String getStringRepresentationForPosition(int x, int y, ArrayList<? extends ElementMonde> elements) {
        for (ElementMonde element : elements) {
            if (element.getX() >= x && element.getX() < x + element.getLargeur() && element.getY() >= y && element.getY() < y + element.getHauteur()) {
                return element.getSymbol();
            }
        }
        return null;
    }
}
