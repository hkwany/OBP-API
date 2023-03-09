

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailTLS {

  public static void main(String[] args) {

    final String username = "hkwany520@gmail.com";
    final String password = "bnivtgmicpdqjsco";

    Properties prop = new Properties();
//    prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
//    prop.put("mail.smtp.ssl.trust", "*");
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.transport.protocol", "smtp");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true"); //TLS

    Session session = Session.getInstance(prop,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
              }
            });

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("hkwany520@gmail.com"));
      message.setRecipients(
              Message.RecipientType.TO,
              InternetAddress.parse("hkwany520sg@gmail.com, huangk@nus.edu.sg")
      );
      message.setSubject("Testing Gmail TLS");
      message.setText("Dear Mail Crawler,"
              + "\n\n Please do not spam my email!");

      Transport.send(message);

      System.out.println("Done");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

}