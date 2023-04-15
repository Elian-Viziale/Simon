package com.crosemont.simon.modele;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe représentant un jeu du Simon
 */

public class Jeu {

    CouleurCellule celluleRouge;
    CouleurCellule celluleVerte;
    CouleurCellule celluleBleue;
    CouleurCellule celluleJaune;
    int score;
    ArrayList<CouleurCellule> sequenceOrdinateur;

    /**
     * Constructeur du jeu initialisant tout les paramètres
     */

    public Jeu() {
        sequenceOrdinateur = new ArrayList<CouleurCellule>();
        score = 0;
        celluleBleue = CouleurCellule.BLEU;
        celluleJaune = CouleurCellule.JAUNE;
        celluleRouge = CouleurCellule.ROUGE;
        celluleVerte = CouleurCellule.VERT;
    }



    public int getScore() {
        return score;
    }

    /**
     * Méthode générant une couleur du jeu, a partir d'un chiffre obtenu aléatoirement
     * @return la couleur (CouleurCellule) alléatoire
     */

    public CouleurCellule genererCouleurAlleatoire() {
        Random rd = new Random();
        CouleurCellule celluleRetourner = null;
        int numeroCouleur = rd.nextInt(3);
        if (numeroCouleur == 0) {
            celluleRetourner = celluleRouge;
        } else if (numeroCouleur == 1) {
            celluleRetourner = celluleBleue;
        } else if (numeroCouleur == 2) {
            celluleRetourner = celluleJaune;
        } else if (numeroCouleur == 3) {
            celluleRetourner = celluleVerte;
        }
        return celluleRetourner;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Permet d'ajouter ou retirer un nombre de point au score du joueur
     * @param scoreAAjouter Le score a ajouter ou retirer (entier)
     */
    public void ajusterScore(int scoreAAjouter) {
        this.score += scoreAAjouter;
    }
}
