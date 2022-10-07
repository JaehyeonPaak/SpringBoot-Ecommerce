package com.floyd;

import com.floyd.setting.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.parameters.P;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

public class Utility {

    public static String getSiteURL(HttpServletRequest servletRequest) {
        String siteURL = servletRequest.getRequestURL().toString();
        return siteURL.replace(servletRequest.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(settings.getHost());
        javaMailSender.setPort(Integer.parseInt(settings.getPort()));
        javaMailSender.setUsername(settings.getUsername());
        javaMailSender.setPassword(settings.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

        javaMailSender.setJavaMailProperties(mailProperties);

        return javaMailSender;
    }
}
