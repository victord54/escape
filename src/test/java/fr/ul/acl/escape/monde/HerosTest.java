package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.TypeMouvement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HerosTest {
    Personnage p;

    @BeforeEach
    void setup() {
        p = new Heros(0, 0, 1, 1, 1);
    }

    @Test
    void testDeplacerTypeMouvementCorrect() throws MouvementNullException {


        //Right
        p.deplacer(TypeMouvement.RIGHT);
        assertEquals(p.x, p.vitesse);
        assertEquals(p.y, 0f);

        //Left
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.LEFT);
        assertEquals(p.x, -p.vitesse);
        assertEquals(p.y, 0f);

        //Forward
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.UP);
        assertEquals(p.x, 0f);
        assertEquals(p.y, -p.vitesse);

        //Back
        reinitialiserCoordonnees(p);
        p.deplacer(TypeMouvement.DOWN);
        assertEquals(p.x, 0f);
        assertEquals(p.y, p.vitesse);

    }

    @Test
    void testDeplacerTypeMouvementNull() {
        Personnage p = new Heros(0, 0, 1, 1, 1);
        assertThrows(MouvementNullException.class, () -> {
            p.deplacer(null);
        });
    }

    void reinitialiserCoordonnees(Personnage p) {
        p.x = 0;
        p.y = 0;
    }
}
