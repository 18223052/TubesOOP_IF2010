package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Your Game Title");
        

        final GamePanel gp = new GamePanel();
        window.add(gp);
        
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gp.startGameThread();
            }
        });
    }
}