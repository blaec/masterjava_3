package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }


    public static void main(String[] args) throws IOException, JAXBException {
        String projectName = "topjava";

        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());

        String projectId = Objects.requireNonNull(payload.getProjects().getProject().stream()
                .filter(p -> p.getName().equals(projectName))
                .map(Project::getId)
                .findAny().orElse(null));

        List<Group> groups = payload.getGroups().getGroup().stream()
                .filter(g -> g.getProject().getId().equals(projectId))
                .collect(Collectors.toList());

        payload.getUsers().getUser().stream()
                .filter(u -> groups.stream().anyMatch(u.getGroups()::contains))
                .sorted(Comparator.comparing(User::getFullName))
                .forEach(System.out::println);
    }
}
