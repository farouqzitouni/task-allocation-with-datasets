package package3;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class readXMLFile {
	
	LinkedList<LinkedList<String>> list_of_allocations = new LinkedList<>();

	public readXMLFile(String filePath, int nbr_sensors) {
		DecimalFormat format1 = new DecimalFormat();
		format1.setMinimumIntegerDigits(3);
		format1.setMaximumIntegerDigits(3);
		format1.setMinimumFractionDigits(0);
		format1.setMaximumFractionDigits(0);
		format1.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));
		
		try {
			File fXmlFile = new File(filePath + "/allocations.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Allocation");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				LinkedList<String> allocation = new LinkedList<>();

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					for (int i = 0; i < nbr_sensors; i++) {
						allocation.addLast(eElement.getElementsByTagName("SENSOR" + String.valueOf(format1.format(i + 1))).item(0).getTextContent());
					}
				}
				
				list_of_allocations.addLast(allocation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LinkedList<LinkedList<String>> return_list_of_allocations() {
		return list_of_allocations;
	}
}