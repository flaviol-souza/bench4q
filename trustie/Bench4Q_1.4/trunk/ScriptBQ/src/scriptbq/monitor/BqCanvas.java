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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * BqCanvas is the main class to show the performance real time. It provides several methods to
 * show the curve dynamically
 */
public class BqCanvas extends Canvas{

	/**
	 * The point of the canvas to draw the curve, it changes all the time
	 */
	private List<Point>listPoints;
	/**
	 * The max value of all the data, it is used to determine scale of Y-Axis
	 */
	private int maxValue;
	
	private ScrollBar barX;	
	private ScrollBar barY;
	
	private int hSelection;
	
	/**
	 * time stands for real time
	 */
	private int time;
	/**
	 * timeFrame is used to show the length of the time to show curve
	 */
	private int timeFrame;
	private int timeFrameWithoutZoom;
	/**
	 * The min time of X-Axis to show
	 */
	private int initMinTime;
	/**
	 * The min time of X-Axis to show
	 */
	private int minTime;
	
	/**
	 * marginH  the margin in Y-Axis
	 */
	private int marginH;
	/**
	 * marginW  the margin in X-Axis
	 */
	private int marginW;
	/**
	 * Init max value of the data 
	 */
	private int initMax;
	
	private int zoomY = 1;
	private int minY = 0;
	
	@SuppressWarnings("unused")
	private String type;
	private int result;
	private String BqType = "";
	
	/**
	 * whether testing is finished
	 */
	private boolean finished=false;
	
	/**
	 * values to set scales
	 */
	static private final int[][] scaleValues =
	{
		{4,8},
		{4,8,12},
		{4,8,12,16},
		{10,20},
		{10,20,30},
		{10,20,30,40},
		{20,40},
		{20,40,60},
		{20,40,60},
		{20,40,60,80},
		{20,40,60,80}
	};

