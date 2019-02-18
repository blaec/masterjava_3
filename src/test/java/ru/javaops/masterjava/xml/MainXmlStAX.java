package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.util.*;

public class MainXmlStAX {
    public static void main(String[] args) throws IOException, XMLStreamException {
        String project = "topjava";

        String projectId = null;
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            String projectName;
            while (processor.doUntil(XMLEvent.START_ELEMENT, "Project")) {
                String id = processor.getReader().getAttributeValue(0);
                if ((projectName = processor.getElementValue("name")) != null &&
                        project.equals(projectName)) {
                    projectId = id;
                    break;
                }
            }
        }

        List<String> groups = new ArrayList<>();
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            while (processor.doUntil(XMLEvent.START_ELEMENT, "Group")) {
                if (projectId.equals(processor.getReader().getAttributeValue(0))) {
                    groups.add(Objects.requireNonNull(processor.getReader().getAttributeValue(1)));
                }
            }
        }

        List<String> userEmail = new ArrayList<>();
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            String userName;
            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                String email = processor.getReader().getAttributeValue(2);
                String userGroups = processor.getReader().getAttributeValue(3);
                if ((userName = processor.getElementValue("fullName")) != null) {
                    if (groups.stream().anyMatch(userGroups::contains)) {
                        userEmail.add(userName + "\\" + email);
                    }
                }
            }
        }
        Collections.sort(userEmail);
        System.out.println(userEmail);
    }
}
