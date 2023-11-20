package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameInterface;
import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.entities.Personnage;
import fr.ul.acl.escape.monde.environment.Terrain;
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
                Character c = getCharForPosition(x, y, controller.getTerrains());
                if (c != null) {
                    System.out.print("[" + c + "]");
                    continue;
                }
                c = getCharForPosition(x, y, controller.getPersonnages());
                if (c != null) {
                    System.out.print("[" + c + "]");
                    continue;
                }
                System.out.print("[ ]");
            }
            System.out.println();
        }

        if (Donnees.DEBUG) {
            for (Personnage p : controller.getPersonnages()) {
                System.out.println((p.estUnHeros() ? "Hero: " : "Monstre: ") + p);
            }
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

    private Character getCharForPosition(int x, int y, ArrayList<? extends ElementMonde> elements) {
        for (ElementMonde element : elements) {
            if (element.getX() >= x && element.getX() < x + element.getLargeur() && element.getY() >= y && element.getY() < y + element.getHauteur()) {
                return element.getSymbol();
            }
        }
        return null;
    }
}
