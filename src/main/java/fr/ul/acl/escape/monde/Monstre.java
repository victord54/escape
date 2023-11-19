package fr.ul.acl.escape.monde;

import java.util.List;

import static java.lang.System.currentTimeMillis;

public abstract class Monstre extends Personnage {

    long derniereAttaque = currentTimeMillis();
    double delayAttaque;

    public Monstre(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur, vitesse);
    }

    public Monstre(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(x, y, hauteur, largeur, vitesse, id);
    }

    @Override
    public void attaquer(List<Personnage> touches) {
        if (currentTimeMillis() - derniereAttaque < delayAttaque) return;
        derniereAttaque = currentTimeMillis();
        for (Personnage p : touches) {
            p.coeursPerdu(this.degats);
        }
    }
}
