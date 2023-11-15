package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.outils.FabriqueId;

public abstract class Personnage extends ElementMonde {
    protected double vitesse;
    private final int id;
    protected double coeurs;

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
        id = FabriqueId.getInstance().getId();

    }

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
        this.id = id;
    }

    /**
     * Method who moves a Personnage
     *
     * @param typeMouvement : the movement type (RIGHT, LEFT, ...)
     * @param deltaTime     : the time difference since the last iteration
     */
    public void deplacer(TypeMouvement typeMouvement, double deltaTime) {
        double vitesseTransformee = vitesse * (deltaTime >= 0 ? deltaTime : 0);
        switch (typeMouvement) {
            case RIGHT -> this.x += vitesseTransformee;
            case LEFT -> this.x -= vitesseTransformee;
            case UP -> this.y -= vitesseTransformee;
            case DOWN -> this.y += vitesseTransformee;
        }
    }

    public boolean estUnHeros() {
        return false;
    }

    public int getId() {
        return id;
    }

    public double getVitesse() {
        return vitesse;
    }

    public double getCoeurs() {
        return coeurs;
    }

    public void setCoeurs(double c) {
        coeurs = c;
    }

    /**
     * Method that reduce the number of hearts.
     *
     * @param c Number of lost hearts.
     */
    public void coeursPerdu(double c) {
        coeurs -= c;
    }

    @Override
    public String toString() {
        return super.toString() + "id :" + this.id;
    }

}
