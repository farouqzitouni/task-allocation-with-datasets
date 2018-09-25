package package3;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class writeXMLFile {

	public writeXMLFile(LinkedList<String[]> all_allocations, String filePath) {
		DecimalFormat format1 = new DecimalFormat();
		format1.setMinimumIntegerDigits(3);
		format1.setMaximumIntegerDigits(3);
		format1.setMinimumFractionDigits(0);
		format1.setMaximumFractionDigits(0);
		format1.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale(".")));

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Allocations");
			doc.appendChild(rootElement);

			for (int i = 0; i < all_allocations.size(); i++) {
				Element allocation = doc.createElement("Allocation");
				rootElement.appendChild(allocation);
				
				Attr attr = doc.createAttribute("id");
				attr.setValue(String.valueOf(i + 1));
				allocation.setAttributeNode(attr);
				
				String[] values1 = all_allocations.get(i);
				
				for (int j = 0; j < values1.length; j++) {
					Element sensor = doc.createElement("SENSOR" + String.valueOf(format1.format(j + 1)));
					sensor.appendChild(doc.createTextNode(values1[j]));
					allocation.appendChild(sensor);
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath + "/allocations.xml"));

			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}