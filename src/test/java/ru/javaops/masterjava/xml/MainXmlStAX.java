package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MainXmlStAX {
    public static void main(String[] args) throws IOException, XMLStreamException {
        String project = "topjava";
        final String DELIMITER = "-";

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
                        userEmail.add(userName + DELIMITER + email);
                    }
                }
            }
        }
        Collections.sort(userEmail);
        System.out.println(userEmail);

        final String HTML_START = "<!DOCTYPE html><head></head><body><table border=\"1\"><tr><th>User</th><th>e-mail</th></tr>";
        final String HTML_END = "</table></body></html>";
        final String OPEN_TD = "<td>";
        final String OPEN_TR = "<tr>";
        final String CLOSE_TD = "</td>";
        final String CLOSE_TR = "</tr>";
        StringBuffer html = new StringBuffer();
        html.append(HTML_START);
        for (String user : userEmail) {
            String[] split = user.split(DELIMITER);
            html.append(OPEN_TR)
                    .append(OPEN_TD).append(split[0]).append(CLOSE_TD)
                    .append(OPEN_TD).append(split[1]).append(CLOSE_TD)
                    .append(CLOSE_TR);
        }
        html.append(HTML_END);
        FileWriter fileWriter = new FileWriter(new File("src/test/resources/output.html"));
        fileWriter.write(html.toString());
        fileWriter.close();
        System.out.println(html.toString());
    }
}
