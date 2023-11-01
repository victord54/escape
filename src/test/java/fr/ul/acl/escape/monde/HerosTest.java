package fr.ul.acl.escape.monde;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HerosTest {
    Heros p;

    @BeforeEach
    void setup() {
        p = new Heros(0, 0, 1, 1);
    }

    @Test
    void testDeplacerTypeMouvementCorrect() {


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
    void testDeplacerDeltaTimeCorrects() {

        p.deplacer(TypeMouvement.RIGHT, 1);
        assertEquals(p.x, p.vitesse);

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, 0.5);
        assertEquals(p.x, p.vitesse * 0.5);

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, 0.1);
        assertEquals(p.x, p.vitesse * 0.1);
    }

    @Test
    void testDeplacerDeltaTimeIncorrect() {

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, -0.5);
        assertEquals(p.x, 0);

    }


    void reinitialiserCoordonnees(Personnage p) {
        p.x = 0;
        p.y = 0;
    }
}
