package tv.memoryleakdeath.magentabreeze.app.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWindowListener implements WindowListener {
    private static final Logger logger = LoggerFactory.getLogger(AppWindowListener.class);
    private JFrame frame;

    public AppWindowListener(JFrame frame) {
        super();
        this.frame = frame;
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        logger.info("Application shut down complete!");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        logger.info("Shutting down application....");
        frame.dispose();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

}
