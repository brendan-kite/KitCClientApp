/**
 * @author Brendan Kite
 * Description: Class for automated email sending upon signup
 */

// -- Package
package com.example.kitcclientapp;

// -- Import Statements
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// -- Class Declaration
public class EmailSender {

    // -- Set the gmail host URL
    final static private String host = "smtp.gmail.com";

    // -- Sets the port
    final static private String port = "587";

    // -- Sender account information (username/email and password)
    final static private String gmailusername = "kite.in.the.clouds.22@gmail.com";
    final static private String gmailpassword = "ywdqlcivzyhwdyfs";

    // -- Method that sets the email template
    public static void sendMail(String recipient, String firstName, String verificationCode) {
        // -- Configurations for the email connection to the Google SMTP server using TLS
        Properties props = new Properties();
        props.put("mail.smtp.host", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // -- Create a session with required user details
        //    this is basically logging into the gmail account
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gmailusername, gmailpassword);
            }
        });
        try {
            //-- Creates a new Message to be sent
            MimeMessage msg = new MimeMessage(session);

            // -- Gets the internet addresses of the recipient
            InternetAddress[] address = InternetAddress.parse(recipient, true);

            // -- Sets the recipient
            msg.setRecipients(Message.RecipientType.TO, address);

            // -- Sets the subject of the email
            msg.setSubject("Welcome to KitC!");
            msg.setSentDate(new Date());

            // -- Sets the message text (body) of the email
            msg.setText("Hi " + firstName +"! \n Welcome to KitC, your own private, personal, cloud storage solution! \n We're glad to have you with us. Here's your verification code to get started: \n" + verificationCode);
            msg.setHeader("XPriority", "1");

            // -- Sends the message
            Transport.send(msg);

            // -- For testing can be removed
            System.out.println("Mail has been sent successfully");
        } catch (MessagingException e) {
            System.out.println("Unable to send an email" + e);
        }
    }
}

