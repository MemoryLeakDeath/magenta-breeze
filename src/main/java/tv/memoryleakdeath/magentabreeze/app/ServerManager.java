package tv.memoryleakdeath.magentabreeze.app;

import java.net.URI;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerManager {
    private static final Logger logger = LoggerFactory.getLogger(ServerManager.class);
    private static final int SERVER_PORT = 7000;
    private static final long SERVER_STOP_TIMEOUT = 6000;

    private Server server;
    private String warFile;
    private WebAppContext webAppContext;

    public ServerManager(String warFile) {
        this.warFile = warFile;
        initServer();
    }

    private void initServer() {
        server = new Server();
        webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setWar(warFile);
        webAppContext.setServer(server);
        webAppContext.setConfigurationDiscovered(true);
        webAppContext.setTempDirectoryPersistent(true);
        webAppContext.setExtractWAR(true);
        HttpConfiguration http = new HttpConfiguration();
        ServerConnector httpConnector = new ServerConnector(server, new HttpConnectionFactory(http));
        httpConnector.setPort(SERVER_PORT);
        httpConnector.setHost("127.0.0.1");
        server.setConnectors(new Connector[] { httpConnector });
        server.setHandler(webAppContext);
        server.setStopAtShutdown(true);
        server.setStopTimeout(SERVER_STOP_TIMEOUT);
    }

    public void startServer() {
        try {
            logger.info("Starting Magenta Breeze server, please wait...");
            server.start();
            if (server.isStarted()) {
                logger.info("Magenta Breeze server started successfully!  Open browser to this url: {}",
                        getServerUrl());
            }
        } catch (Exception e) {
            logger.error("Unable to start Magenta Breeze server! ", e);
        }
    }

    public void stopServer() {
        try {
            logger.info("Stopping Magenta Breeze server, please wait...");
            server.stop();
            if (server.isStopped()) {
                logger.info("Magenta Breeze server stopped successfully!");
            }
        } catch (Exception e) {
            logger.error("Unable to stop Magenta Breeze server!", e);
        }
    }

    public String getServerUrl() {
        if (server != null && server.isStarted()) {
            return server.getURI().toASCIIString();
        }
        return null;
    }

    public URI getServerUri() {
        if (server != null && server.isStarted()) {
            return server.getURI();
        }
        return null;
    }
}
