/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there. 
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.console.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.EventListener;
import java.util.List;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.common.util.ListenerSupport;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author duanzhiquan
 * 
 */
public final class ConfigModel {

	private File m_selectedFile;

	private final ListenerSupport m_listeners = new ListenerSupport();

	private static final String xml = "org/bench4Q/resources/bench4Q.xml";
	private static final String schema = "org/bench4Q/resources/bench4Q-schema.xsd";
	private SAXBuilder m_builder;
	private Document m_doc;
	private Args m_args;

	/**
	 * 
	 */
	public ConfigModel() {
		try {
			m_builder = new SAXBuilder(false);
			m_builder.setFeature(
					"http://apache.org/xml/features/validation/schema", true);
			m_builder
					.setProperty(
							"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
							getClass().getClassLoader().getResource(schema)
									.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		InputStream file = getClass().getClassLoader().getResourceAsStream(xml);
		try {
			m_doc = m_builder.build(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initRBE();

	}

	/**
	 * @param defaultFile
	 * 
	 */
	public ConfigModel(File defaultFile) {
		this();
		m_selectedFile = defaultFile;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean CheckFile(File file) {
		try {
			m_doc = m_builder.build(new FileReader(file));
		} catch (JDOMException e) {
			return false;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 */
	public void newEmptyFile(File file) {

		CheckFile(file);
		initFile(file);
	}

	/**
	 * @param XMLFile
	 */
	public void initFile(File XMLFile) {

		Element root = new Element("bench4Q");
		m_doc.setRootElement(root);

		Element testName = new Element("testName")
				.setText(m_args.getTestName());
		root.addContent(testName);
		Element testDescription = new Element("testDescription").setText(m_args
				.getTestDescription());
		root.addContent(testDescription);

		Element rbe = new Element("rbe");
		root.addContent(rbe);
		rbe.setAttribute(new Attribute("rbetype", "closed"));
		Element interval = new Element("interval").setText(String
				.valueOf(m_args.getInterval()));
		rbe.addContent(interval);
		Element prepair = new Element("prepair").setText(String.valueOf(m_args
				.getPrepair()));
		rbe.addContent(prepair);
		Element cooldown = new Element("cooldown").setText(String
				.valueOf(m_args.getCooldown()));
		rbe.addContent(cooldown);
		Element out = new Element("out").setText(m_args.getOut());
		rbe.addContent(out);
		Element tolerance = new Element("tolerance").setText(String
				.valueOf(m_args.getTolerance()));
		rbe.addContent(tolerance);
		Element retry = new Element("retry").setText(String.valueOf(m_args
				.getRetry()));
		rbe.addContent(retry);
		Element thinktime = new Element("thinktime").setText(String
				.valueOf(m_args.getThinktime()));
		rbe.addContent(thinktime);
		Element mix = new Element("mix").setText(m_args.getMix());
		rbe.addContent(mix);
		Element slow = new Element("slow").setText(String.valueOf(m_args
				.getSlow()));
		rbe.addContent(slow);
		Element getImage = new Element("getImage").setText(String
				.valueOf(m_args.isGetImage()));
		rbe.addContent(getImage);
		Element baseURL = new Element("baseURL").setText(m_args.getBaseURL());
		rbe.addContent(baseURL);
		if (m_args.getEbs().isEmpty()) {
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
			for (TestPhase testPhase : m_args.getEbs()) {
				Element ebs = new Element("ebs");
				rbe.addContent(ebs);
				Element baseLoad = new Element("baseLoad").setText(String
						.valueOf(testPhase.getBaseLoad()));
				ebs.addContent(baseLoad);
				Element randomLoad = new Element("randomLoad").setText(String
						.valueOf(testPhase.getRandomLoad()));
				ebs.addContent(randomLoad);
				Element rate = new Element("rate").setText(String
						.valueOf(testPhase.getRate()));
				ebs.addContent(rate);
				Element triggerTime = new Element("triggerTime").setText(String
						.valueOf(testPhase.getTriggerTime()));
				ebs.addContent(triggerTime);
				Element stdyTime = new Element("stdyTime").setText(String
						.valueOf(testPhase.getStdyTime()));
				ebs.addContent(stdyTime);
			}
		}
		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8");
		format.setIndent("  ");

		XMLOutputter XMLOut = new XMLOutputter(format);
		try {
			XMLOut.output(m_doc, new FileOutputStream(XMLFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		resetConfigPanel();
	}

	/**
	 * 
	 */
	public void SaveToFile() {
		SaveToFile(m_selectedFile);
	}

	/**
	 * @param XMLFile
	 */
	public void SaveToFile(File XMLFile) {

		Element root = m_doc.getRootElement();
		root.removeChildren("testName");
		Element testName = new Element("testName")
				.setText(m_args.getTestName());
		root.addContent(testName);
		root.removeChildren("testDescription");
		Element testDescription = new Element("testDescription").setText(m_args
				.getTestDescription());
		root.addContent(testDescription);

		root.removeChildren("rbe");
		Element rbe = new Element("rbe");
		root.addContent(rbe);
		rbe.setAttribute(new Attribute("rbetype", m_args.getRbetype()));
		Element interval = new Element("interval").setText(String
				.valueOf(m_args.getInterval()));
		rbe.addContent(interval);
		Element prepair = new Element("prepair").setText(String.valueOf(m_args
				.getPrepair()));
		rbe.addContent(prepair);
		Element cooldown = new Element("cooldown").setText(String
				.valueOf(m_args.getCooldown()));
		rbe.addContent(cooldown);
		Element out = new Element("out").setText(m_args.getOut());
		rbe.addContent(out);
		Element tolerance = new Element("tolerance").setText(String
				.valueOf(m_args.getTolerance()));
		rbe.addContent(tolerance);
		Element retry = new Element("retry").setText(String.valueOf(m_args
				.getRetry()));
		rbe.addContent(retry);
		Element thinktime = new Element("thinktime").setText(String
				.valueOf(m_args.getThinktime()));
		rbe.addContent(thinktime);
		Element mix = new Element("mix").setText(m_args.getMix());
		rbe.addContent(mix);
		Element slow = new Element("slow").setText(String.valueOf(m_args
				.getSlow()));
		rbe.addContent(slow);
		Element getImage = new Element("getImage").setText(String
				.valueOf(m_args.isGetImage()));
		rbe.addContent(getImage);
		Element baseURL = new Element("baseURL").setText(m_args.getBaseURL());
		rbe.addContent(baseURL);

		for (TestPhase testPhase : m_args.getEbs()) {
			Element ebs = new Element("ebs");
			rbe.addContent(ebs);
			Element baseLoad = new Element("baseLoad").setText(String
					.valueOf(testPhase.getBaseLoad()));
			ebs.addContent(baseLoad);
			Element randomLoad = new Element("randomLoad").setText(String
					.valueOf(testPhase.getRandomLoad()));
			ebs.addContent(randomLoad);
			Element rate = new Element("rate").setText(String.valueOf(testPhase
					.getRate()));
			ebs.addContent(rate);
			Element triggerTime = new Element("triggerTime").setText(String
					.valueOf(testPhase.getTriggerTime()));
			ebs.addContent(triggerTime);
			Element stdyTime = new Element("stdyTime").setText(String
					.valueOf(testPhase.getStdyTime()));
			ebs.addContent(stdyTime);

		}

		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8");
		format.setIndent("  ");

		XMLOutputter XMLOut = new XMLOutputter(format);
		try {
			FileOutputStream outstream = new FileOutputStream(XMLFile);
			XMLOut.output(m_doc, outstream);
			outstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public boolean initRBEWithXMLFile() {
		if ((m_selectedFile == null) || !CheckFile(m_selectedFile)) {
			return false;
		}
		try {
			m_doc = m_builder.build(m_selectedFile);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initRBE();
		resetConfigPanel();
		return true;
	}

	private void initRBE() {
		Element root = m_doc.getRootElement();
		Element rbe = root.getChild("rbe");
		Args args = new Args();

		args.setTestName(root.getChildText("testName").trim());
		args.setTestDescription(root.getChildText("testDescription").trim());
		args.setRbetype(rbe.getAttribute("rbetype").getValue().trim());
		args.setInterval(Double
				.parseDouble(rbe.getChildText("interval").trim()));
		args.setCooldown(Integer.parseInt(rbe.getChildText("cooldown").trim()));
		args.setPrepair(Integer.parseInt(rbe.getChildText("prepair").trim()));
		args.setMix(rbe.getChildText("mix").trim());
		args.setOut(rbe.getChildText("out").trim());
		args.setTolerance(Double.parseDouble(rbe.getChildText("tolerance")
				.trim()));
		args.setRetry(Integer.parseInt(rbe.getChildText("retry").trim()));
		args.setThinktime(Double.parseDouble(rbe.getChildText("thinktime")));
		if (rbe.getChildText("slow") != null)
			args.setSlow(Double.parseDouble(rbe.getChildText("slow").trim()));
		if (rbe.getChildText("getImage") != null) {
			if (rbe.getChildText("getImage").equals("true")) {
				args.setGetImage(true);
			} else {
				args.setGetImage(false);
			}
		}
		args.setBaseURL(rbe.getChildText("baseURL").trim());

		List ebs = rbe.getChildren("ebs");
		for (int j = 0; j < ebs.size(); j++) {
			Element eb = (Element) ebs.get(j);
			TestPhase testPhase = new TestPhase();
			testPhase
					.setBaseLoad(Integer.parseInt(eb.getChildText("baseLoad")));
			testPhase.setRandomLoad(Integer.parseInt(eb
					.getChildText("randomLoad")));
			testPhase.setRate(Integer.parseInt(eb.getChildText("rate")));
			testPhase.setTriggerTime(Integer.parseInt(eb
					.getChildText("triggerTime")));
			testPhase
					.setStdyTime(Integer.parseInt(eb.getChildText("stdyTime")));
			args.getEbs().add(testPhase);
		}
		this.m_args = args;

	}

	/**
	 * Add a new listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void addListener(Listener listener) {
		m_listeners.add(listener);
	}

	/**
	 * @return
	 */
	public File getSelectedFile() {
		return m_selectedFile;
	}

	/**
	 * @param file
	 */
	public void setSelectedFile(File file) {
		m_selectedFile = file;
	}

	/**
	 * @author duanzhiquan
	 * 
	 */
	public interface Listener extends EventListener {

		/**
		 * 
		 */
		void isArgsChanged();

	}

	/**
	 * @author duanzhiquan
	 * 
	 */
	public abstract static class AbstractListener implements Listener {

		public void isArgsChanged() {
		}
	}

	/**
	 * @return
	 */
	public Args getArgs() {
		return m_args;
	}

	/**
	 * @param args
	 */
	public void setArgs(Args args) {
		this.m_args = args;
	}

	private void resetConfigPanel() {
		m_listeners.apply(new ListenerSupport.Informer() {
			public void inform(Object listener) {
				((Listener) listener).isArgsChanged();
			}
		});
	}

}
