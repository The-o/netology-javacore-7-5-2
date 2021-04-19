package ru.netology.pyas;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.netology.pyas.employee.Employee;

public class Main {

    public static void main(String[] args) {
        final String inFile = "data.xml";
        final String outFile = "data.json";

        List<Employee> list = parseXml(inFile);
        String json = listToJson(list);
        jsonToFile(json, outFile);
    }

    private static List<Employee> parseXml(String fileName) {
        List<Employee> result = new ArrayList<>();
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));
            
            NodeList employeeNodes = document.getElementsByTagName("employee");

            for (int i = 0; i < employeeNodes.getLength(); ++i) {
                result.add(readEmployee(employeeNodes.item(i)));
            }
        } catch(IOException|ParserConfigurationException|SAXException e) {
            e.printStackTrace();
        }        

        return result;
    }

    private static Employee readEmployee(Node node) {
        Employee result = new Employee();

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            switch (child.getNodeName()) {
                case "id":
                    result.id = Integer.parseInt(child.getTextContent());
                    break;

                case "age":
                    result.age = Integer.parseInt(child.getTextContent());
                    break;

                case "firstName":
                    result.firstName = child.getTextContent();
                    break;

                case "lastName":
                    result.lastName = child.getTextContent();
                    break;

                case "country":
                    result.country = child.getTextContent();
                    break;
            }
        }

        return result;
    }

    private static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(list, list.getClass());
    }

    private static void jsonToFile(String json, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}