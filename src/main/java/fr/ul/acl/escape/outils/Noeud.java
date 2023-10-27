package fr.ul.acl.escape.outils;

public class Noeud {
    double x, y;

    public Noeud(double x, double y) {
        this.x = x;
        this.y = y;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Noeud{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
