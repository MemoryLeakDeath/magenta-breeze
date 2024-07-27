package tv.memoryleakdeath.magentabreeze.app.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.memoryleakdeath.magentabreeze.app.ExternalMessageResolver;

public class AboutDialog extends JDialog {
    private static final Logger logger = LoggerFactory.getLogger(AboutDialog.class);
    private static final long serialVersionUID = 1L;

    public AboutDialog(JFrame parent, ExternalMessageResolver messages) {
        super(parent, messages.getMessage("about.title"), true);
        logger.info("Showing 'about' modal...");
        JPanel panel = new JPanel(new BorderLayout());
        String aboutMessage = messages.getMessage("about.message", "0.0.1");
        JLabel label = new JLabel(aboutMessage);
        panel.add(label, BorderLayout.CENTER);
        JButton okButton = new JButton(messages.getMessage("button.ok"));
        okButton.addActionListener(l -> {
            this.dispose();
        });
        panel.add(okButton, BorderLayout.SOUTH);
        getContentPane().add(panel);
        pack();
        setSize(500, 500);
        setLocationRelativeTo(parent);
    }
}
