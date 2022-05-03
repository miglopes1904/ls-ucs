package com.learningscorecard.ucs.util;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class MessageUtils {

    private static final ResourceBundleMessageSource messageSource = messageSource();

    public static String getMessage(String code) {
        String[] empty = {""};
        return messageSource.getMessage(code, empty, Locale.ENGLISH);
    }

    public static ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
