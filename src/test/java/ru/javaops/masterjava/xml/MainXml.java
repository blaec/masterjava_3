package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.Group;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws Exception {
        String projectName = Math.random() * 100 > 50 ? "topjava" : "masterjava";

        Set<User> usersJAXB = getWithJaxb(projectName);
        System.out.println("JAXB: " + usersJAXB);

        Set<User> usersStAX = getWithStAX(projectName);
        System.out.println("StAX: " + usersStAX);

        writeUsersToHtml(usersStAX, projectName + " users");
    }

    private static void writeUsersToHtml(Set<User> users, String title) throws Exception {
        final String HTML_START = "<!DOCTYPE html><head></head><body><h1>" + title +
                                  "</h1><table border=\"1\"><tr><th>User</th><th>e-mail</th></tr>";
        final String HTML_END = "</table></body></html>";
        final String OPEN_TD = "<td>";
        final String OPEN_TR = "<tr>";
        final String CLOSE_TD = "</td>";
        final String CLOSE_TR = "</tr>";
        StringBuffer html = new StringBuffer();

        html.append(HTML_START);
        for (User user : users) {
            html.append(OPEN_TR)
                    .append(OPEN_TD).append(user.getValue()).append(CLOSE_TD)
                    .append(OPEN_TD).append(user.getEmail()).append(CLOSE_TD)
                    .append(CLOSE_TR);
        }
        html.append(HTML_END);
        FileWriter fileWriter = new FileWriter(new File("out/hw02_output.html"));
        fileWriter.write(html.toString());
        fileWriter.close();
    }

    private static Set<User> getWithStAX(String projectName) throws Exception {
        Set<User> users = new TreeSet<>(USER_COMPARATOR);

        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            List<String> groups = new ArrayList<>();
            while (processor.doUntil(XMLEvent.START_ELEMENT, "Project")) {
                if ((projectName.equals(processor.getAttribure("name")))) {
                    while (processor.doUntil(XMLEvent.START_ELEMENT, "Group", "Project")) {
                        groups.add(processor.getAttribure("name"));
                    }
                    break;
                }
            }

            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                String email = processor.getAttribure("email");
                String userGroups = processor.getAttribure("groups");
                String userName;

                if ((userName = processor.getText()) != null) {
                    if (groups.stream().anyMatch(userGroups::contains)) {
                        User user = new User();
//                        JaxbParser jaxbParser = new JaxbParser(User.class);
//                        jaxbParser.setSchema(Schemas.ofClasspath("payload.xsd"));
//                        User user = jaxbParser.unmarshal(Resources.getResource("payload.xml").openStream(), User.class);
                        user.setEmail(email);
                        user.setValue(userName);
                        users.add(user);
                    }
                }
            }
        }

        return users;
    }

    private static Set<User> getWithJaxb(String projectName) throws Exception {
        Payload payload = JAXB_PARSER.unmarshal(Resources.getResource("payload.xml").openStream());

        List<Group> groups = payload.getProjects().getProject().stream()
                .filter(p -> p.getName().equals(projectName))
                .findAny()
                .orElseThrow(NoSuchElementException::new)
                .getGroup();

        return payload.getUsers().getUser().stream()
                .filter(u -> !Collections.disjoint(groups, u.getGroups()))
                .collect(Collectors.toCollection(() -> new TreeSet<>(USER_COMPARATOR)));
    }


}
