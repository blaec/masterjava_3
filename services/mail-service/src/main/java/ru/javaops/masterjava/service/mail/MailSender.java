package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;

import java.util.List;

@Slf4j
public class MailSender {
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        Config mail = Configs.getConfig("mail.conf","mail");

        Email email = new SimpleEmail();
        email.setHostName(mail.getString("host"));
        email.setSmtpPort(mail.getInt("port"));
        email.setAuthenticator(new DefaultAuthenticator(mail.getString("username"), mail.getString("password")));
        email.setSSLOnConnect(mail.getBoolean("useSSL"));
        email.setSubject(subject);
        try {
            email.setFrom(mail.getString("fromName"));
            email.setMsg(body);
            email.addTo(to.get(0).getEmail(), to.get(0).getName());
            email.send();
            log.info("Sent mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
