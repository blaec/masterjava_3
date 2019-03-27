package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.DBITestProvider;
import ru.javaops.masterjava.service.dao.Message;
import ru.javaops.masterjava.service.dao.MessageDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MailSender {
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        Config db = Configs.getConfig("persist.conf", "db");
        DBITestProvider.initDBI(db.getString("url"), db.getString("user"), db.getString("password"));
        MessageDao dao = DBIProvider.getDao(MessageDao.class);

        Config mail = Configs.getConfig("mail.conf", "mail");

        Email email = new SimpleEmail();
        email.setHostName(mail.getString("host"));
        email.setSmtpPort(mail.getInt("port"));
        email.setAuthenticator(new DefaultAuthenticator(mail.getString("username"), mail.getString("password")));
        email.setSSLOnConnect(mail.getBoolean("useSSL"));
        email.setSubject(subject);
        try {
            email.setFrom(mail.getString("fromName"));
            email.setMsg(body);
            if (to.size() > 0) {
                email.addTo(toVarargs(to));
            }
            if (cc.size() > 0) {
                email.addCc(toVarargs(cc));
            }
            email.send();
            log.info("Sent mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
            Message message = new Message(emailToString(to), emailToString(cc), subject, body, LocalDateTime.now());
            DBIProvider.getDBI().useTransaction((conn, status) -> {
                dao.insert(message);
            });
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    private static String[] toVarargs(List<Addressee> emails) {
        return emails.stream()
                .map(Addressee::getEmail)
                .collect(Collectors.toList())
                .toArray(new String[emails.size()]);
    }

    private static String emailToString(List<Addressee> emails) {
        return emails.stream()
                .map(e -> e.getName() + " <" + e.getEmail() + ">")
                .collect(Collectors.joining("; "));
    }
}
