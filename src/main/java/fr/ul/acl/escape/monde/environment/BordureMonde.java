package fr.ul.acl.escape.monde.environment;

import fr.ul.acl.escape.gui.Sprite;

public class BordureMonde extends Terrain {
    public BordureMonde(double x, double y) {
        super(Type.NOT_SERIALIZABLE, x, y, 1, 1);
        sprite = new Sprite("assets/decors.png", 0, 0, 50, 50);
    }

    @Override
    public char getSymbol() {
        return '█';
    }

    public boolean estTraversable() {
        return false;
    }
}
