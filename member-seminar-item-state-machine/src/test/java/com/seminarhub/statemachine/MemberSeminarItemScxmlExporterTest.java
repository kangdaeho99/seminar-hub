package com.seminarhub.statemachine;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.seminarhub.domain.payment.service.PaymentService;
import com.seminarhub.domain.seminar.service.MemberSeminarItemService;
import com.seminarhub.statemachine.action.FullCancelAction;
import com.seminarhub.statemachine.action.ConfirmPaymentAction;
import com.seminarhub.statemachine.action.PartialCancelAction;
import com.seminarhub.statemachine.guard.FullCancelGuard;
import com.seminarhub.statemachine.guard.ConfirmPaymentGuard;
import com.seminarhub.statemachine.guard.PartialCancelGuard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class MemberSeminarItemScxmlExporterTest {

    private static final String SCXML_NAMESPACE = "http://www.w3.org/2005/07/scxml";

    @Test
    void exportsConfiguredStateMachineToScxmlFile() throws Exception {
        PaymentService paymentService = mock(PaymentService.class);
        MemberSeminarItemService itemService = mock(MemberSeminarItemService.class);
        MemberSeminarItemStateMachineConfiguration configuration =
                new MemberSeminarItemStateMachineConfiguration(
                        new ConfirmPaymentGuard(paymentService),
                        new ConfirmPaymentAction(),
                        new FullCancelGuard(itemService),
                        new FullCancelAction(itemService),
                        new PartialCancelGuard(),
                        new PartialCancelAction(itemService));
        MemberSeminarItemScxmlExporter exporter = new MemberSeminarItemScxmlExporter(
                configuration.memberSeminarItemStateMachineBuilder());

        Path outputFile = Path.of(System.getProperty(
                "scxml.output", "build/scxml/member-seminar-item.scxml"));
        Path exportedFile = exporter.exportScxml(outputFile);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document = factory.newDocumentBuilder().parse(exportedFile.toFile());
        Element root = document.getDocumentElement();
        assertEquals("scxml", root.getLocalName());
        assertEquals(SCXML_NAMESPACE, root.getNamespaceURI());
        assertEquals("1.0", root.getAttribute("version"));
        assertEquals("MemberSeminarItemStateMachine", root.getAttribute("name"));
        assertEquals("ecmascript", root.getAttribute("datamodel"));
        assertEquals("ORDERED", root.getAttribute("initial"));

        NodeList states = document.getElementsByTagNameNS(SCXML_NAMESPACE, "state");
        Set<String> stateIds = new HashSet<>();
        for (int index = 0; index < states.getLength(); index++) {
            Element state = (Element) states.item(index);
            stateIds.add(state.getAttribute("id"));
        }
        assertEquals(3, states.getLength());
        assertEquals(Set.of("ORDERED", "PAID", "CANCELLED"), stateIds);

        Map<String, String> handlers = Map.of(
                "CONFIRM_PAYMENT", "ConfirmPayment",
                "FULL_CANCEL", "FullCancel",
                "PARTIAL_CANCEL", "PartialCancel");
        NodeList transitions = document.getElementsByTagNameNS(SCXML_NAMESPACE, "transition");
        Set<String> transitionDefinitions = new HashSet<>();
        for (int index = 0; index < transitions.getLength(); index++) {
            Element transition = (Element) transitions.item(index);
            Element source = (Element) transition.getParentNode();
            transitionDefinitions.add(source.getAttribute("id") + ":"
                    + transition.getAttribute("event") + ":"
                    + transition.getAttribute("target"));
            String handler = handlers.get(transition.getAttribute("event"));
            assertEquals(handler + "Guard()", transition.getAttribute("cond"));
            assertEquals("external", transition.getAttribute("type"));
            NodeList scripts = transition.getElementsByTagNameNS(SCXML_NAMESPACE, "script");
            assertEquals(1, scripts.getLength());
            assertEquals(handler + "Action();", scripts.item(0).getTextContent());
        }
        assertEquals(3, transitions.getLength());
        assertEquals(Set.of(
                "ORDERED:CONFIRM_PAYMENT:PAID",
                "PAID:FULL_CANCEL:CANCELLED",
                "PAID:PARTIAL_CANCEL:CANCELLED"), transitionDefinitions);
        String xml = Files.readString(exportedFile);
        assertFalse(xml.contains("sqrl:"));
        assertFalse(xml.contains("instance#"));
        assertTrue(xml.contains("Guard and Action functions require runtime implementations."));
        assertTrue(xml.contains("context-type: TransitionExecutionContext"));
        assertEquals(3, xml.split("Original transition priority: 1", -1).length - 1);
        verifyNoInteractions(paymentService, itemService);

        System.out.println("SCXML exported to: " + exportedFile);
    }
}
