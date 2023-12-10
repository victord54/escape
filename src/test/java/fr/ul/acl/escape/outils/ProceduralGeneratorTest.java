package fr.ul.acl.escape.outils;

import fr.ul.acl.escape.monde.Monde;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProceduralGeneratorTest {

    @Test
    void testGetMonde1() {
        ProceduralGenerator generator = new ProceduralGenerator(1, 1);
        Monde monde = generator.getMonde();


        //On ajoute 2 pour prendre en compte les bordures
        assertTrue(monde.getHeight() >= 8+2);
        assertTrue(monde.getHeight() <= 14+2);

        assertTrue(monde.getWidth() >= 14+2);
        assertTrue(monde.getWidth() <= 18+2);

        assertEquals(monde.getPersonnages().size(), 2);

        assertEquals(monde.getObjets().size(), 4);

        assertNotNull(monde.getHeros());
    }

    @Test
    void testGetMonde2() {
        ProceduralGenerator generator = new ProceduralGenerator(2, 2);
        Monde monde = generator.getMonde();


        //On ajoute 2 pour prendre en compte les bordures
        assertTrue(monde.getHeight() >= 8+2);
        assertTrue(monde.getHeight() <= 14+2);

        assertTrue(monde.getWidth() >= 14+2);
        assertTrue(monde.getWidth() <= 18+2);

        assertEquals(monde.getPersonnages().size(), 3);

        assertEquals(monde.getObjets().size(), 5);

        assertNotNull(monde.getHeros());
    }
}
