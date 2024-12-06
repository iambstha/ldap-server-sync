package org.base.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class MessageSourceProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageSourceProducer.class);

    @Produces
    @Named("defaultMessageSource")
    public MessageSource defaultMessageSource() {
        MessageSource messageSource = new MessageSource();
        messageSource.loadControllerMessages("default", "en");
        return messageSource;
    }

    @Produces
    @Named("authMessageSource")
    public MessageSource authMessageSource() {
        MessageSource messageSource = new MessageSource();
        messageSource.loadControllerMessages("auth", "en");
        return messageSource;
    }

}
