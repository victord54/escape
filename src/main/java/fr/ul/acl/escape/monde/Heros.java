package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.TypeMouvement;

public class Heros extends Personnage {

    public Heros(float x, float y, int hauteur, int largeur, float vitesse) {
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

    public boolean estUnHeros() {
        return true;
    }
}
