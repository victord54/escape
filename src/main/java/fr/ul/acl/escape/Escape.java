package fr.ul.acl.escape;

import fr.ul.acl.escape.monde.Monde;
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
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    again = false;
                    break;
            }
        } while (again);
    }

    public void launchGraphic() {
    }
}
