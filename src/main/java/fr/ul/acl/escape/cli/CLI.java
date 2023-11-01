package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameInterface;
import fr.ul.acl.escape.monde.Personnage;
import fr.ul.acl.escape.monde.Terrain;
import fr.ul.acl.escape.outils.Donnees;

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
        for (int y = 0; y < Donnees.WORLD_HEIGHT; y++) {
            for (int x = 0; x < Donnees.WORLD_WIDTH; x++) {
                boolean isThereElement = false;
                for (Terrain terrain : controller.getTerrains()) {
                    if (terrain.getX() >= x && terrain.getX() < x + terrain.getLargeur() && terrain.getY() >= y && terrain.getY() < y + terrain.getHauteur()) {
                        System.out.print("[" + Donnees.SYMBOL_WALL + "]");
                        isThereElement = true;
                        break;
                    }
                }
                if (isThereElement) continue;
                for (Personnage personnage : controller.getPersonnages()) {
                    if (personnage.getX() >= x && personnage.getX() < x + personnage.getLargeur() && personnage.getY() >= y && personnage.getY() < y + personnage.getHauteur()) {
                        System.out.print("[" + (personnage.estUnHeros() ? Donnees.SYMBOL_HERO : Donnees.SYMBOL_WALKER) + "]");
                        isThereElement = true;
                        break;
                    }
                }

                if (!isThereElement) {
                    System.out.print("[ ]");
                }
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
}
