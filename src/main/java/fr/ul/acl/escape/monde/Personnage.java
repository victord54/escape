package fr.ul.acl.escape.monde;

import fr.ul.acl.escape.monde.exceptions.MouvementNullException;
import fr.ul.acl.escape.outils.FabriqueId;

public abstract class Personnage extends ElementMonde {
    protected double vitesse;
    private int id;

    protected TypeMouvement lastDeplacement = TypeMouvement.DOWN;

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse) {
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
        id = FabriqueId.getInstance().getId();

    }

    public Personnage(double x, double y, double hauteur, double largeur, double vitesse, int id){
        super(x, y, hauteur, largeur);
        this.vitesse = vitesse;
        this.id = id;
    }

    /**
     * Method who moves a Character
     *
     * @param typeMouvement : the movement type (RIGHT, LEFT, ...)
     * @param deltaTime     : the time difference since the last iteration
     * @throws MouvementNullException : if movement type is null
     */
    public void deplacer(TypeMouvement typeMouvement, double deltaTime) throws MouvementNullException {
        if (typeMouvement == null) throw new MouvementNullException();

        double vitesseTransformee = vitesse * (deltaTime >= 0 ? deltaTime : 0);
        switch (typeMouvement) {
            case RIGHT -> this.x += vitesseTransformee;
            case LEFT -> this.x -= vitesseTransformee;
            case UP -> this.y -= vitesseTransformee;
            case DOWN -> this.y += vitesseTransformee;
        }

        this.lastDeplacement = typeMouvement;
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

    @Override
    public String toString() {
        return super.toString() + "id :" + this.id;
    }

    public TypeMouvement getLastDeplacement(){
        return lastDeplacement;
    }
}
