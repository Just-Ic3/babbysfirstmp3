package wavulous;

import javax.swing.JFrame;

/**
 *
 * @author Luis Padilla, Jorge Imperial, Alex Ramirez y Luis Torres.
 */
public class Wavulous {
    public static void main(String[] args) {
		PlayerX paragonX = new PlayerX();
		paragonX.setSize(416,160);
		paragonX.setVisible(true);
		paragonX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		paragonX.setLocationRelativeTo(null);
		paragonX.setResizable(false);
    }//main
}
