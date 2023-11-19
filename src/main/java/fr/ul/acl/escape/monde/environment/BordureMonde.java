package fr.ul.acl.escape.monde.environment;

public class BordureMonde extends Terrain {
    public BordureMonde(double x, double y) {
        super(Type.NOT_SERIALIZABLE, x, y, 1, 1);
    }

    @Override
    public char getSymbol() {
        return 'X';
    }

    public boolean estTraversable() {
        return false;
    }
}
