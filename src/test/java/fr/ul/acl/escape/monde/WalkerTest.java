package fr.ul.acl.escape.monde;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.ul.acl.escape.outils.Donnees.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WalkerTest {

    @Test
    void testAttaquer(){
        Walker w     = new Walker(0,0,1,1);
        Personnage p = new Heros(0,0,1,1);

        w.derniereAttaque = (long) (WALKER_HIT_COUNTDOWN+1);
        w.attaquer(List.of(p));

        assertEquals(HERO_HEART-WALKER_HIT, p.coeurs);
    }

}
