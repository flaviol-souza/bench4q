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
package scriptbq.monitor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import scriptbq.tree.BqTreeObject;

/**
 * This class is the container of the canvas for drawing curves. It consists of two
 * parts each monitors a data from the agent.
 */
public class BqGraph extends SashForm implements PaintListener{
	/**
	 * The left canvas to draw curve
	 */
	private BqCanvas cvLeft;
	/**
	 * The right canvas to draw curve
	 */
	private BqCanvas cvRight;
	/**
	 * updateReport is used to update the graph
	 */
	private BqActionReport updateReport;
	
	/**
	 * constructor
	 * @param parent
	 * @param style
	 * @param agent
	 */
	public BqGraph(Composite parent, int style,BqTreeObject agent) {
		super(parent, style);
		this.setLayout(new FillLayout());
		Color bg = new Color(null,240,255,255 );
		
		cvLeft = new BqCanvas(this,SWT.V_SCROLL | SWT.H_SCROLL);
		cvLeft.setLayout(new FillLayout());
		cvLeft.addPaintListener(this);
		cvLeft.setBqType("throughput");
		cvLeft.setBackground(bg);
	
		cvRight = new BqCanvas(this,SWT.V_SCROLL | SWT.H_SCROLL);
		cvRight.setLayout(new FillLayout());
		cvRight.addPaintListener(this);
		cvRight.setBqType("responsetime");
		cvRight.setBackground(bg);
		
		updateReport = new BqActionReport(this,agent);
		updateReport.setDelay(1000);
		updateReport.start();
		
		this.setWeights(new int[]{50,50});
	}
	
	/**
	 * Method to get the left canvas
	 * @return
	 */
	public BqCanvas getCanvasLeft(){
		return cvLeft;
	}
	
	/**
	 * Method to get the right canvas
	 * @return
	 */
	public BqCanvas getCanvasRight(){
		return cvRight;
	}
	
	/**
	 * Method to add the data into the left canvas
	 * @param time
	 * @param value
	 */
	public void addPointLeft(int time,int value){
		cvLeft.addPoint(time, value);
	}
	
	/**
	 * Method to add the data into the right canvas
	 * @param time
	 * @param value
	 */
	public void addPointRight(int time,int value){
		cvRight.addPoint(time, value);
	}

	/**
	 * Method to response to the PaintEvent
	 */
	public void paintControl(PaintEvent e) {
		cvLeft.drawGraph();
		cvRight.drawGraph();
	}

}