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
package scriptbq.tree;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import scriptbq.Activator;
import scriptbq.resource.ImageRegistry;

/**
 * This class is used to provide label for BqTreeView. The model of TreeViewer in JFace is MVC,
 * BqTreeLabelProvider provides the label of it. This class is necessary for the BqTreeViewer
 * to show out.
 */
public class BqTreeLabelProvider extends LabelProvider{
	
	/**
	 * Method to show the information of the object
	 * @param obj   The specified object
	 */
	public String getText(Object obj) {
		return obj.toString();
	}
	
	/**
	 * Method to get the image for the object, this method provides the images for actions
	 * @param obj  The specified obg
	 * @return  The corresponding image
	 */
	public Image getImage(Object obj) {
		ImageDescriptor ImgDes = null;
		if(obj instanceof BqTreeParent){
			BqTreeParent pobj = (BqTreeParent)obj;
			String name = pobj.getName();
			if(name=="TenantGroup"){
				ImgDes = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						         ImageRegistry.getImagePath(ImageRegistry.TENANTGROUP)); 
			}
			else{
				ImgDes = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				         ImageRegistry.getImagePath(ImageRegistry.TENANT));
			}
		}
		else if(obj instanceof BqTreeObject){
			ImgDes = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			         ImageRegistry.getImagePath(ImageRegistry.AGENT));
		}
		
		return ImgDes.createImage();

	}

}
