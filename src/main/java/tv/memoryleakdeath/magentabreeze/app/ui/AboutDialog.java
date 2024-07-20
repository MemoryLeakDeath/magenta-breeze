package tv.memoryleakdeath.magentabreeze.app.ui;

import java.awt.BorderLayout;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AboutDialog extends JDialog {
    private static final Logger logger = LoggerFactory.getLogger(AboutDialog.class);
    private static final long serialVersionUID = 1L;

    public AboutDialog(JFrame parent, ResourceBundle messages) {
        super(parent, messages.getString("about.title"), true);
        logger.info("Showing 'about' modal...");
        JPanel panel = new JPanel(new BorderLayout());
        String unformattedAboutMessage = messages.getString("about.message");
        MessageFormat formatter = new MessageFormat("");
        formatter.applyPattern(unformattedAboutMessage);
        String aboutMessage = formatter.format(new Object[] { "0.0.1" });
        JLabel label = new JLabel(aboutMessage);
        panel.add(label, BorderLayout.CENTER);
        JButton okButton = new JButton(messages.getString("button.ok"));
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
