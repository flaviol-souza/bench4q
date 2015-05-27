package org.bench4Q.console.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import javax.swing.Timer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.IconUIResource;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;

public class ProgressBarFrame extends JDialog{
	
	private static final int DEFAULT_WIDTH = 225;
	private static final int DEFAULT_HEIGHT = 225;
	private JButton collectButton;
	private JProgressBar progressBar;
	private JButton cancelButton;
	private SimulatedActivity activity;
	private Timer activityMonitor;
	private ProcessControl m_processControl;
	private Resources m_resource;
	private JLabel pic;

	public ProgressBarFrame(JFrame jframe, Args args, ProcessControl processcontrol, Resources resource){
		
		super(jframe, true);
		setTitle("Benchmarking...");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        m_processControl = processcontrol;
        m_resource = resource;
		
		int testInterval = 0;
		int testPhaseEndTime;
		for (TestPhase testPhase : args.getEbs()) {
			testPhaseEndTime = testPhase.getTriggerTime()
					+ testPhase.getExperimentTime();
			if (testPhaseEndTime > testInterval) {
				testInterval = testPhaseEndTime;
			}
		}
		
		
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				m_processControl.stopAgentAndWorkerProcesses();
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		JPanel buttonPanel = new JPanel();
		JPanel progressPanel = new JPanel();

		collectButton = new JButton("Collect");
		cancelButton = new JButton("Cancel");
		progressBar = new JProgressBar();
		

		pic = new JLabel(m_resource.getImageIcon("loading.image"));
		
		activityMonitor = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int current = activity.getCurrent();

				progressBar.setStringPainted(!progressBar.isIndeterminate());
				progressBar.setValue(current);

				if (current == activity.getTarget()) {
					activityMonitor.stop();
					collectButton.setEnabled(true);
				}
			}
		});
		collectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m_processControl.collectResultProcesses();
				dispose();
				
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m_processControl.stopAgentAndWorkerProcesses();
				collectButton.setEnabled(true);
				cancelButton.setEnabled(false);
				activityMonitor.stop();
				try {
					Thread.sleep(2000l);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					Thread.currentThread().interrupt();
				}
				
			}
		});
		
		
		progressPanel.add(progressBar);
		buttonPanel.add(collectButton);
		buttonPanel.add(cancelButton);
		
		
		add(pic, BorderLayout.NORTH);
		add(progressPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		collectButton.setEnabled(false);
		progressBar.setMaximum(testInterval);
		activity = new SimulatedActivity(testInterval);
		new Thread(activity).start();
		activityMonitor.start();
		
		
		setLocationRelativeTo(jframe);
	}
	
	class SimulatedActivity implements Runnable
	{
		private volatile int current;
		private int target;
		
		public SimulatedActivity(int t){
			current = 0;
			target = t;
		}
		
		public int getTarget(){
			return target;
		}
		
		public int getCurrent(){
			return current;
		}

		@Override
		public void run() {
			try{
				while(current < target){
					Thread.sleep(1000);
					current++;
				}
			}
			catch(InterruptedException e){
				
			}
		}
		
	}
//	public static void main(String[] args){
//		JFrame frame = new ProgressBarFrame();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//		}
	

}
