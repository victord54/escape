package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.GameMode;
import fr.ul.acl.escape.Settings;
import fr.ul.acl.escape.engine.GameInterface;
import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.outils.Resources;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
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
        // clear screen
        System.out.print("\033[H\033[2J");

        // print welcome message
        System.out.println(Resources.getI18NString("game.welcome").replace("${game.title}", Resources.getI18NString("game.title")));
        String settingsFilePath = Settings.getFileFullPath();
        if (settingsFilePath != null) {
            System.out.println('\n' + Resources.getI18NString("settings.info").replace("${settings.file}", settingsFilePath));
        }
        System.out.println('\n' + Resources.getI18NString("pressEnterToContinue"));

        // wait for user input
        scan.nextLine();

        controller = new CLIController();
        engine = new CLIEngine(this, controller);
        engine.start();
    }

    public void render() {
        // clear screen
        System.out.print("\033[H\033[2J");

        // print turn number
        System.out.println(Resources.getI18NString("game.turn").replace("${turn}", String.valueOf(engine.getTurn())) +
                (controller.getGameMode() == GameMode.CAMPAIGN ?
                        (" - " + Resources.getI18NString("game.level") + " " + controller.getCurrentLevelDifficulty()) :
                        ""));

        // print game board
        for (int y = 0; y < controller.getHeight(); y++) {
            for (int x = 0; x < controller.getWidth(); x++) {
                String res;

                String strTerrain = getStringRepresentationForPosition(x, y, controller.getTerrains());
                res = strTerrain == null ? "[ ]" : strTerrain;
                String strEntity = getStringRepresentationForPosition(x, y, controller.getPersonnages());
                res = strEntity == null ? res : strEntity; // If there is an entity, it will override the terrain (ex: ghost through wall)

                if (strTerrain != null) {
                    // If there is a terrain, we don't need to check for objects
                    System.out.print(res);
                    continue;
                }

                String[] strSplit = res.split(" ");
                System.out.print(strSplit[0]); // print color code (if there is one) and opening bracket

                String strObjet = getStringRepresentationForPosition(x, y, controller.getObjets());
                System.out.print(strObjet == null ? " " : strObjet); // print object if there is one

                System.out.print(strSplit[1]); // print closing bracket and reset color code (if there is one)
            }
            System.out.println();
        }

        // print characters data
        controller.getPersonnages().forEach(System.out::println);

        if (!controller.getHeros().estVivant()) {
            // print game over message
            System.out.println(Resources.getI18NString("game.end"));

            // close the game
            System.exit(0);
        }

        // print actions
        List<String> actions = new ArrayList<>() {{
            add(Resources.getI18NString("left"));
            add(Resources.getI18NString("right"));
            add(Resources.getI18NString("up"));
            add(Resources.getI18NString("down"));
            add(Resources.getI18NString("wait"));
            add(Resources.getI18NString("attack"));
            add(Resources.getI18NString("take"));
            add(Resources.getI18NString("quit"));
        }};
        System.out.println(Resources.getI18NString("selectAction"));
        for (int i = 1; i <= actions.size(); i++) {
            System.out.println(i + " - " + actions.get(i - 1));
        }

        // read user input
        int action = scan.nextInt();
        if (action < 1 || action > actions.size()) {
            System.out.println("\u001B[31m" + Resources.getI18NString("invalidAction") + "\u001B[0m");
            render();
            return;
        }

        if (action == actions.size()) {
            // clear screen
            System.out.print("\033[H\033[2J");

            // close the game
            System.exit(0);
        }

        controller.setAction(action);
    }

    private String getStringRepresentationForPosition(int x, int y, ArrayList<? extends ElementMonde> elements) {
        for (ElementMonde element : elements) {
            Rectangle2D hitBoxElement = new Rectangle2D(element.getX(), element.getY(), element.getLargeur(), element.getHauteur());

            Rectangle2D hitBoxPosition = new Rectangle2D(x, y, 1, 1);
            if (hitBoxElement.intersects(hitBoxPosition)) {
                return element.getSymbol();
            }
        }
        return null;
    }
}
