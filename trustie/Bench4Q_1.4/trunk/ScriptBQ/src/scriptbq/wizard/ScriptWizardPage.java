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

package scriptbq.wizard;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import maxq.HttpCapture;
import maxq.Utils.UserException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import javax.swing.JTextArea;

import scriptbq.BqConstant;
/**
 * This class is the wizard page of ScriptWizard
 *
 */
public class ScriptWizardPage extends WizardPage {
	
	/**
	 * start local proxy server
	 */
	private Button proxyButton;
	/**
	 * start recording script
	 */
	private Button startButton;
	/**
	 * stop recording script
	 */
	private Button stopButton;
	/**
	 * shut down the local proxy server
	 */
	private Button shutButton;
	private Label timeLabelFix;
	private Label timeLabelChange;
	/**
	 * choose to use existed file as script
	 */
	private Combo existedFile;
	private Button openFile;
	private HttpCapture BqHttpCapture = null;
	/**
	 * file path of script file
	 */
	private String ScriptPath;
	/**
	 * status of recording
	 */
	private boolean isRecording;
	private Button existselection;
	
	private Timer BqTimer;
	private long beginTime;
	private long recordedTime = 0;
	private long currentTime;

	public ScriptWizardPage() {	
		super("wizardPage");
		setTitle("Http Script");
		setDescription("This wizard allows the user to create http-script for test.");		
		ScriptPath = BqConstant.BqTreePath;	
		isRecording = false;
	}

	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2,false));
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);
		
		proxyButton = new Button(composite,SWT.PUSH);
		proxyButton.setText("proxy server");
		proxyButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					BqHttpCapture = new HttpCapture(ScriptPath,BqConstant.MAXQPORT,
							BqConstant.MAXQGENERATOR,new JTextArea());
					BqHttpCapture.startProxyServer();
					proxyButton.setEnabled(false);
					startButton.setEnabled(true);
					System.out.println("INFO: Proxy Server Started at Port: "+BqConstant.MAXQPORT);
				} catch (IOException e1) {
					MessageDialog.openError(
							getShell(),
							"ScriptBq",
					"Error When build the proxy server!");
				} catch (UserException e1) {
					MessageDialog.openError(
							getShell(),
							"ScriptBq",
					"Error When build the proxy server!");
				}
			}
		});
		
		shutButton = new Button(composite,SWT.PUSH);
		shutButton.setText("shut server");
		shutButton.setEnabled(false);
		shutButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					if(isRecording){
						BqHttpCapture.stopRecording();
					}
					BqHttpCapture.shutProxyServer();
					proxyButton.setEnabled(true);
					startButton.setEnabled(false);
					stopButton.setEnabled(false);
					shutButton.setEnabled(false);
					recordedTime=0;
					BqTimer.cancel();
				} catch (IOException e1) {
					MessageDialog.openError(
							getShell(),
							"ScriptBq",
					"Error When shut the proxy server!");
				} catch (UserException e1) {
					MessageDialog.openError(
							getShell(),
							"ScriptBq",
					"Error When shut the proxy server!");
				}
			}
		});
		
		startButton = new Button(composite,SWT.PUSH);
		startButton.setText("start recording");
		startButton.setEnabled(false);
		startButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					BqHttpCapture.startRecording();
					stopButton.setEnabled(true);
					shutButton.setEnabled(true);
					startButton.setEnabled(false);
					beginTime = System.currentTimeMillis()/1000;
					startTimer();
					isRecording = true;
				} catch (IOException e1) {
					MessageDialog.openError(
							getShell(),
							"ScriptBq",
					"Error When start recording!");
					e1.printStackTrace();
				} catch (UserException e1) {
					MessageDialog.openError(
							getShell(),
							"ScriptBq",
					"Error When start recording!");
					e1.printStackTrace();
				}
			}
		});
		
		stopButton = new Button(composite,SWT.PUSH);
		stopButton.setText("stop recording");
		stopButton.setEnabled(false);
		stopButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					BqHttpCapture.stopRecording();
					recordedTime=currentTime;
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
					shutButton.setEnabled(true);
					isRecording = false;
					BqTimer.cancel();
				} catch (IOException e1) {
					MessageDialog.openError(
							getShell(),
							"ScriptBq",
					"Error When stop recording!");
				} catch (UserException e1) {
					MessageDialog.openError(
							getShell(),
							"ScriptBq",
					"Error When stop recording!");
				}
			}
		});
		
		
		
		timeLabelFix = new Label(composite, SWT.NULL);
		timeLabelFix.setText("Time elapsed (seconds) :");
		GridData gd = new GridData(GridData.BEGINNING);
		timeLabelFix.setLayoutData(gd);
		
		timeLabelChange = new Label(composite, SWT.NULL);
		timeLabelChange.setText("0");
		gd = new GridData(GridData.BEGINNING);
		gd.widthHint=100;
		timeLabelChange.setLayoutData(gd);
		
		existselection=new Button(composite,SWT.CHECK|SWT.LEFT);
		existselection.setText("choose a existed script file:");
		GridData data = new GridData();
		data.horizontalAlignment = GridData.BEGINNING;
		//data.verticalAlignment=GridData.END;
		data.grabExcessHorizontalSpace = true;
		data.horizontalSpan = 2;
		data.verticalIndent=50;
		existselection.setLayoutData(data);
		
		existedFile=new Combo(composite,SWT.DROP_DOWN);
		existedFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//existedFile.setText("D:/Script.xml");
		openFile=new Button(composite,SWT.NULL);
		openFile.setText("Browse...");
		openFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileSelect=new FileDialog(Display.getCurrent().getActiveShell(),SWT.SINGLE);    
				fileSelect.setFilterNames(new String[]{"*.xml"});    
				fileSelect.setFilterExtensions(new String[]{"*.xml"});    
				String url="";
				url=fileSelect.open();
				existedFile.setText(url);
				existedFile.add(url);
			}
		});
		setControl(composite);					
	}
	
	private void startTimer(){
		BqTimer = new Timer();
		BqTimer.schedule(new TimerTask() {
			public void run() {
				try{
					getShell().getDisplay().syncExec(new Runnable() {
						public void run () {
							try{
								displayBqTimer();
							} catch (Throwable t) {
								BqTimer.cancel();
							}
						}
					});
				}catch (Throwable t){
					BqTimer.cancel();
				}
			}
		}, new Date(), 1000);
	}

	private void displayBqTimer(){
		currentTime = recordedTime + (System.currentTimeMillis()/1000)-beginTime;
		timeLabelChange.setText(String.valueOf(currentTime));
	}

	public String getExistedFile()
	{
		return existedFile.getText();
	}
	public boolean getExisted()
	{
		return existselection.getSelection();
	}
}
