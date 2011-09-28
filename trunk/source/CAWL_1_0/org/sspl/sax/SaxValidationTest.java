package org.sspl.sax;

import java.io.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.xml.sax.SAXException;


public class SaxValidationTest {
	public static void main(String[] args) throws SAXException, IOException {
		String xmlFile = "CAWL.xml";
		
		// 1. Lookup a factory for the W3C XML Schema language
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		// 2. Compile the schema.
		// Here the schema is loaded from a java.io.File, but you could use
		// a java.net.URL or a javax.xml.transform.Source instead.
		File schemaLocation = new File("/root/Dropbox/workspace/uwdl/1_uWDL2_0_pre_a/CAWL_v1.0.xsd");
		Schema schema = factory.newSchema(schemaLocation);

		// 3. Get a validator from the schema.
		Validator validator = schema.newValidator();

		// 4. Parse the document you want to check.
//		Source source = new StreamSource(args[0]);
		Source source = new StreamSource(xmlFile);

		// 5. Check the document
		try {
			validator.validate(source);
//			System.out.println(args[0] + " is valid.");
			System.out.println(xmlFile + " is valid.");
		} catch (SAXException ex) {
//			System.out.println(args[0] + " is not valid because ");
			System.out.println(xmlFile + " is not valid because ");
			System.out.println(ex.getMessage());
		}

	}

}
