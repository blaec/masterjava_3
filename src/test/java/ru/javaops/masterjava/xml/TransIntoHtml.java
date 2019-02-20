package ru.javaops.masterjava.xml;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class TransIntoHtml {
    public static void main(String[] args) throws IOException, TransformerException {
        String project = "masterjava";

        Source xml = new StreamSource(new File("src/test/resources/payload.xml"));
        Source xslt = new StreamSource("src/test/resources/makehtml.xml");

        StringWriter sw = new StringWriter();

        FileWriter fw = new FileWriter("src/test/resources/output.html");
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer trasform = tFactory.newTransformer(xslt);
        trasform.setParameter("projectName", project);
        trasform.transform(xml, new StreamResult(sw));
        fw.write(sw.toString());
        fw.close();
    }
}
