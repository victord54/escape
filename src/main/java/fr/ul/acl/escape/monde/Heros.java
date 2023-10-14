package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;

public class Heros extends Personnage {

    public Heros(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur, vitesse);
    }

    @Override
    public void deplacer(TypeMouvement typeMouvement) throws MouvementNullException {

        if (typeMouvement == null) throw new MouvementNullException();

        switch (typeMouvement) {
            case RIGHT -> this.x += vitesse;
            case LEFT -> this.x -= vitesse;
            case UP -> this.y -= vitesse;
            case DOWN -> this.y += vitesse;
        }
    }

    @Override
    public boolean estUnHeros() {
        return true;
    }
}
