package fr.ul.acl.escape.monde.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WalkerTest {

    @Test
    void testAttaquer() {
        Walker w = new Walker(0, 0, 1, 1, 1, 1,3, 3, 1, -1);
        Personnage p = new Heros(0, 0, 1, 1, 1, 1,3, 3, 1, 0,-1);
        int pvHeros = 3;
        w.attaquer(List.of(p), w.getCoolDownAttaque());

        assertEquals(pvHeros - w.degats, p.coeurs);
    }

    @Test
    void testAttaquerCooldown() {
        int pvHeros = 3;
        Walker w = new Walker(0, 0, 1, 1, 1, 1,3, 3, 1, -1);
        Personnage p = new Heros(0, 0, 1, 1, 1,1, pvHeros, pvHeros, 1, 0, -1);

        w.attaquer(List.of(p), w.getCoolDownAttaque());
        w.attaquer(List.of(p), w.getCoolDownAttaque());

        assertEquals(pvHeros - w.degats, p.coeurs);
    }
}
