package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.Group;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

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

        Set<User> users = getWithJaxb(projectName);
        System.out.println(users);
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
