package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HerosTest {
    Personnage p;

    @BeforeEach
    void setup() {
        p = new Heros(0, 0, 1, 1, 1);
    }

    @Test
    void testDeplacerTypeMouvementCorrect() throws MouvementNullException {


        //Right
        p.deplacer(TypeMouvement.RIGHT, 1);
        assertEquals(p.x, p.vitesse);
        assertEquals(p.y, 0f);

        //Left
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.LEFT, 1);
        assertEquals(p.x, -p.vitesse);
        assertEquals(p.y, 0f);

        //Forward
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.UP, 1);
        assertEquals(p.x, 0f);
        assertEquals(p.y, -p.vitesse);

        //Back
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.DOWN, 1);
        assertEquals(p.x, 0f);
        assertEquals(p.y, p.vitesse);

    }

    @Test
    void testDeplacerDeltaTimeCorrects() throws MouvementNullException {

        p.deplacer(TypeMouvement.RIGHT, 1);
        assertEquals(p.x, p.vitesse);

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, 0.5);
        assertEquals(p.x, p.vitesse*0.5);

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, 0.1);
        assertEquals(p.x, p.vitesse*0.1);
    }

    @Test
    void testDeplacerDeltaTimeIncorrect() throws MouvementNullException {

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, -0.5);
        assertEquals(p.x, 0);

    }



    void reinitialiserCoordonnees(Personnage p) {
        p.x = 0;
        p.y = 0;
    }
}
