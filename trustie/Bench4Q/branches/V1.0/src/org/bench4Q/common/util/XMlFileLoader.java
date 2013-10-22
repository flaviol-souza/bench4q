package org.bench4Q.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.TestPhase;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMlFileLoader {

	private static final String xml = "./bench4Q.xml";
	private static final String schema = "org/bench4Q/resources/bench4Q-schema.xsd";
	private SAXBuilder builder;
	private Document doc;
	private File XMLFile;
	private Args args;

	public XMlFileLoader() {
		try {
			builder = new SAXBuilder(true);
			builder.setFeature("http://apache.org/xml/features/validation/schema", true);
			builder.setProperty(
					"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
					 getClass().getClassLoader().getResource(schema).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		File xmlFile = new File(xml);
		if (!xmlFile.exists()) {
			try {
				xmlFile.createNewFile();
				doc = new Document();
				args = new Args();
				initXMLFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				doc = builder.build(new FileReader(xmlFile));
				initRBEWithXMLFile();
			} catch (JDOMException e) {
				System.out.println("XML read error or XML fomat error...");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void initXMLFile() {
		Element root = new Element("bench4Q");
		doc.setRootElement(root);
		
		Element testName = new Element("testName").setText( args.getTestName() );
		root.addContent(testName);
		Element testDescription = new Element("testDescription").setText( args.getTestDescription() );
		root.addContent(testDescription);
		
		Element rbe = new Element("rbe");
		root.addContent(rbe);
		rbe.setAttribute(new Attribute("rbetype", "closed"));
		Element interval = new Element("interval").setText(String.valueOf(args.getInterval()));
		rbe.addContent(interval);
		Element prepair = new Element("prepair").setText(String.valueOf(args.getPrepair()));
		rbe.addContent(prepair);
		Element cooldown = new Element("cooldown").setText(String.valueOf(args.getCooldown()));
		rbe.addContent(cooldown);
		Element out = new Element("out").setText(args.getOut());
		rbe.addContent(out);
		Element tolerance = new Element("tolerance").setText(String.valueOf(args.getTolerance()));
		rbe.addContent(tolerance);
		Element retry = new Element("retry").setText(String.valueOf(args.getRetry()));
		rbe.addContent(retry);
		Element thinktime = new Element("thinktime").setText(String.valueOf(args.getThinktime()));
		rbe.addContent(thinktime);
		Element urlConnectionTimeOut = new Element("urlConnectionTimeOut").setText(String
				.valueOf(args.getUrlConnectionTimeOut()));
		rbe.addContent(urlConnectionTimeOut);
		Element urlReadTimeOut = new Element("urlReadTimeOut").setText(String.valueOf(args
				.getUrlReadTimeOut()));
		rbe.addContent(urlReadTimeOut);
		Element mix = new Element("mix").setText(args.getMix());
		rbe.addContent(mix);
		Element slow = new Element("slow").setText(String.valueOf(args.getSlow()));
		rbe.addContent(slow);
		Element getImage = new Element("getImage").setText(String.valueOf(args.isGetImage()));
		rbe.addContent(getImage);
		Element baseURL = new Element("baseURL").setText(args.getBaseURL());
		rbe.addContent(baseURL);
		if (args.getEbs().isEmpty()) {
			Element ebs = new Element("ebs");
			rbe.addContent(ebs);
			Element baseLoad = new Element("baseLoad").setText("0");
			ebs.addContent(baseLoad);
			Element randomLoad = new Element("randomLoad").setText("0");
			ebs.addContent(randomLoad);
			
			Element rate = new Element("rate").setText("0");
			ebs.addContent(rate);
			Element triggerTime = new Element("triggerTime").setText("0");
			ebs.addContent(triggerTime);
			Element stdyTime = new Element("stdyTime").setText("0");
			ebs.addContent(stdyTime);
		} else {
			for (TestPhase testPhase : args.getEbs()) {
				Element ebs = new Element("ebs");
				rbe.addContent(ebs);
				Element baseLoad = new Element("baseLoad").setText(String.valueOf(testPhase.getBaseLoad()));
				ebs.addContent(baseLoad);
				Element randomLoad = new Element("randomLoad").setText(String.valueOf(testPhase.getRandomLoad()));
				ebs.addContent(randomLoad);
				Element rate = new Element("rate").setText(String.valueOf(testPhase.getRate()));
				ebs.addContent(rate);
				Element triggerTime = new Element("triggerTime").setText(String.valueOf(testPhase.getTriggerTime()));
				ebs.addContent(triggerTime);
				Element stdyTime = new Element("stdyTime").setText(String.valueOf(testPhase.getStdyTime()));
				ebs.addContent(stdyTime);
			}
		}
		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8");
		format.setIndent("  ");

		XMLOutputter XMLOut = new XMLOutputter(format);
		try {
			XMLOut.output(doc, new FileOutputStream(xml));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SaveToFile() {

		Element root = doc.getRootElement();
		root.removeChildren("testName");
		Element testName = new Element("testName").setText( args.getTestName() );
		root.addContent(testName);
		root.removeChildren("testDescription");
		Element testDescription = new Element("testDescription").setText( args.getTestDescription() );
		root.addContent(testDescription);
		
		root.removeChildren("rbe");
		Element rbe = new Element("rbe");
		root.addContent(rbe);
		rbe.setAttribute(new Attribute("rbetype", args.getRbetype()));
		Element interval = new Element("interval").setText(String.valueOf(args.getInterval()));
		rbe.addContent(interval);
		Element prepair = new Element("prepair").setText(String.valueOf(args.getPrepair()));
		rbe.addContent(prepair);
		Element cooldown = new Element("cooldown").setText(String.valueOf(args.getCooldown()));
		rbe.addContent(cooldown);
		Element out = new Element("out").setText(args.getOut());
		rbe.addContent(out);
		Element tolerance = new Element("tolerance").setText(String.valueOf(args.getTolerance()));
		rbe.addContent(tolerance);
		Element retry = new Element("retry").setText(String.valueOf(args.getRetry()));
		rbe.addContent(retry);
		Element thinktime = new Element("thinktime").setText(String.valueOf(args.getThinktime()));
		rbe.addContent(thinktime);
		Element urlConnectionTimeOut = new Element("urlConnectionTimeOut").setText(String
				.valueOf(args.getUrlConnectionTimeOut()));
		rbe.addContent(urlConnectionTimeOut);
		Element urlReadTimeOut = new Element("urlReadTimeOut").setText(String.valueOf(args
				.getUrlReadTimeOut()));
		rbe.addContent(urlReadTimeOut);
		Element mix = new Element("mix").setText(args.getMix());
		rbe.addContent(mix);
		Element slow = new Element("slow").setText(String.valueOf(args.getSlow()));
		rbe.addContent(slow);
		Element getImage = new Element("getImage").setText(String.valueOf(args.isGetImage()));
		rbe.addContent(getImage);
		Element baseURL = new Element("baseURL").setText(args.getBaseURL());
		rbe.addContent(baseURL);

		for (TestPhase testPhase : args.getEbs()) {
			Element ebs = new Element("ebs");
			rbe.addContent(ebs);
			Element baseLoad = new Element("baseLoad").setText(String.valueOf(testPhase.getBaseLoad()));
			ebs.addContent(baseLoad);
			Element randomLoad = new Element("randomLoad").setText(String.valueOf(testPhase.getRandomLoad()));
			ebs.addContent(randomLoad);
			Element rate = new Element("rate").setText(String.valueOf(testPhase.getRate()));
			ebs.addContent(rate);
			Element triggerTime = new Element("triggerTime").setText(String.valueOf(testPhase.getTriggerTime()));
			ebs.addContent(triggerTime);
			Element stdyTime = new Element("stdyTime").setText(String.valueOf(testPhase.getStdyTime()));
			ebs.addContent(stdyTime);
			
		}

		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8");
		format.setIndent("  ");

		XMLOutputter XMLOut = new XMLOutputter(format);
		try {
			XMLOut.output(doc, new FileOutputStream(xml));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initRBEWithXMLFile() {
		Element root = doc.getRootElement();
		Element rbe = root.getChild("rbe");
		Args args = new Args();
		
		args.setTestName(root.getChildText("testName"));
		args.setTestDescription(root.getChildText("testDescription"));
		args.setRbetype(rbe.getAttribute("rbetype").getValue());
		args.setInterval(Double.parseDouble(rbe.getChildText("interval")));
		args.setCooldown(Integer.parseInt(rbe.getChildText("cooldown")));
		args.setPrepair(Integer.parseInt(rbe.getChildText("prepair")));
		args.setMix(rbe.getChildText("mix"));
		args.setOut(rbe.getChildText("out"));
		args.setTolerance(Double.parseDouble(rbe.getChildText("tolerance")));
		args.setRetry(Integer.parseInt(rbe.getChildText("retry")));
		args.setThinktime(Double.parseDouble(rbe.getChildText("thinktime")));
		args.setUrlConnectionTimeOut(Integer.parseInt(rbe.getChildText("urlConnectionTimeOut")));
		args.setUrlReadTimeOut(Integer.parseInt(rbe.getChildText("urlReadTimeOut")));
		if (rbe.getChildText("slow") != null)
			args.setSlow(Double.parseDouble(rbe.getChildText("slow")));
		if (rbe.getChildText("getImage") != null) {
			if (rbe.getChildText("getImage").equals("true")) {
				args.setGetImage(true);
			} else {
				args.setGetImage(false);
			}
		}
		args.setBaseURL(rbe.getChildText("baseURL"));

		List ebs = rbe.getChildren("ebs");
		for (int j = 0; j < ebs.size(); j++) {
			Element eb = (Element) ebs.get(j);
			TestPhase testPhase = new TestPhase();
			testPhase.setBaseLoad(Integer.parseInt(eb.getChildText("baseLoad")));
			testPhase.setRandomLoad(Integer.parseInt(eb.getChildText("randomLoad")));
			testPhase.setRate(Integer.parseInt(eb.getChildText("rate")));
			testPhase.setTriggerTime(Integer.parseInt(eb.getChildText("triggerTime")));
			testPhase.setStdyTime(Integer.parseInt(eb.getChildText("stdyTime")));
			args.getEbs().add(testPhase);
		}
		this.args = args;
	}

	public File getXMLFile() {
		return XMLFile;
	}

	public void setXMLFile(File file) {
		XMLFile = file;
	}

	public Args getArgs() {
		return args;
	}

	public void setArgs(Args args) {
		this.args = args;
	}
}
