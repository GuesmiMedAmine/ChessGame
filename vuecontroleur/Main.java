package vuecontroleur;

import modele.jeu.Jeu;

public class Main {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        VueControleur vue = new VueControleur(jeu);
        vue.setVisible(true);
    }
}