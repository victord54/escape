package fr.ul.acl.escape.monde.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.ul.acl.escape.outils.Donnees.MONSTER_HIT_COUNTDOWN;
import static fr.ul.acl.escape.outils.Donnees.WALKER_HIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WalkerTest {

    @Test
    void testAttaquer() {
        Walker w = new Walker(0, 0, 1, 1, 1, 3, 3, 1, -1);
        Personnage p = new Heros(0, 0, 1, 1, 1, 3, 3, 1, -1);

        w.derniereAttaque = (long) (MONSTER_HIT_COUNTDOWN + 1);
        w.attaquer(List.of(p));

        assertEquals(3 - WALKER_HIT, p.coeurs);
    }

}
