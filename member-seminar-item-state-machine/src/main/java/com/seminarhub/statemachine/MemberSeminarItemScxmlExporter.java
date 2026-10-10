package com.seminarhub.statemachine;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;

@Component
@RequiredArgsConstructor
public class MemberSeminarItemScxmlExporter {

    private static final String SCXML_NAMESPACE = "http://www.w3.org/2005/07/scxml";
    private static final String SQUIRREL_NAMESPACE = "http://squirrelframework.org/squirrel";

    private final StateMachineBuilder<
            MemberSeminarItemStateMachine,
            MemberSeminarItemStatus,
            MemberSeminarItemEvent,
            TransitionExecutionContext> stateMachineBuilder;

    public String exportScxml() {
        MemberSeminarItemStateMachine stateMachine =
                stateMachineBuilder.newStateMachine(MemberSeminarItemStatus.ORDERED);
        return toStandardScxml(stateMachine.exportXMLDefinition(true));
    }

    // Guard/action calls describe the Java model; an SCXML runtime must implement them.
    private String toStandardScxml(String squirrelXml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            Document source = factory.newDocumentBuilder().parse(
                    new InputSource(new StringReader(squirrelXml)));
            Document output = factory.newDocumentBuilder().newDocument();
            Element root = output.createElementNS(SCXML_NAMESPACE, "scxml");
            root.setAttribute("version", "1.0");
            root.setAttribute("name", MemberSeminarItemStateMachine.class.getSimpleName());
            root.setAttribute("datamodel", "ecmascript");
            root.setAttribute("initial", source.getDocumentElement().getAttribute("initial"));
            output.appendChild(root);

            Element metadata = (Element) source.getElementsByTagNameNS(
                    SQUIRREL_NAMESPACE, "fsm").item(0);
            StringBuilder comment = new StringBuilder(
                    " Visualization model: Guard and Action functions require runtime implementations.\n"
                            + "         Original metadata:\n");
            for (String attribute : new String[] {"id", "context-insensitive", "context-type",
                    "event-type", "fsm-type", "state-type"}) {
                String value = metadata.getAttribute(attribute);
                comment.append("         ").append(attribute).append(": ")
                        .append(attribute.endsWith("-type") ? simpleName(value) : value)
                        .append('\n');
            }
            root.appendChild(output.createComment(comment.append("    ").toString()));

            for (Node child = source.getDocumentElement().getFirstChild();
                    child != null; child = child.getNextSibling()) {
                if (child instanceof Element element && element != metadata) {
                    appendElement(element, root, output);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            var transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(output), new StreamResult(writer));
            return writer.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to export standard SCXML", exception);
        }
    }

    private void appendElement(Element source, Element parent, Document output) {
        if (SQUIRREL_NAMESPACE.equals(source.getNamespaceURI())
                && "action".equals(source.getLocalName())) {
            Element script = output.createElementNS(SCXML_NAMESPACE, "script");
            script.setTextContent(functionCall(source.getAttribute("content")) + ";");
            parent.appendChild(script);
            return;
        }

        if (source.hasAttributeNS(SQUIRREL_NAMESPACE, "priority")) {
            parent.appendChild(output.createComment(" Original transition priority: "
                    + source.getAttributeNS(SQUIRREL_NAMESPACE, "priority") + " "));
        }
        Element target = output.createElementNS(SCXML_NAMESPACE, source.getLocalName());
        for (int index = 0; index < source.getAttributes().getLength(); index++) {
            Node attribute = source.getAttributes().item(index);
            if (SQUIRREL_NAMESPACE.equals(attribute.getNamespaceURI())) {
                if ("type".equals(attribute.getLocalName())) {
                    target.setAttribute("type", attribute.getNodeValue().toLowerCase(Locale.ROOT));
                }
            } else if (attribute.getNamespaceURI() == null) {
                String value = attribute.getNodeValue();
                target.setAttribute(attribute.getNodeName(),
                        "cond".equals(attribute.getNodeName()) ? functionCall(value) : value);
            }
        }
        parent.appendChild(target);
        for (Node child = source.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element element) {
                appendElement(element, target, output);
            }
        }
    }

    private String functionCall(String value) {
        return value.startsWith("instance#") ? simpleName(value.substring("instance#".length())) + "()"
                : value;
    }

    private String simpleName(String className) {
        return className.substring(className.lastIndexOf('.') + 1);
    }

    public Path exportScxml(Path outputFile) throws IOException {
        String scxml = exportScxml();
        Path target = outputFile.toAbsolutePath().normalize();
        Files.createDirectories(target.getParent());
        return Files.writeString(target, scxml, StandardCharsets.UTF_8);
    }
}
