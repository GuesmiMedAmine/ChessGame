import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observer;
import java.util.Observable;
import vuecontroleur.Deco;

class Mafenetre extends JFrame implements Observer {

    @Override
    public void update(Observable o, Object arg) {
    }

    public void build() {
        setSize(400, 400);

        Modele m = new Modele();

        JPanel jp = new JPanel(new GridLayout(8, 8));
        setContentPane(jp);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel jl = new JLabel();
                jl.setOpaque(true);
                jl.setBackground(Deco.getCouleurCase(i, j));
                final int ii =i;
                final int jj =j;
                jl.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //System.out.println(ii+jj);
                        m.i =ii;
                        m.j =jj;
                    }
                });
                jp.add(jl);
            }
        }

        // Rendre la fenêtre visible
        setVisible(true);
    }

}