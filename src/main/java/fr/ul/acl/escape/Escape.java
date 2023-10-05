package fr.ul.acl.escape;

import fr.ul.acl.escape.gui.VIEWS;
import fr.ul.acl.escape.gui.ViewManager;
import fr.ul.acl.escape.gui.views.GameView;
import fr.ul.acl.escape.gui.views.HomeView;
import fr.ul.acl.escape.outils.Donnees;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class Escape extends Application {

    public static void main(String[] args) {
        Monde monde = new Monde();
        monde.addPersonnage(new Heros(0, 0, 1, 1, 1));

        int inp;
        boolean again = true;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println(monde.getHeros().toString());
            System.out.println("1: Gauche\n2: Droite\n3: Haut\n4: Bas\n5: Quitter");
            inp = scan.nextInt();
            switch (inp) {
                case 1 -> {
                    try {
                        monde.deplacementHeros(TypeMouvement.LEFT);
                    } catch (MouvementNullException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 2 -> {
                    try {
                        monde.deplacementHeros(TypeMouvement.RIGHT);
                    } catch (MouvementNullException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> {
                    try {
                        monde.deplacementHeros(TypeMouvement.UP);
                    } catch (MouvementNullException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 4 -> {
                    try {
                        monde.deplacementHeros(TypeMouvement.DOWN);
                    } catch (MouvementNullException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> again = false;
            }
            monde.updateData();
        } while (again);
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewManager.getInstance().setStage(stage);

        // Register the views of the game
        ViewManager.getInstance().registerView(VIEWS.HOME, new HomeView());
        ViewManager.getInstance().registerView(VIEWS.GAME, new GameView());

        // Set the default view
        ViewManager.getInstance().navigateTo(VIEWS.HOME);

        // Show window
        stage.setTitle(Donnees.GAME_TITLE);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }
}
