package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    private static boolean isTrue = true;
    public static void main(String[] args) {
        // Create and set up the window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Your Game Title");
        

        final GamePanel gp = new GamePanel();
        window.add(gp);
        
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        // Start the game thread after everything is visible
        // Using SwingUtilities.invokeLater ensures this happens after initial rendering
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gp.startGameThread();
            }
        });

        while(isTrue){
            System.out.println(gp.gameState);
            System.out.println(gp.isTimePaused);
        }
    }
}