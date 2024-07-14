package tv.memoryleakdeath.magentabreeze.app;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Launcher {
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    @Option(name = "-installDir", usage = "specify directory to find war files to deploy", metaVar = "directory")
    private String installDir = System.getProperty("jpackage.app-path");

    @Option(name = "-?", usage = "Show this help text")
    private boolean printHelpFlag;

    private void runApplication() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            logger.error("Unable to set look and feel!", e);
        }
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Magenta Breeze - Hello!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileExitMenuItem = new JMenuItem("Exit");
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        JMenu settingsMenu = new JMenu("Settings");
        menuBar.add(settingsMenu);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpAboutMenuItem = new JMenuItem("About");
        helpMenu.add(helpAboutMenuItem);
        menuBar.add(helpMenu);
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        String warFileStatus = "War file status: %s".formatted(WarManagementUtil.isWarOk(installDir).name());
        JLabel installDirLabel = new JLabel(installDir);
        centerPanel.add(installDirLabel, BorderLayout.NORTH);

        JTextArea logTextArea = new JTextArea(LauncherWindowLogAppender.getSwingDocument(), "Log messages:\n", 25, 40);
        centerPanel.add(logTextArea, BorderLayout.CENTER);
        frame.getContentPane().add(centerPanel);

        JPanel actionPanel = new JPanel(new BorderLayout());
        frame.getContentPane().add(actionPanel, BorderLayout.SOUTH);

        JButton toggleServerStateButton = new JButton("Stop Server");
        actionPanel.add(toggleServerStateButton, BorderLayout.WEST);

        JButton openBrowserButton = new JButton("Open Browser");
        openBrowserButton.addActionListener(l -> {
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
        });
        actionPanel.add(openBrowserButton, BorderLayout.EAST);

        frame.setMinimumSize(new Dimension(800, 600));
        frame.setVisible(true);
    }

    private void parseCommandLine(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            logger.error("Invalid command line arguments!", e);
            parser.printUsage(System.out);
        }

        if (printHelpFlag) {
            System.out.println("Usage: magenta-breeze [options...]");
            parser.printUsage(System.out);
        }
    }

    private void doMain(String[] args) {
        parseCommandLine(args);

        if (printHelpFlag) {
            return;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                runApplication();
                logger.info("Launcher started!");
            }
        });
    }

    public static void main(String[] args) {
        new Launcher().doMain(args);
    }

}
