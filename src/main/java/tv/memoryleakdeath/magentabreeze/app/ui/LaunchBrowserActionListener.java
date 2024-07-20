package tv.memoryleakdeath.magentabreeze.app.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaunchBrowserActionListener implements ActionListener {
    private static final Logger logger = LoggerFactory.getLogger(LaunchBrowserActionListener.class);

    @Override
    public void actionPerformed(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://google.com/"));
                    logger.info("Browser launched via Desktop object!");
                } else {
                    ProcessBuilder pb = new ProcessBuilder("xdg-open", "https://google.com/");
                    pb.start();
                    logger.info("Browser launched via xdg-open!");
                }
            } catch (IOException | URISyntaxException e) {
                logger.error("Unable to launch browser!", e);
            }
        } else {
            logger.error("java.awt.Desktop is not supported on this platform!");
        }
    }
}
