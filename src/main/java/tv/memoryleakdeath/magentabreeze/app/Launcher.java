package tv.memoryleakdeath.magentabreeze.app;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Launcher {
    private Launcher() {
    }

    private static void runApplication() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Magenta Breeze - Hello!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel helloLabel = new JLabel("Hello World!");
        frame.getContentPane().add(helloLabel);

        frame.setMinimumSize(new Dimension(800, 600));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                runApplication();
            }
        });
    }
}
