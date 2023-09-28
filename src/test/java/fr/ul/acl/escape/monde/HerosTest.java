package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HerosTest {

    @Test
    void testDeplacerTypeMouvementCorrect() {
        Personnage p = new Heros(0, 0, 1, 1);

        //Right
        p.deplacer(Personnage.TypeMouvement.RIGHT);
        assertEquals(p.x, 1);
        assertEquals(p.y, 0);

        //Left
        reinitialiserCoordonnees(p);
        p.deplacer(Personnage.TypeMouvement.LEFT);
        assertEquals(p.x, -1);
        assertEquals(p.y, 0);

        //Forward
        reinitialiserCoordonnees(p);
        p.deplacer(Personnage.TypeMouvement.FORWARD);
        assertEquals(p.x, 0);
        assertEquals(p.y, -1);

        //Back
        reinitialiserCoordonnees(p);
        p.deplacer(Personnage.TypeMouvement.BACK);
        assertEquals(p.x, 0);
        assertEquals(p.y, 1);

    }

    @Test
    void testDeplacerTypeMouvementNull(){
        Personnage p = new Heros(0, 0, 1, 1);
        assertThrows(MouvementNullException.class, () -> {
            p.deplacer(null);
        });
    }

    void reinitialiserCoordonnees(Personnage p){
        p.x = 0; p.y = 0;
    }
}
