package tv.memoryleakdeath.magentabreeze.app;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

public class LauncherWindowLogAppender extends AppenderBase<ILoggingEvent> {

    private Layout<ILoggingEvent> layout;
    private static Document swingDocument = new PlainDocument();

    @Override
    protected void append(ILoggingEvent eventObject) {
        String loggingEvent = this.layout.doLayout(eventObject);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    swingDocument.insertString(swingDocument.getLength(), loggingEvent, null);
                } catch (BadLocationException e) {
                    addError("Unable to append to end of swing document!");
                }
            }
        });
    }

    @Override
    public void start() {
        if (this.layout == null) {
            addError("No layout set for logger with name [%s]".formatted(name));
            return;
        }
        super.start();
    }

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    public static Document getSwingDocument() {
        return swingDocument;
    }

}
