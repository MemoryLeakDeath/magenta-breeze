package tv.memoryleakdeath.magentabreeze.app.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.memoryleakdeath.magentabreeze.app.ServerManager;

public class LaunchBrowserActionListener implements ActionListener {
    private static final Logger logger = LoggerFactory.getLogger(LaunchBrowserActionListener.class);

    private ServerManager serverManager;

    public LaunchBrowserActionListener(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    URI serverURI = serverManager.getServerUri();
                    if (serverURI != null) {
                        Desktop.getDesktop().browse(serverURI);
                        logger.info("Browser launched via Desktop object!");
                    } else {
                        logger.info("Server not started yet!");
                    }
                } else {
                    String serverURL = serverManager.getServerUrl();
                    if (serverURL != null) {
                        ProcessBuilder pb = new ProcessBuilder("xdg-open", serverURL);
                        pb.start();
                        logger.info("Browser launched via xdg-open!");
                    } else {
                        logger.info("Server not started yet!");
                    }
                }
            } catch (IOException e) {
                logger.error("Unable to launch browser!", e);
            }
        } else {
            logger.error("java.awt.Desktop is not supported on this platform!");
        }
    }
}
