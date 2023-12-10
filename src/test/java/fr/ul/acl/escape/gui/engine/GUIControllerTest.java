package fr.ul.acl.escape.gui.engine;

import fr.ul.acl.escape.monde.Monde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.monde.entities.Heros;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GUIControllerTest {

    private static GUIController.MovementManager mm;
    private static Monde monde;
    private static Heros hero;

    @BeforeAll
    static void setUpAll() {
        mm = GUIController.MovementManager.instance;
    }

    @BeforeEach
    void setUp() {
        //On s'assure que l'ensemble des touches est vide au
        //commencement de chaque test
        mm.mouvements = new HashSet<>();
        monde = new Monde(5, 5);
        hero = new Heros(0, 0, 1, 1, 1, 1,1, 1, -1, 0);
        monde.addPersonnage(hero);
    }

    //Right
    @Test
    void testMovementManagerExecuteMouvementOrthogonal() {
        mm.addMouvement(TypeMouvement.RIGHT);
        mm.executerMouvement(monde, 1);

        assertEquals(hero.getX(), 1);
        assertEquals(hero.getY(), 0);

        mm.addMouvement(TypeMouvement.UP);
        mm.executerMouvement(monde, 1);

        assertEquals(hero.getX(), 1);
        assertEquals(hero.getY(), -1);
    }

    @Test
    void testMovementManagerExecuteMouvementOrthogonal3Directions() {
        mm.addMouvement(TypeMouvement.RIGHT);
        mm.addMouvement(TypeMouvement.LEFT);
        mm.addMouvement(TypeMouvement.UP);
        mm.executerMouvement(monde, 1);

        assertEquals(hero.getX(), 0);
        assertEquals(hero.getY(), -1);
    }

    @Test
    void testMovementManagerExecuteMouvementDiagonal() {
        mm.addMouvement(TypeMouvement.RIGHT);
        mm.addMouvement(TypeMouvement.UP);
        mm.executerMouvement(monde, 1);

        assertEquals(hero.getX(), 1 * GUIController.MovementManager.PIBY4);
        assertEquals(hero.getY(), -1 * GUIController.MovementManager.PIBY4);
    }

    //Outbounding
    @Test
    void testMovementManagerInstanceNotNull() {
        assertNotNull(GUIController.MovementManager.instance);
    }

    @Test
    void testMovementManagerSetVideApresMouvementExecute() {
        mm.addMouvement(TypeMouvement.RIGHT);
        mm.executerMouvement(monde, 1);

        assertEquals(0, mm.mouvements.size());
    }
}
