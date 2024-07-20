package tv.memoryleakdeath.magentabreeze.app.ui;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTextAreaDocumentListener implements DocumentListener {
    private static final Logger logger = LoggerFactory.getLogger(LogTextAreaDocumentListener.class);
    private static final int MAX_LOGGER_VIEW_LINES = 500;
    private JTextArea logTextArea;

    public LogTextAreaDocumentListener(JTextArea logTextArea) {
        super();
        this.logTextArea = logTextArea;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                hideOldLogEntries();
                logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
            }
        });
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    private void hideOldLogEntries() {
        Element rootElement = logTextArea.getDocument().getDefaultRootElement();
        while (rootElement.getElementCount() >= MAX_LOGGER_VIEW_LINES) {
            Element firstLogEntry = rootElement.getElement(0);
            try {
                logTextArea.getDocument().remove(0, firstLogEntry.getEndOffset());
            } catch (BadLocationException e) {
                logger.error("Unable to cull log view!", e);
            }
        }
    }
}
