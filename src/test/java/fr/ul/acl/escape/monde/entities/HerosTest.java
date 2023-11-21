package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.TypeMouvement;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.ul.acl.escape.monde.TypeMouvement.*;
import static fr.ul.acl.escape.outils.Donnees.HERO_HIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HerosTest {
    private static final double HERO_SPEED = 4;
    private static final double HERO_HEART = 3;
    private static final double WALKER_SPEED = 2;
    private static final double WALKER_HEART = 3;

    Heros p;

    @BeforeEach
    void setup() {
        p = new Heros(0, 0, 1, 1, HERO_SPEED, HERO_HEART, HERO_HEART, -1);
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
        p.deplacer(UP, 1);
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

    @Test
    void testCoeursPerdu() {
        p.coeursPerdu(0.25);
        assertEquals(p.coeurs, 2.75);
        p.coeursPerdu(1.0);
        assertEquals(p.coeurs, 1.75);
        p.coeursPerdu(1.75);
        assertEquals(p.coeurs, 0);
    }

    @Test
    void testAttaquer() {
        Walker w = new Walker(1, 1, 1, 1, WALKER_SPEED, WALKER_HEART, WALKER_HEART, -1);
        p.attaquer(List.of(w));

        assertEquals(w.getCoeurs(), WALKER_HEART - HERO_HIT);
    }

    @Test
    void getHitBoxCollision() {
        Rectangle2D rectVoulu = new Rectangle2D(0, 0, 1, 1);

        Rectangle2D hitbox = p.getHitBoxCollision();

        assertEquals(hitbox, rectVoulu);
    }

    @Test
    void getHitBoxAttaque() {
        Rectangle2D rectVoulu = new Rectangle2D(0 + p.getLargeur(), 0, 1, 1);
        p.setOrientation(RIGHT);

        Rectangle2D hitbox = p.getHitBoxAttaque();
        assertEquals(hitbox, rectVoulu);


        rectVoulu = new Rectangle2D(0 - p.getLargeur(), 0, 1, 1);
        p.setOrientation(LEFT);

        hitbox = p.getHitBoxAttaque();
        assertEquals(hitbox, rectVoulu);


        rectVoulu = new Rectangle2D(0, 0 - p.getHauteur(), 1, 1);
        p.setOrientation(UP);

        hitbox = p.getHitBoxAttaque();
        assertEquals(hitbox, rectVoulu);


        rectVoulu = new Rectangle2D(0, 0 + p.getHauteur(), 1, 1);
        p.setOrientation(DOWN);

        hitbox = p.getHitBoxAttaque();
        assertEquals(hitbox, rectVoulu);
    }

    void reinitialiserCoordonnees(Personnage p) {
        p.setX(0);
        p.setY(0);
    }

    @Test
    void coeursGagneDejaFull(){
        p.coeursGagne(1);
        assertEquals(p.getCoeurs(), WALKER_HEART);

    }

    @Test
    void coeursGagneSuiteA1Ramassage(){
        p.coeursPerdu(1);
        p.coeursGagne(1);
        assertEquals(p.getCoeurs(), WALKER_HEART);
    }

    @Test
    void coeursGagneLorsquePasUneVieEntierePerdue(){
        p.coeursPerdu(0.25);
        p.coeursGagne(1);
        assertEquals(p.getCoeurs(),HERO_HEART);
    }
}
