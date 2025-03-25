import modele.jeu.Jeu;
import vuecontroleur.VueControleur;

public class Main {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        VueControleur fenetre = new VueControleur(jeu);
        fenetre.setVisible(true);
    }
}
