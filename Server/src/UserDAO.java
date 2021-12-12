import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class UserDAO {

    public User getUser(String checkLogin, String checkPassword) {
        try {
            File xmlFile = new File("users.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nNode = nodeList.item(i);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    Node node1 = elem.getElementsByTagName("login").item(0);
                    String login = node1.getTextContent();

                    if (login.equals(checkLogin)) {

                        Node node2 = elem.getElementsByTagName("password").item(0);
                        String password = node2.getTextContent();

                        if (password.equals(checkPassword)) {

                            Node node3 = elem.getElementsByTagName("permission").item(0);
                            String permission = node3.getTextContent();

                            return new User(login, password, permission);

                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
