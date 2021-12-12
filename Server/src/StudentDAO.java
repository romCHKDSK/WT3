import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StudentDAO {

    private static final String PATH = "students.xml";
    private static Document doc;

    private List<Student> getStudents() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Student> studentsList = new ArrayList<>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(PATH);
            NodeList elementList = doc.getElementsByTagName("student");
            for (int i = 0; i < elementList.getLength(); i++) {
                Node p = elementList.item(i);
                if (p.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) p;
                    NodeList nameList = element.getChildNodes();
                    String firstname = "";
                    String lastname = "";
                    String specialty = "";
                    int age = 0;
                    int group = 0;
                    for (int j = 0; j < nameList.getLength(); j++) {
                        Node n = nameList.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            Element name = (Element) n;
                            try {
                                switch (name.getTagName()) {
                                    case ("firstname"):
                                        firstname = name.getTextContent();
                                        break;
                                    case ("lastname"):
                                        lastname = name.getTextContent();
                                        break;
                                    case ("specialty"):
                                        specialty = name.getTextContent();
                                        break;
                                    case ("age"):
                                        age = Integer.parseInt(name.getTextContent());
                                        break;
                                    case ("group"):
                                        group = Integer.parseInt(name.getTextContent());
                                        break;
                                }
                            } catch (Exception ignore){}
                        }
                    }
                    Student student = new Student(firstname, lastname, age,specialty,group);
                    studentsList.add(student);
                }
            }
            return studentsList;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return studentsList;
    }

    public String readStudentCase(String firstname, String lastname){
        String result = "";
        List<Student> students = getStudents();
        for (Student student : students){
            if (student.getFirstname().equals(firstname) && student.getLastname().equals(lastname)){
                result += student.toString();
            }
        }
        return result;
    }

    public String readAllStudentCases(){
        String result = "";
        List<Student> students = getStudents();
        for (Student student : students){
                result += student.toString() + ";";
        }
        return result;
    }

    public void addNewStudent(Student student) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(PATH);
        Element root = document.getDocumentElement();

        Element newStudent = document.createElement("student");

        Element firstname = document.createElement("firstname");
        firstname.appendChild(document.createTextNode(student.getFirstname()));
        newStudent.appendChild(firstname);

        Element lastname = document.createElement("lastname");
        lastname.appendChild(document.createTextNode(student.getLastname()));
        newStudent.appendChild(lastname);

        Element age = document.createElement("age");
        age.appendChild(document.createTextNode(String.valueOf(student.getAge())));
        newStudent.appendChild(age);

        Element specialty = document.createElement("specialty");
        specialty.appendChild(document.createTextNode(student.getSpecialty()));
        newStudent.appendChild(specialty);

        Element group = document.createElement("group");
        group.appendChild(document.createTextNode(String.valueOf(student.getGroup())));
        newStudent.appendChild(group);

        root.appendChild(newStudent);

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StreamResult result = new StreamResult(PATH);
        transformer.transform(source, result);
    }

    public boolean editStudent(Student oldStudent, Student newStudent) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        boolean isStudentFind = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(PATH);
        NodeList elementList = doc.getElementsByTagName("student");
        for (int i = 0; i < elementList.getLength(); i++) {
            Node p = elementList.item(i);
            if (p.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) p;
                NodeList nameList = element.getChildNodes();
                String firstname = "";
                String lastname = "";
                String age = "";
                for (int j = 0; j < nameList.getLength(); j++) {
                    Node n = nameList.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element name = (Element) n;
                        if (name.getTagName().equals("firstname")){
                            firstname = name.getTextContent();
                        }
                        if (name.getTagName().equals("lastname")){
                            lastname = name.getTextContent();
                        }
                        if (name.getTagName().equals("age")){
                            age = name.getTextContent();
                        }
                    }
                }
                if (oldStudent.getFirstname().equals(firstname) && oldStudent.getLastname().equals(lastname)&& age.equals(oldStudent.getAge()+"")){
                    isStudentFind = true;
                    for (int j = 0; j < nameList.getLength(); j++) {
                        Node n = nameList.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            Element name = (Element) n;
                            switch (name.getTagName()) {
                                case ("firstname"):
                                    name.setTextContent(newStudent.getFirstname());
                                    break;
                                case ("lastname"):
                                    name.setTextContent(newStudent.getLastname());
                                    break;
                                case ("specialty"):
                                    name.setTextContent(newStudent.getSpecialty());
                                    break;
                                case ("age"):
                                    name.setTextContent(String.valueOf(newStudent.getAge()));
                                    break;
                                case ("group"):
                                     name.setTextContent(String.valueOf(newStudent.getGroup()));
                                     break;
                            }
                        }
                   }
                   DOMSource source = new DOMSource(doc);

                   TransformerFactory transformerFactory = TransformerFactory.newInstance();
                   Transformer transformer = transformerFactory.newTransformer();
                   transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                   transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                   transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                   StreamResult result = new StreamResult(PATH);
                   transformer.transform(source, result);
                }
            }
        }
        return isStudentFind;
    }
}




