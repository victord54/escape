package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameInterface;

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

    public CLI() {
        System.out.println("Bienvenue dans Escape !");

        controller = new CLIController();
        engine = new CLIEngine(this, controller);
        engine.start();
    }

    public void render() {
        System.out.println(controller.getHeros().toString());
        System.out.println("1: Gauche\n2: Droite\n3: Haut\n4: Bas\n5: Quitter");

        Scanner scan = new Scanner(System.in);
        int action = scan.nextInt();
        scan.close();
        if (action < 1 || action > 4) {
            engine.stop();
        }
        controller.setAction(action);
    }
}
