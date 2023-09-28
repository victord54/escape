package fr.ul.acl.escape;

import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.TypeMouvement;

import java.util.Scanner;

public class Escape {
    private Monde monde;

    public Escape() {
        monde = new Monde();
    }

    public void launchCLI() {
        int inp;
        boolean again = true;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("1: Gauche\n2: Droite\n3: Haut\n4: Bas\n5: Quitter");
            inp = scan.nextInt();
            switch (inp) {
                case 1 -> {
                    try {
                        monde.getHeros().deplacer(TypeMouvement.LEFT);
                    } catch (MouvementNullException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 2 -> {
                    try {
                        monde.getHeros().deplacer(TypeMouvement.RIGHT);
                    } catch (MouvementNullException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> {
                    try {
                        monde.getHeros().deplacer(TypeMouvement.UP);
                    } catch (MouvementNullException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 4 -> {
                    try {
                        monde.getHeros().deplacer(TypeMouvement.DOWN);
                    } catch (MouvementNullException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> again = false;
            }
            monde.updateData();
            System.out.println(monde.getHeros().toString());
        } while (again);
    }

    public void launchGraphic() {
    }
}
