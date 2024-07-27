package tv.memoryleakdeath.magentabreeze.app;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalMessageResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExternalMessageResolver.class);
    private static final String RESOURCE_BUNDLE_NAME = "messages.messages";

    private ResourceBundle resourceBundle;
    private MessageFormat messageFormat = new MessageFormat("");

    public ExternalMessageResolver(Locale locale) {
        resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, locale);
    }

    public String getMessage(String messageKey) {
        String formattedMessage = null;
        if (messageKey != null) {
            try {
                formattedMessage = resourceBundle.getString(messageKey);
            } catch (Exception e) {
                logger.error("Unable to find external message for key: %s".formatted(messageKey), e);
            }
        }
        return formattedMessage;
    }

    public String getMessage(String messageKey, Object... arguments) {
        String formattedMessage = null;
        if (messageKey != null) {
            try {
                String rawMessage = resourceBundle.getString(messageKey);
                messageFormat.applyPattern(rawMessage);
                formattedMessage = messageFormat.format(arguments);
            } catch (Exception e) {
                logger.error("Unable to find external message for key: %s".formatted(messageKey), e);
            }
        }
        return formattedMessage;
    }
}
