package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.web.WsClient;

import javax.xml.namespace.QName;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Slf4j
public class MailWSClient {
    private static final WsClient<MailService> WS_CLIENT;
    private static final String CONFIG_PATH = "/Users/blaec/Documents/javaProjects/apps/masterjava/config/";

    static {
        URL url = null;
        try {
            url = new File(CONFIG_PATH + "wsdl/mailService.wsdl").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        WS_CLIENT = new WsClient<>(url,
                new QName("http://mail.javaops.ru/", "MailServiceImplService"),
                MailService.class);

        WS_CLIENT.init("mail", "/mail/mailService?wsdl");
    }


    public static void sendToGroup(final Set<Addressee> to, final Set<Addressee> cc, final String subject, final String body) {
        log.info("Send mail to '" + to + "' cc '" + cc + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        WS_CLIENT.getPort().sendToGroup(to, cc, subject, body);
    }
}
