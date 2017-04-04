package de.milke.ecost.mail;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class MeasureAddedMail {
    static Logger LOG = Logger.getLogger(MeasureAddedMail.class.getName());

    @Resource(mappedName = "java:jboss/mail/Gmail")
    Session gmailSession;

    /**
     * Default constructor.
     */
    public MeasureAddedMail() {
    }

    @Asynchronous
    public void sendEmail(String to, String from, String subject, String content) {

	LOG.info("Sending Email from " + from + " to " + to + " : " + subject);

	try {

	    Message message = new MimeMessage(gmailSession);
	    message.setFrom(new InternetAddress(from));
	    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	    message.setSubject(subject);
	    message.setText(content);
	    message.setContent("<h1>This is actual message embedded in HTML tags</h1>\n" + content
		    + "\n" + "http://localhost:8080/#page_power_verlauf", "text/html");
	    Transport.send(message);

	    LOG.info("Email was sent");

	} catch (MessagingException e) {
	    LOG.warning("Error while sending email : " + e.getMessage());
	}
    }
}
