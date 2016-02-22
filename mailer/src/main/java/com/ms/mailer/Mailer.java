/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.mailer;

import com.ms.enums.MailerApp;
import static com.ms.mailer.Main.mailServerProperties;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author mySolutions
 */
public class Mailer {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    static MimeMultipart generateMailMultipart;

    public static void sendSMTPEmail(List<String> receivers, MailerApp aplication, String content, String subject, List<String> pathAttached) throws AddressException, MessagingException {
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);

        if (receivers != null) {
            for (String receiver : receivers) {
                generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            }
        }
        generateMailMessage.setSubject(subject);

        MimeMultipart multiPart = new MimeMultipart();
        BodyPart adjunto = new MimeBodyPart();
        if (pathAttached != null) {
            for (String attach : pathAttached) {
                if (!attach.equalsIgnoreCase("")) {
                    adjunto = new MimeBodyPart();
                    FileDataSource fileDataSource = new FileDataSource(attach);
                    adjunto.setDataHandler(new DataHandler(fileDataSource));
                    adjunto.setFileName(fileDataSource.getName());
                    multiPart.addBodyPart(adjunto);
                }
            }
        }

        BodyPart text = new MimeBodyPart();
        text.setContent(aplication.getTopTemplate() + content + aplication.getBottomTemplate(), "text/html");

        multiPart.addBodyPart(text);

        generateMailMessage.setContent(multiPart);
        Transport transport = getMailSession.getTransport("smtp");
        transport.connect(aplication.getProvider(), aplication.getUser(), aplication.getPassword());
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}
