package fr.ul.acl.escape.monde;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MondeTest {
    private Monde monde;
    @BeforeEach
    void setUp() {
        this.monde = new Monde();
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
    void redeplacementSiCollision(){
        Mur mur = new Mur(10,10,10,10);

        Heros e1 = new Heros(12,8,4,4);
        monde.redeplacementSiCollision(e1,mur); // Collision en bas de e1 : x inchangé - mur.y - heros.hauteur
        assertEquals( 12, e1.getX() );
        assertEquals( 6, e1.getY());


        e1 = new Heros(18,14, 4,4);
        monde.redeplacementSiCollision(e1,mur); // Collision à gauche de e1 : mur.x + mur.largeur - y inchangé
        assertEquals( 20, e1.getX() );
        assertEquals( 14, e1.getY());

        e1 = new Heros(12,18, 4,4);
        monde.redeplacementSiCollision(e1,mur); // Collision en haut de e1 - x inchangé - mur.y + mur.hauteur
        assertEquals( 12, e1.getX() );
        assertEquals( 20, e1.getY());

        e1 = new Heros(8,12, 4,4);
        monde.redeplacementSiCollision(e1,mur); // Collision à droite de e1 : mur.x - heros.largeur - y inchangé
        assertEquals( 6, e1.getX() );
        assertEquals( 12, e1.getY());
    }
}