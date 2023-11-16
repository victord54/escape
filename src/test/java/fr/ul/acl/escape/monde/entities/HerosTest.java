package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.TypeMouvement;
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
        assertEquals(p.getX(), p.vitesse);
        assertEquals(p.getY(), 0f);

        //Left
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.LEFT, 1);
        assertEquals(p.getX(), -p.vitesse);
        assertEquals(p.getY(), 0f);

        //Forward
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.UP, 1);
        assertEquals(p.getX(), 0f);
        assertEquals(p.getY(), -p.vitesse);

        //Back
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.DOWN, 1);
        assertEquals(p.getX(), 0f);
        assertEquals(p.getY(), p.vitesse);

    }

    @Test
    void testDeplacerDeltaTimeCorrects() {

        p.deplacer(TypeMouvement.RIGHT, 1);
        assertEquals(p.getX(), p.vitesse);

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, 0.5);
        assertEquals(p.getX(), p.vitesse * 0.5);

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, 0.1);
        assertEquals(p.getX(), p.vitesse * 0.1);
    }

    @Test
    void testDeplacerDeltaTimeIncorrect() {

        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.RIGHT, -0.5);
        assertEquals(p.getX(), 0);

    }


    void reinitialiserCoordonnees(Personnage p) {
        p.setX(0);
        p.setY(0);
    }
}
