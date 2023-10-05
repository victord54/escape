package fr.ul.acl.escape;

import fr.ul.acl.escape.monde.Heros;
import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.TypeMouvement;
import javafx.event.Event;

import java.util.Scanner;

public class Escape {
    private final Monde monde;

    public Escape() {
        monde = new Monde();
        monde.addPersonnage(new Heros(0, 0, 1, 1, 1));
    }

    public void launchCLI() {
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

    public void launchGraphic() {
    }
}
