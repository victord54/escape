package fr.ul.acl.escape.monde;

public class Heros extends Personnage{

    public Heros(float x, float y, int hauteur, int largeur) {
        super(x, y, hauteur, largeur);
    }

    @Override
    public void deplacer(TypeMouvement typeMouvement){

        switch(typeMouvement){
            case RIGHT -> this.x+=vitesse;
            case LEFT -> this.x-=vitesse;
            case FORWARD -> this.y-=vitesse;
            case BACK -> this.y+=vitesse;
        }
    }

    public boolean estUnHeros(){
        return true;
    }
}
