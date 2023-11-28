package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.entities.Heros;
import fr.ul.acl.escape.monde.entities.Walker;
import fr.ul.acl.escape.monde.environment.Mur;
import fr.ul.acl.escape.monde.objects.Coeur;
import fr.ul.acl.escape.monde.objects.Piege;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static fr.ul.acl.escape.outils.Donnees.HERO_HIT;
import static fr.ul.acl.escape.outils.Donnees.WALKER_HIT;
import static org.junit.jupiter.api.Assertions.*;

class MondeTest {
    private static final double HERO_SPEED = 4;
    private static final double HERO_HEART = 3;
    private static final double WALKER_SPEED = 2;
    private static final double WALKER_HEART = 3;

    private Monde monde;
    private Monde monde2;

    @BeforeEach
    void setUp() {
        this.monde = new Monde(12, 18);
        this.monde2 = new Monde(12, 18);
    }

    @Test
    void collision() {
        Heros e1 = new Heros(25, 25, 5, 5, HERO_SPEED, HERO_HEART, HERO_HEART, HERO_HIT, -1);
        Mur e2 = new Mur(15, 15, 2, 2); // Mur en dehors de e1
        Mur e3 = new Mur(20, 25, 10, 10); // Mur collision à gauche de e1
        Mur e4 = new Mur(28, 25, 10, 10); // Mur collision à droite de e1
        Mur e5 = new Mur(25, 20, 10, 10); // Mur collision au-dessus de e1
        Mur e6 = new Mur(25, 28, 10, 10); // Mur collision en dessous de e1
        Mur e7 = new Mur(26, 26, 1, 1); // Mur à l'intérieur de e1

        assertFalse(monde.collision(e1, e2)); // Pas de collision
        assertTrue(monde.collision(e1, e3));
        assertTrue(monde.collision(e1, e4));
        assertTrue(monde.collision(e1, e5));
        assertTrue(monde.collision(e1, e6));
        assertTrue(monde.collision(e1, e7));

    }

    @Test
    void deplacementHeros() {
        Heros h = new Heros(5, 5, 1, 1, HERO_SPEED, HERO_HEART, HERO_HEART, HERO_HIT, -1);
        monde.addPersonnage(h);
        monde.deplacementHeros(TypeMouvement.LEFT, 1);
        assertEquals(5 - HERO_SPEED, monde.getHeros().getX(), "T1");
        assertEquals(5, monde.getHeros().getY(), "T2");

        monde.deplacementHeros(TypeMouvement.RIGHT, 1);
        assertEquals(5, monde.getHeros().getX(), "T3");
        assertEquals(5, monde.getHeros().getY(), "T4");

        monde.deplacementHeros(TypeMouvement.UP, 1);
        assertEquals(5, monde.getHeros().getX(), "T5");
        assertEquals(5 - HERO_SPEED, monde.getHeros().getY(), "T6");

        monde.deplacementHeros(TypeMouvement.DOWN, 1);
        assertEquals(5, monde.getHeros().getX(), "T7");
        assertEquals(5, monde.getHeros().getY(), "T9");

        //DeltaTime différent
        monde.deplacementHeros(TypeMouvement.DOWN, 0.5);
        assertEquals(5, monde.getHeros().getX(), "T7");
        assertEquals(5 + (HERO_SPEED * 0.5), monde.getHeros().getY(), "T9");


        Mur m1 = new Mur(2, 2, 2, 2);
        monde2.addTerrains(m1);
        Mur m2 = new Mur(5, 2, 2, 2);
        monde2.addTerrains(m2);

        Heros e1 = new Heros(4, 2, 1, 1, HERO_SPEED, HERO_HEART, HERO_HEART, HERO_HIT, -1);
        monde2.addPersonnage(e1);

        Walker w1 = new Walker(4, 1, 1, 1, WALKER_SPEED, WALKER_HEART, WALKER_HEART, WALKER_HIT, -1);
        monde2.addPersonnage(w1);
        Walker w2 = new Walker(4, 3, 1, 1, WALKER_SPEED, WALKER_HEART, WALKER_HEART, WALKER_HIT, -1);
        monde2.addPersonnage(w2);


        //On utilise un petit deltaTime car c'est le cas pratique (le système est prévu pour fonctionner qu'à petite vitesse pour l'instant)
        //Lorsque le personnage va trop vite, il peut traverser les murs.
        monde2.deplacementHeros(TypeMouvement.LEFT, 0.1); // Deplacement à gauche impossible car m1
        assertEquals(4, monde2.getHeros().getX());
        assertEquals(2, monde2.getHeros().getY());


        monde2.deplacementHeros(TypeMouvement.RIGHT, 0.1); // Deplacement à gauche impossible car m2
        assertEquals(4, monde2.getHeros().getX());
        assertEquals(2, monde2.getHeros().getY());

        monde2.deplacementHeros(TypeMouvement.DOWN, 0.1); // Deplacement à gauche impossible car w1
        assertEquals(4, monde2.getHeros().getX());
        assertEquals(2, monde2.getHeros().getY());

        monde2.deplacementHeros(TypeMouvement.UP, 0.1); // Deplacement à gauche impossible car w2
        assertEquals(4, monde2.getHeros().getX());
        assertEquals(2, monde2.getHeros().getY());

    }

