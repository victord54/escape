package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MondeTest {
    private Monde monde;
    private Monde monde2;
    @BeforeEach
    void setUp() {
        this.monde = new Monde();
        this.monde2 = new Monde();
    }

    @Test
    void collision() {
        Heros e1 = new Heros(25,25, 5,5);
        Mur e2 = new Mur(15,15,2,2); // Mur en dehors de e1
        Mur e3 = new Mur(20,25,10,10); // Mur collision à gauche de e1
        Mur e4 = new Mur(28,25,10,10); // Mur collision à droite de e1
        Mur e5 = new Mur(25,20,10,10); // Mur collision au dessus de e1
        Mur e6 = new Mur(25,28,10,10); // Mur collision en dessous de e1
        Mur e7 = new Mur(26,26,1,1); // Mur à l'intérieur de e1

        assertFalse(monde.collision(e1,e2)); // Pas de collision
        assertTrue(monde.collision(e1,e3));
        assertTrue(monde.collision(e1,e4));
        assertTrue(monde.collision(e1,e5));
        assertTrue(monde.collision(e1,e6));
        assertTrue(monde.collision(e1,e7));

    }

    @Test
    void gestionCollisions() {
    }

    @Test
    void deplacementHeros() throws MouvementNullException {
        Heros h = new Heros(5,5,1,1);
        monde.addPersonnage(h);
        monde.deplacementHeros(Personnage.TypeMouvement.LEFT);
        assertEquals(4, monde.getHeros().getX());
        assertEquals(5, monde.getHeros().getY());

        monde.deplacementHeros(Personnage.TypeMouvement.RIGHT);
        assertEquals(5, monde.getHeros().getX());
        assertEquals(5, monde.getHeros().getY());

        monde.deplacementHeros(Personnage.TypeMouvement.FORWARD);
        assertEquals(5, monde.getHeros().getX());
        assertEquals(4, monde.getHeros().getY());

        monde.deplacementHeros(Personnage.TypeMouvement.BACK);
        assertEquals(5, monde.getHeros().getX());
        assertEquals(5, monde.getHeros().getY());


        Mur m1 = new Mur(2,2,2,2);
        monde2.addTerains(m1);
        Mur m2 = new Mur(5,2,2,2);
        monde2.addTerains(m2);

        Heros e1 = new Heros(4,2,1,1);
        monde2.addPersonnage(e1);

        Walker w1 = new Walker(4,1,1,1);
        monde2.addPersonnage(w1);
        Walker w2 = new Walker(4,3,1,1);
        monde2.addPersonnage(w2);

        monde2.deplacementHeros(Personnage.TypeMouvement.LEFT); // Deplacement à gauche impossible car m1
        assertEquals(4, monde2.getHeros().getX());
        assertEquals(2, monde2.getHeros().getY());


        monde2.deplacementHeros(Personnage.TypeMouvement.RIGHT); // Deplacement à gauche impossible car m2
        assertEquals(4, monde2.getHeros().getX());
        assertEquals(2, monde2.getHeros().getY());

        monde2.deplacementHeros(Personnage.TypeMouvement.FORWARD); // Deplacement à gauche impossible car w1
        assertEquals(4, monde2.getHeros().getX());
        assertEquals(2, monde2.getHeros().getY());

        monde2.deplacementHeros(Personnage.TypeMouvement.BACK); // Deplacement à gauche impossible car w2
        assertEquals(4, monde2.getHeros().getX());
        assertEquals(2, monde2.getHeros().getY());

    }

    @Test
    void deplacementHerosNull(){
        assertThrows(MouvementNullException.class, () -> monde.deplacementHeros(null));
    }
}
