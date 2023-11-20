package fr.ul.acl.escape.monde.objects;

import fr.ul.acl.escape.monde.ElementMonde;
import fr.ul.acl.escape.monde.TypeMouvement;
import fr.ul.acl.escape.outils.FabriqueId;
import org.json.JSONObject;

public abstract class Objet extends ElementMonde {

    public Objet(Type type, double x, double y, double hauteur, double largeur) {
        super(type, x,y,hauteur,largeur);
    }

    public Objet(JSONObject json) {
        super(json);
    }

    public boolean estCoeur(){
        return false;
    }
}
