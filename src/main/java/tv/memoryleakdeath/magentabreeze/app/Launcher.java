package tv.memoryleakdeath.magentabreeze.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.memoryleakdeath.magentabreeze.app.ui.AboutDialog;
import tv.memoryleakdeath.magentabreeze.app.ui.AppWindowListener;
import tv.memoryleakdeath.magentabreeze.app.ui.LaunchBrowserActionListener;
import tv.memoryleakdeath.magentabreeze.app.ui.LogTextAreaDocumentListener;

public final class Launcher {
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    private static final String START_SERVER_ACTION = "startServer";
    private static final String STOP_SERVER_ACTION = "stopServer";

    @Option(name = "-warFile", usage = "specify war file to deploy", metaVar = "file")
    private String warFile = null;

    @Option(name = "-keystore", usage = "specify ssl keystore file", metaVar = "file")
    private String keystore = null;

    @Option(name = "-?", usage = "Show this help text")
    private boolean printHelpFlag;

    private ExternalMessageResolver messages;
    private ServerManager serverManager;

    private void runApplication() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            logger.error("Unable to set look and feel!", e);
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        initServerManager();
        initView();
    }

    private void initResourceBundle() {
        Locale systemLocale = Locale.getDefault();
        messages = new ExternalMessageResolver(systemLocale);
    }

    private void initServerManager() {
        if (warFile == null) {
            URL warFileUrl = getClass().getClassLoader().getResource(ApplicationConstants.WAR_FILENAME);
            if (warFileUrl == null) {
                logger.error("Unable to find WAR file for server deployment!");
            } else {
                warFile = warFileUrl.toString();
            }
        }
        if (keystore == null) {
            URL keystoreUrl = getClass().getClassLoader().getResource(ApplicationConstants.KEYSTORE_FILENAME);
            if (keystoreUrl == null) {
                logger.error("Unable to find keystore file for server ssl!");
            } else {
                keystore = keystoreUrl.toString();
            }
        }
        serverManager = new ServerManager(warFile, keystore);
    }

    private void initView() {
        JFrame frame = new JFrame(messages.getMessage("app.title"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new AppWindowListener(frame));
        frame.getContentPane().setLayout(new BorderLayout());

        // Menu bar and items
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(messages.getMessage("menu.file"));
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileExitMenuItem = new JMenuItem(messages.getMessage("menu.exit"));
        fileExitMenuItem.addActionListener(l -> {
            Toolkit.getDefaultToolkit().getSystemEventQueue()
                    .postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        fileExitMenuItem.setMnemonic(KeyEvent.VK_X);
        fileExitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);

        JMenu settingsMenu = new JMenu(messages.getMessage("menu.settings"));
        settingsMenu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(settingsMenu);

        JMenu helpMenu = new JMenu(messages.getMessage("menu.help"));
        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem helpAboutMenuItem = new JMenuItem(messages.getMessage("menu.about"));
        helpAboutMenuItem.setMnemonic(KeyEvent.VK_A);
        helpAboutMenuItem.addActionListener(l -> {
            AboutDialog dialog = new AboutDialog(frame, messages);
            dialog.setVisible(true);
        });
        helpMenu.add(helpAboutMenuItem);
        menuBar.add(helpMenu);
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);

        // Center Area
        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel installDirLabel = new JLabel(warFile);
        centerPanel.add(installDirLabel, BorderLayout.NORTH);

        // Logging area and scroll pane
        JTextArea logTextArea = new JTextArea(LauncherWindowLogAppender.getSwingDocument(), "", 25, 40);
        logTextArea.getDocument().addDocumentListener(new LogTextAreaDocumentListener(logTextArea));
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        centerPanel.add(logScrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(centerPanel);

        // Bottom button panel items
        JPanel actionPanel = new JPanel(new BorderLayout());
        frame.getContentPane().add(actionPanel, BorderLayout.SOUTH);

        JButton toggleServerStateButton = new JButton(messages.getMessage("button.startserver"));
        toggleServerStateButton.setActionCommand(START_SERVER_ACTION);
        toggleServerStateButton.addActionListener(l -> {
            if (START_SERVER_ACTION.equals(l.getActionCommand())) {
                serverManager.startServer();
                toggleServerStateButton.setActionCommand(STOP_SERVER_ACTION);
                toggleServerStateButton.setText(messages.getMessage("button.stopserver"));
            } else {
                serverManager.stopServer();
                toggleServerStateButton.setActionCommand(START_SERVER_ACTION);
                toggleServerStateButton.setText(messages.getMessage("button.startserver"));
            }
        });
        actionPanel.add(toggleServerStateButton, BorderLayout.WEST);

        JButton openBrowserButton = new JButton(messages.getMessage("button.openbrowser"));
        openBrowserButton.addActionListener(new LaunchBrowserActionListener(serverManager));
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
        initResourceBundle();
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
