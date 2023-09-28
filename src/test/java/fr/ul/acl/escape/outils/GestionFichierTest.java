package fr.ul.acl.escape.outils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestionFichierTest {

    @Test
    void lireFichierCarte() {
        char [][] temoin = {
                {'A','B','C','D','E','F','G','H','I','J','K','L','1',},
                {'0','0','0','0','0','0','0','0','0','0','0','0','2',},
                {'0','0','0','0','0','0','0','0','0','0','0','0','3',},
                {'0','0','0','0','0','0','0','0','0','0','0','0','4',},
                {'0','0','0','0','0','0','0','0','0','0','0','0','5',},
                {'0','0','0','0','0','0','0','0','0','0','0','0','6',},
                {'0','0','0','0','0','0','0','0','0','0','0','0','7',}
        };

        char [][] test = GestionFichier.lireFichierCarte("exemple");
        assertArrayEquals(temoin,test);


    }
}
