package fr.ul.acl.escape.monde.entities;

import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.outils.FabriqueId;
import org.json.JSONObject;

public abstract class Personnage extends ElementMonde {
    protected double vitesse;
    private final int id;


    public Personnage(Type type, double x, double y, double hauteur, double largeur, double vitesse) {
        super(type, x, y, hauteur, largeur);
        this.vitesse = vitesse;
        id = FabriqueId.getInstance().getId();

    }

    public Personnage(Type type, double x, double y, double hauteur, double largeur, double vitesse, int id) {
        super(type, x, y, hauteur, largeur);
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

    @Override
    public String toString() {
        return super.toString() + "id :" + this.id;
    }

    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("id", id);
        json.put("speed", vitesse);
        return json;
    }

    public static Personnage fromJSON(JSONObject json) {
        Type type = Type.valueOf(json.getString("type"));
        if (type == Type.HERO) {
            return Heros.fromJSON(json);
        } else if (type == Type.WALKER) {
            return Walker.fromJSON(json);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
