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
			m_builder.setFeature("http://apache.org/xml/features/validation/schema", true);
			m_builder.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
					getClass().getClassLoader().getResource(schema).toString());
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

		Element testName = new Element("testName").setText(m_args.getTestName());
		root.addContent(testName);
		Element testDescription = new Element("testDescription").setText(m_args.getTestDescription());
		root.addContent(testDescription);

		Element rbe = new Element("rbe");
		root.addContent(rbe);
		rbe.setAttribute(new Attribute("rbetype", "closed"));
		Element interval = new Element("interval").setText(String.valueOf(m_args.getInterval()));
		rbe.addContent(interval);
		Element prepair = new Element("prepair").setText(String.valueOf(m_args.getPrepair()));
		rbe.addContent(prepair);
		Element cooldown = new Element("cooldown").setText(String.valueOf(m_args.getCooldown()));
		rbe.addContent(cooldown);
		Element out = new Element("out").setText(m_args.getOut());
		rbe.addContent(out);
		Element tolerance = new Element("tolerance").setText(String.valueOf(m_args.getTolerance()));
		rbe.addContent(tolerance);
		Element retry = new Element("retry").setText(String.valueOf(m_args.getRetry()));
		rbe.addContent(retry);
		Element vIPrate = new Element("VIPrate").setText(String.valueOf(m_args.getRate()));
		rbe.addContent(vIPrate);
		Element thinktime = new Element("thinktime").setText(String.valueOf(m_args.getThinktime()));
		rbe.addContent(thinktime);
		Element mix = new Element("mix").setText(m_args.getMix());
		rbe.addContent(mix);
		Element slow = new Element("slow").setText(String.valueOf(m_args.getSlow()));
		rbe.addContent(slow);
		Element getImage = new Element("getImage").setText(String.valueOf(m_args.isGetImage()));
		rbe.addContent(getImage);
		Element baseURL = new Element("baseURL").setText(m_args.getBaseURL());
		rbe.addContent(baseURL);
		Element webport = new Element("webport").setText(String.valueOf(m_args.getWebPort()));
		rbe.addContent(webport);
		Element DBURL = new Element("databaseURL").setText(m_args.getDBURL());
		rbe.addContent(DBURL);
		Element DBPort = new Element("dbport").setText(String.valueOf(m_args.getDBPort()));
		rbe.addContent(DBPort);
		Element hypbaseURL = new Element("hypbaseURL").setText(m_args.getHyperHost());
		rbe.addContent(hypbaseURL);
		Element hypport = new Element("hypport").setText(String.valueOf(m_args.getHyperPort()));
		rbe.addContent(hypport);
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
		Element testName = new Element("testName").setText(m_args.getTestName());
		root.addContent(testName);
		root.removeChildren("testDescription");
		Element testDescription = new Element("testDescription").setText(m_args.getTestDescription());
		root.addContent(testDescription);

		root.removeChildren("rbe");
		Element rbe = new Element("rbe");
		root.addContent(rbe);
		rbe.setAttribute(new Attribute("rbetype", m_args.getRbetype()));
		Element interval = new Element("interval").setText(String.valueOf(m_args.getInterval()));
		rbe.addContent(interval);
		Element prepair = new Element("prepair").setText(String.valueOf(m_args.getPrepair()));
		rbe.addContent(prepair);
		Element cooldown = new Element("cooldown").setText(String.valueOf(m_args.getCooldown()));
		rbe.addContent(cooldown);
		Element out = new Element("out").setText(m_args.getOut());
		rbe.addContent(out);
		Element tolerance = new Element("tolerance").setText(String.valueOf(m_args.getTolerance()));
		rbe.addContent(tolerance);
		Element retry = new Element("retry").setText(String.valueOf(m_args.getRetry()));
		rbe.addContent(retry);
		Element vIPrate = new Element("VIPrate").setText(String.valueOf(m_args.getRate()));
		rbe.addContent(vIPrate);
		Element thinktime = new Element("thinktime").setText(String.valueOf(m_args.getThinktime()));
		rbe.addContent(thinktime);
		Element tfOption = new Element("tfOption").setText(String.valueOf(m_args.getTfOption()));
		rbe.addContent(tfOption);
		Element mix = new Element("mix").setText(m_args.getMix());
		rbe.addContent(mix);
		Element slow = new Element("slow").setText(String.valueOf(m_args.getSlow()));
		rbe.addContent(slow);
		Element getImage = new Element("getImage").setText(String.valueOf(m_args.isGetImage()));
		rbe.addContent(getImage);
		Element baseURL = new Element("baseURL").setText(m_args.getBaseURL());
		rbe.addContent(baseURL);
		
		Element lbHost = new Element("lbHost").setText(m_args.getLbHost());
		rbe.addContent(lbHost);
		Element lbPort = new Element("lbPort").setText(String.valueOf(m_args.getLbPort()));
		rbe.addContent(lbPort);
		Element nvms = new Element("nvms").setText(String.valueOf(m_args.getNvms()));
		rbe.addContent(nvms);
		Element downstep = new Element("downstep").setText(String.valueOf(m_args.getDownStep()));
		rbe.addContent(downstep);
		Element upstep = new Element("upstep").setText(String.valueOf(m_args.getUpStep()));
		rbe.addContent(upstep);
		Element addload = new Element("addload").setText(String.valueOf(m_args.getAddLoad()));
		rbe.addContent(addload);
		Element addloadopt = new Element("addloadopt").setText(String.valueOf(m_args.getAddLoadOpt()));
		rbe.addContent(addloadopt);
		
		Element webport = new Element("webport").setText(String.valueOf(m_args.getWebPort()));
		rbe.addContent(webport);
		
		Element hypbaseURL = new Element("hypbaseURL").setText(String.valueOf(m_args.getHyperHost()));
		rbe.addContent(hypbaseURL);
		Element hypport = new Element("hypport").setText(String.valueOf(m_args.getHyperPort()));
		rbe.addContent(hypport);
		
		Element DatabaseURL = new Element("databaseURL").setText(m_args.getDBURL());
		rbe.addContent(DatabaseURL);

		Element DBPort = new Element("dbport").setText(String.valueOf(m_args.getDBPort()));
		rbe.addContent(DBPort);

		for (TestPhase testPhase : m_args.getEbs()) {
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
		args.setInterval(Double.parseDouble(rbe.getChildText("interval").trim()));
		args.setCooldown(Integer.parseInt(rbe.getChildText("cooldown").trim()));
		args.setPrepair(Integer.parseInt(rbe.getChildText("prepair").trim()));
		args.setMix(rbe.getChildText("mix").trim());
		args.setOut(rbe.getChildText("out").trim());
		args.setTolerance(Double.parseDouble(rbe.getChildText("tolerance").trim()));
		args.setRetry(Integer.parseInt(rbe.getChildText("retry").trim()));
		args.setThinktime(Double.parseDouble(rbe.getChildText("thinktime")));
		args.setTfOption(Boolean.parseBoolean(rbe.getChildText("tfOption")));
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
		args.setLbHost(rbe.getChildText("lbHost").trim());
		args.setLbPort(Integer.parseInt(rbe.getChildText("lbPort").trim()));
		args.setNvms(Integer.parseInt(rbe.getChildText("nvms").trim()));
		args.setDownStep(Double.parseDouble(rbe.getChildText("downstep").trim()));
		args.setUpStep(Double.parseDouble(rbe.getChildText("upstep").trim()));
		args.setAddLoad(Integer.parseInt(rbe.getChildText("addload").trim()));
		args.setAddLoadOpt(Integer.parseInt(rbe.getChildText("addloadopt").trim()));
		args.setWebPort(Integer.parseInt(rbe.getChildText("webport").trim()));
		args.setDBURL(rbe.getChildText("databaseURL").trim());
		args.setDBPort(Integer.parseInt(rbe.getChildText("dbport").trim()));
		args.setHyperHost(rbe.getChildText("hypbaseURL").trim());
		args.setHyperPort(Integer.parseInt(rbe.getChildText("hypport").trim()));
		

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
