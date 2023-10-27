package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;

public abstract class Monstre extends Personnage {
    public Monstre(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur, vitesse);
    }

    public Monstre(double x, double y, double hauteur, double largeur, double vitesse, int id){
        super(x, y, hauteur, largeur, vitesse, id);
    }

    @Override
    public void deplacer(TypeMouvement typeMouvement, double deltaTime) throws MouvementNullException {
        if (typeMouvement == null) throw new MouvementNullException();
        int tmpX = (int) (this.x * 10000);
        int tmpY = (int) (this.y * 10000);
        int v = (int) (this.vitesse*10000);
        switch (typeMouvement) {
            case RIGHT -> tmpX += v;
            case LEFT -> tmpX -= v;
            case UP -> tmpY -= v;
            case DOWN -> tmpY += v;
        }

        this.x = (double) tmpX / 10000;
        this.y = (double) tmpY / 10000;
    }
}