    @Test
    void deplacementMonstre() {
        Walker w = new Walker(1, 1, 1, 1, WALKER_SPEED, WALKER_HEART, WALKER_HEART, WALKER_HIT, -1);
        System.out.println(w);
        Heros h = new Heros(6, 6, 1, 1, HERO_SPEED, HERO_HEART, HERO_HEART, HERO_HIT, -1);
        monde.addPersonnage(w);
        monde.addPersonnage(h);
        System.out.println(w);
        monde.deplacementMonstre(w, 1);
        System.out.println(w);
    }

    @Test
    void deplacementMonstres() {
        Walker w = new Walker(1, 1, 1, 1, WALKER_SPEED, WALKER_HEART, WALKER_HEART, WALKER_HIT, -1);
        Heros h = new Heros(6, 6, 1, 1, HERO_SPEED, HERO_HEART, HERO_HEART, HERO_HIT, -1);
        monde.addPersonnage(w);
        monde.addPersonnage(h);

        System.out.println(w);
        System.out.println("-----------");
        monde.deplacementMonstres(1);
        System.out.println("-----------");
        System.out.println(w);
    }

    @Test
    void intLePlusProche() {
        assertEquals(1500, monde.intLePlusProche(1004, 500));
        assertEquals(1500, monde.intLePlusProche(1350, 500));
        assertEquals(500, monde.intLePlusProche(500, 500));
    }

    @Test
    void heroRamassageObjet() {
        Heros h = new Heros(6, 6, 1, 1, HERO_SPEED, HERO_HEART, HERO_HEART, HERO_HIT, -1);
        Coeur c = new Coeur(6, 6, 0.2, 0.2, 1);
        monde.addPersonnage(h);
        monde.addObjet(c);

        monde.heroRamassageObjet();

        assertEquals(monde.getObjets().size(), 0);

        monde.addObjet(c);
        monde.addObjet(c);

        monde.heroRamassageObjet();
        assertEquals(monde.getObjets().size(), 1);

        Coeur c2 = new Coeur(8, 8, 0.2, 0.2, 1);
        monde.addObjet(c2);
        monde.heroRamassageObjet();
        assertEquals(monde.getObjets().size(), 1);
        monde.heroRamassageObjet();
        assertEquals(monde.getObjets().size(), 1);
    }

    @Test
    void coeurSurPiegeRamassageObjet() {
        Heros h = new Heros(6, 6, 1, 1, HERO_SPEED, HERO_HEART, HERO_HEART, HERO_HIT, -1);
        Piege p = new Piege(6, 6, 0.2, 0.2, 1);
        Coeur c = new Coeur(6, 6, 0.2, 0.2, 1);
        monde.addObjet(p);
        monde.addObjet(c);

        monde.addPersonnage(h);

        monde.heroRamassageObjet();
        assertEquals(1, monde.getObjets().size());
    }
}
