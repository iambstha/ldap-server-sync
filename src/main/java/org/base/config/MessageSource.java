package org.base.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

@ApplicationScoped
public class MessageSource {

    private static final Logger logger = LoggerFactory.getLogger(MessageSource.class);

    private final Properties messages = new Properties();

    @PostConstruct
    public void init() {
        loadControllerMessages("default", "en");
    }

    public void loadControllerMessages(String controller, String language) {
        String baseName = String.format("/messages/%s/messages_%s.properties", controller, language);
        try (InputStream input = getClass().getResourceAsStream(baseName)) {
            if (input != null) {
                messages.load(input);
                logger.info("Loaded messages for controller: {} in language: {}", controller, language);
            } else {
                throw new IOException("Message file not found: " + baseName);
            }
        } catch (IOException e) {
            logger.error("Error loading messages for controller: {}, language: {}. {}", controller, language, e.getMessage(), e);
        }
    }

    public String getMessage(String code) {
        return messages.getProperty(code, "Message not found: " + code);
    }

    public String getMessage(String code, Object... args) {
        String message = messages.getProperty(code, "Message not found: " + code);
        return MessageFormat.format(message, args);
    }

}
