package tv.memoryleakdeath.magentabreeze.app;

import java.net.URI;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerManager {
    private static final Logger logger = LoggerFactory.getLogger(ServerManager.class);
    private static final int SERVER_PORT = 7443;
    private static final long SERVER_STOP_TIMEOUT = 6000;

    private Server server;
    private String warFile;
    private String keystore;
    private WebAppContext webAppContext;

    public ServerManager(String warFile, String keystore) {
        this.warFile = warFile;
        this.keystore = keystore;
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

        SslContextFactory.Server sslContext = new SslContextFactory.Server();
        sslContext.setKeyStorePath(keystore);
        sslContext.setKeyStorePassword(SecureStorageUtil.getKeyValueFromSecureStorage("keystore.password"));

        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new ForwardedRequestCustomizer());
        https.addCustomizer(new SecureRequestCustomizer());

        ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContext, "http/1.1"),
                new HttpConnectionFactory(https));
        sslConnector.setPort(SERVER_PORT);
        sslConnector.setHost("localhost");
        server.setConnectors(new Connector[] { sslConnector });
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
