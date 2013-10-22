/**
 * =========================================================================
 * 					Bench4Q_Script version 1.3.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at  
 * http://www.trustie.net/projects/project/show/Bench4Q
 * You can find latest version there. 
 * Bench4Q_Script adds a script module for Internet application to Bench4Q
 * http://www.trustie.com/projects/project/show/Bench4Q_Script
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
 *  * Initial developer(s): Wangsa , Tianfei , WUYulong , Zhufeng
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */


package scriptbq.resource;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * This class is key to govern the resources of the console; it provides a
 * convenient way to get the images for the console.
 */
public class ImageRegistry {
	private static ImageRegistry INSTANCE;

	/**
	 * This is a static method to get the instance of the class
	 * @return  The instance of the class
	 */
	public static ImageRegistry getRegistry() {
		if (INSTANCE == null) {
			synchronized (ImageRegistry.class) {
				if (INSTANCE == null) {
					INSTANCE = new ImageRegistry();
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * The directory of the images
	 */
	private static String ICON_FOLDER = "resource/icons/";

	public static String TENANTGROUP = "tenantgroup.png";
	public static String TENANT = "tenant.png";
	public static String AGENT = "agent.png";
	public static String STARTALL = "startall.png";
	public static String NEWTENANT = "newtenant.png";
	public static String NEWAGENT = "newagent.png";
	public static String CONFIG = "config.png";
	public static String STARTTENANT = "starttenant.gif";
	public static String AGENTINFO = "info.png";
	public static String SCRIPT = "script.gif";
	public static String DEPLOY = "deploy.png";
	public static String COMMANDLINE = "commandline.png";
	public static String ABOUT = "about.png";
	public static String EXIT = "exit.gif";
	public static String MAGNIFY = "magnify.png";
	public static String ZOOMINX = "zoomInX.png";
	public static String ZOOMOUTX = "zoomOutX.png";
	public static String ZOOMINY = "zoomInY.png";
	public static String ZOOMOUTY = "zoomOutY.png";
	public static String SAVE = "save.gif";

	/**
	 * Map used to store all of images
	 */
	private Map<String, Image> images;
	/**
	 * Map used to store all of imageDescriptors
	 */
	private Map<String, ImageDescriptor> imageDescriptors;

	/**
	 * Method used to find the Icons
	 * @return  The list of Icons' name
	 */
	public static List<String> findIconsList() {
		Field[] fields = ImageRegistry.class.getFields();
		List<String> list = new ArrayList<String>();
		for (Field field : fields) {
			int modifies = field.getModifiers();
			if (field.getType().equals(String.class)
					&& (modifies & Modifier.PUBLIC) != 0
					&& (modifies & Modifier.STATIC) != 0) {
				try {
					list.add(field.get(null).toString());
				} catch (Exception e) {
				}
			}
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	/**
	 * constructor
	 */
	private ImageRegistry() {
		List<String> list = findIconsList();
		images = new ConcurrentHashMap<String, Image>();
		imageDescriptors = new ConcurrentHashMap<String, ImageDescriptor>();

		File file = null;
		for (String name : list) {
			file = new File(ICON_FOLDER, name);
			try {
				ImageDescriptor imageDescriptor = ImageDescriptor
						.createFromURL(file.toURL());
				imageDescriptors.put(name, imageDescriptor);
				images.put(name, imageDescriptor.createImage());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method used to get the path of image
	 * @param name   The name of the image
	 * @return       The path of the image
	 */
	public static String getImagePath(String name){
		return ICON_FOLDER+name;
	}
	
	/**
	 * Method used to get the image
	 * @param name   The name of image
	 * @return       The Image object
	 */
	public static Image getImage(String name) {
		return getRegistry().images.get(name);
	}

	/**
	 * Method used to get the imageDescriptor
	 * @param name   The name of image
	 * @return       The ImageDescriptor object
	 */
	public static ImageDescriptor getImageDescriptor(String name) {
		return getRegistry().imageDescriptors.get(name);
	}
}
