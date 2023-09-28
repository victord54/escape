package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;

public class Heros extends Personnage{

    public Heros(float x, float y, int hauteur, int largeur) {
        super(x, y, hauteur, largeur);
    }

    @Override
    public void deplacer(TypeMouvement typeMouvement) throws MouvementNullException {

        if(typeMouvement == null) throw new MouvementNullException();

        switch(typeMouvement){
            case RIGHT -> this.x+=vitesse;
            case LEFT -> this.x-=vitesse;
            case FORWARD -> this.y-=vitesse;
            case BACK -> this.y+=vitesse;
        }
    }
}