	/**
	 * constructor
	 * @param parent
	 * @param style
	 */
	public BqCanvas(Composite parent, int style) {
		super(parent, style);
		//maxValues = new HashMap<String,Integer>();
		listPoints = new ArrayList<Point>();
		maxValue = 0;
		time = 0;
		minTime = 0;
		initMinTime = 0;
		timeFrame = 300*1000;
		timeFrameWithoutZoom = 300*1000;
		
		GC gc = new GC(this);
		marginH = gc.getFontMetrics().getHeight()*2 + 4;
		marginW = gc.getFontMetrics().getAverageCharWidth()*5 + 4;
		gc.dispose();
	
		barX = this.getHorizontalBar();
		barX.setMaximum(barX.getThumb());
		barX.setIncrement(barX.getMaximum());
		barX.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int diff = barX.getSelection()-hSelection;
				minTime = minTime + (diff*(timeFrame/barX.getThumb()));
				hSelection=barX.getSelection();				
			}});
		
		hSelection=0;		
		barY = this.getVerticalBar();
		barY.setMaximum(barY.getThumb());
		barY.setIncrement(barY.getMaximum());
	
	}


	
	/**
	 * Method to add point
	 */
	public void addPoint(int time,int value){
		if(this.time<time){
			this.time = time;
		}
		listPoints.add(new Point(time,value));
		if(value>maxValue){
			maxValue = value;
		}		
	}
	
	/**
	 * Method to remove points
	 */
	public void removePoints(){
		
		if(time > timeFrameWithoutZoom + initMinTime)
		{
			int tmpTime = initMinTime;
			initMinTime=time-timeFrameWithoutZoom;
			minTime=minTime + (initMinTime-tmpTime);
			Iterator<Point> iter = listPoints.iterator();
			while(iter.hasNext()){
				Point tp = iter.next();
				if(tp.x < minTime){
					iter.remove();
				}
			}
		}
	}
	
	/**
	 * Method to draw X-Axis
	 */
	private void drawXAxis(){
		Rectangle rec = getClientArea();
		GC gc = new GC(this);
		gc.setLineWidth(2);
		gc.drawLine(marginW, rec.height-marginH, rec.width-marginW, rec.height-marginH);
		gc.dispose();
	}
	
	/**
	 * Method to draw Y-Axis
	 */
	private void drawYAxis(){
		Rectangle rec = getClientArea();
		GC gc = new GC(this);
		gc.setLineWidth(2);
		gc.drawLine(marginW, rec.height-marginH, marginW, marginH);
		gc.dispose();
	}
	
	/**
	 * Method to draw the curve. 
	 * @param graph
	 * @param max
	 * @param localMax
	 * @return
	 */
	private int drawCurve(Rectangle graph, int max, int localMax){
		GC gc = new GC(this);
		gc.setLineWidth(2);
		Color CurveColor = new Color(null,0,0,128);
		Point[] points = listPoints.toArray(new Point[listPoints.size()]);
		
		int pointsToDisplay=0;
		max = calculateMaxYValue(max);

		for (int j = 0; j < points.length; j++) {
			Point p = points[j];
			if ((p.x>=minTime)&&(p.x<=minTime+timeFrame)){
				pointsToDisplay++;
			}
		}
		int[] intPoints = new int[pointsToDisplay*2];
		int i=0;
		int stop=0;
		int start=0;
		int sup=0;
		int between=0;
		gc.setForeground(CurveColor);
		for (int j = 0; j < points.length; j++) {
			Point p = points[j];
			if ((p.x>=minTime)&&(p.x<=minTime+timeFrame)){
				intPoints[i*2] = (int) ((p.x-minTime) * (long)graph.width / timeFrame+marginW);
				if ((max == 0)||(p.y<minY)) {
					intPoints[i*2+1] = (graph.height + marginH);
					if (max!=0){
						if (between==2){
							int[] partCurve = new int[4];
							partCurve[0]=intPoints[(i*2)-2];
							partCurve[1]=marginH;
							partCurve[2]=intPoints[i*2];
							partCurve[3]=(graph.height + marginH);
							gc.drawPolyline(partCurve);
						}
						between=1;
					}
					if (start!=stop+1){
						if (sup==0){
							drawCurvePortion(stop,start,intPoints,gc, 2);
							start=i+1;
						}else if (start!=0) {
							drawCurvePortionWithStartLine(sup, stop, start, graph.height, intPoints, gc, 2);
							start=i+1;
						}
					}else{
						start=i+1;
						stop=i;
					}
					if (max!=0){
						sup=1;
					}
				}
				//if point is over Y zoomed area
				else if (p.y>max){
					intPoints[i*2+1] = marginH;
					//if we have to draw a line connecting a point below and a point over a zoomed area
					if (between==1){
						int[] partCurve = new int[4];
						partCurve[0]=intPoints[(i*2)-2];
						partCurve[1]=(graph.height + marginH);
						partCurve[2]=intPoints[i*2];
						partCurve[3]=marginH;
						gc.drawPolyline(partCurve);
					}
					between=2;
					if (start!=stop+1){
						if (sup==0){
							drawCurvePortion(stop,start,intPoints,gc, 2);
							start=i+1;
						}else if (start!=0) {
							drawCurvePortionWithStartLine(sup, stop, start, graph.height, intPoints, gc, 2);
							start=i+1;
						}

					}else{
						start=i+1;
						stop=i;
					}
					sup=2;
				}else{
					//if point is in Y zoomed area
					intPoints[i*2+1] = graph.height - ((int)((long)(p.y-minY) * (long)graph.height / (long)(max-minY)))+marginH;
					stop=i+1;
					between=0;
				}
				i++;

			}
		}
		if (intPoints.length > 0 && intPoints[intPoints.length-1] < localMax)
		{
			localMax = intPoints[intPoints.length-1];
		}
		// draw the final curve portion
		if (start!=stop+1){
			if (sup==0){
				drawCurvePortion(stop, start, intPoints, gc, 0);
			}else if (start!=0){
				drawCurvePortionWithStartLine(sup, stop, start, graph.height, intPoints, gc, 0);
			}
		}
		gc.dispose();
		return localMax;
	}
	
	/**
	 * Method to draw curve
	 * @param stop
	 * @param start
	 * @param intPoints
	 * @param gc
	 * @param type
	 */
	private void drawCurvePortion(int stop, int start, int[] intPoints, GC gc, int type){
		int[] partCurve = new int[(stop-start)*2+type];
		int m=0;
		for (int k=start*2;k<(stop*2+type);k++){
			partCurve[m]=intPoints[k];
			m++;
		}
		gc.drawPolyline(partCurve);
	}
	
	/**
	 * Method to draw the curve
	 * @param sup
	 * @param stop
	 * @param start
	 * @param height
	 * @param intPoints
	 * @param gc
	 * @param type
	 */
	private void drawCurvePortionWithStartLine(int sup, int stop, int start, int height, int[] intPoints, GC gc, int type){
		int[] partCurve = new int[(stop-start)*2+2+type];
		int m=2;
		for (int k=start*2;k<(stop*2+type);k++){
			partCurve[m]=intPoints[k];
			m++;
		}
		partCurve[0]=intPoints[(start*2)-2];
		if (sup==1){
			partCurve[1]=(height + marginH);
		}else {
			partCurve[1]= marginH;
		}
		gc.drawPolyline(partCurve);
	}
	
	/**
	 * Method to draw the scale of Y-Axis
	 */
	private void drawScale(){
		
		Rectangle rec = getClientArea();
		GC gc = new GC(this);
		Rectangle graph = new Rectangle(0, marginH, rec.width-2*marginW, rec.height-2*marginH);
		gc.setLineStyle(SWT.LINE_DASH);
		int max = maxValue;
		initMax=max;
		max = calculateMaxYValue(initMax);
		minY = (int)((double)max -((double)initMax/(double)zoomY));

		int dec = getDecimals(max-minY);
		int [] values = null;
		int []tmp = null;
		tmp = scaleValues[calculateScale(max-minY)];

		if(max-minY < 10 && max-minY > 1) {
			values = new int[tmp.length];
			for (int i = 0; i < values.length; i++) {
				values[i]=tmp[i]/10;
			}
		}
		else {
			if(graph.height/50 > tmp.length*2) {
				int middle = tmp[0]/2;
				if(tmp[tmp.length-1] < max-middle*dec) {
					values = new int[tmp.length*2+1];
				}
				else {
					values = new int[tmp.length*2];
				}

				for (int i = 1; i <= values.length; i++) {
					values[i-1] = middle*i;
				}
			}
			else {
				values = tmp;
			}
		}

		if(max != 0) {
			int maxWidth = rec.width-marginW;
			int maxPos = marginH;
			//Draw scaleValues
			for (int i = 0; i < values.length; i++) {
				int val = values[values.length-i-1]*dec;
				int pos = (int)((long)(max-minY-val) * (long)graph.height/((long)max-minY)) + marginH;
				gc.drawLine(marginW,pos,maxWidth,pos);
				if(Math.abs(pos - maxPos) > gc.getFontMetrics().getHeight()) {
					gc.drawText(String.valueOf(val+minY), 0, pos);
				}
			}
			gc.setForeground(new Color(null,0,0,0));
			gc.drawText(String.valueOf(max), 0, maxPos);
			gc.setForeground(new Color(null,255,0,0));
			gc.drawLine(marginW, maxPos, maxWidth, maxPos);
		}
		String info = "";
		if(BqType.equals("throughput")){
			info = "throughput";
		}
		else{
			info = "responsetime(ms)";
		}
		FontData data = new FontData("Arial",10,SWT.ITALIC);
		gc.setFont(new Font(null,data));
		gc.setForeground(new Color(null,255,0,0));
		gc.drawText(info,0,15);
		gc.dispose();
	}
	
	/**
	 * Method to draw time of X-Axis
	 * @param max
	 */
	private void drawTime(int max){
		
		GC gc = new GC(this);	
		Rectangle rec = getClientArea();
		int timePos = (int)((long)(time - minTime) * (rec.width-2*marginW) / timeFrame + marginW);
		gc.setForeground(new Color(this.getDisplay(),0,0,0));
		
		Rectangle graph = new Rectangle(0, marginH, rec.width-2*marginW, rec.height-2*marginH);
		int dec = getDecimals(timeFrame);
		int step = scaleValues[calculateScale(timeFrame)][0]*dec/2000;
		if(graph.width/50 > (timeFrame/1000)/step*2) {
			step = step/2;
		}
		int i = 1;
		while(step*i < minTime/1000) {
			i++;
		}
		i--;

		int maxHeight = rec.height - marginH + 2;
		gc.setLineWidth(1);
		gc.setLineStyle(SWT.LINE_SOLID);
		while (step*i <= (minTime+timeFrame)/1000) {
			int val = step*i++;
			int pos = (int)(((long)val*1000-minTime) * graph.width/timeFrame + marginW);
			if(pos >= marginW && pos < graph.width + marginW) {
				gc.drawText(FormatTime.getTime(val*1000), pos, maxHeight);
				gc.drawLine(pos,rec.height-marginH,pos,rec.height-marginH+3);
			}
		}

		if(timePos >= marginW && timePos <= graph.width + marginW) {
			gc.setLineStyle(SWT.LINE_DOT);
			gc.drawLine(timePos,rec.height-marginH,timePos,max);
			gc.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_RED));
			gc.drawText(FormatTime.getTime(time), timePos, maxHeight);
		}
		FontData data = new FontData("Arial",10,SWT.ITALIC);
		gc.setFont(new Font(null,data));
		gc.setForeground(new Color(null,255,0,0));
		gc.drawText("time", marginW+graph.width, marginH+graph.height+15);
		gc.dispose();
		
	}
	
	/**
	 * Method to draw the whole graph, including the curve, scale, time and X-Axis, Y-Axis
	 */
	public void drawGraph(){	
		
		removePoints();
		drawXAxis();
		drawYAxis();
		Rectangle rec = getClientArea();
		if(finished){
			GC gc = new GC(this);
			Color resultColor = new Color(null,255,0,0);
			String info = "";
			if(BqType.equals("throughput")){
				info="WIPS = "+result;
			}
			else{
				info="Avg responsetime = "+result;
			}
			FontData data = new FontData("Arial",10,SWT.BOLD);
			gc.setFont(new Font(null,data));
			gc.setForeground(resultColor);
			gc.drawText(info,this.getClientArea().width/4,0);
		}
		Rectangle graph = new Rectangle(marginW, marginH, rec.width-2*marginW, rec.height-2*marginH);
		int max = maxValue;
		int localMax = rec.height;
		localMax = drawCurve(graph, max, localMax);			
		drawTime(localMax);
		drawScale();
	}
	
	/**
	 * Method to set the result
	 * @param type
	 * @param result
	 */
	public void setResult(String type,int result){
		this.type=type;
		this.result=result;
		this.finished=true;
	}
	
	/**
	 * Method to get the decimals, it is a tool method
	 * @param val
	 * @return
	 */
	private int getDecimals(int val) {
		int dec = 0;
		while((val = val/10) >= 1) {dec++;}
		return (dec==0)?1:(int)Math.pow(10,dec-1);
	}
	
	/**
	 * Method to caculate the scale of Y-Axis
	 * @param val
	 * @return
	 */
	private int calculateScale(int val) {
		if(val < 10 && val > 1) {
			return val+1;
		}
		while(val/100 >= 1) {
			val = val/10;
		}
		if(val < 20) {
			if(val<12) {
				return 0;
			}
			return (val < 16)?1:2;
		}
		return val/10+1;
	}
	
	/**
	 * Method to calculate the max value of the Y-Axis
	 * @param initMax
	 * @return
	 */
	private int calculateMaxYValue(int initMax){
		double tmpMax = barY.getMaximum()-(barY.getSelection()+barY.getThumb());
		double tmpMax2 = (double)initMax/(double)barY.getMaximum();
		tmpMax = tmpMax * tmpMax2;
		tmpMax = tmpMax + ((double)initMax/(double)zoomY);
		return (int)tmpMax;
	}
	
	/**
	 * Method to set timeFrame
	 * @param timeFrame
	 */
	public void setTimeFrame(int timeFrame){
		this.timeFrame = timeFrame;
		this.timeFrameWithoutZoom = timeFrame;
	}
	
	/**
	 * Method to set the canvas type
	 * @param x
	 */
	public void setBqType(String x){
		this.BqType = x;
	}
}
