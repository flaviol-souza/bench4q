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

package scriptbq.console;

import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * This class redirect the system's stdin and stdout, so all
 * of the information can be displayed out. This class is also
 * the rcp view.
 */
public class BqConsole extends MessageConsole {

	/**
	 * The console instance of Bench4Q_Script
	 */
	private static MessageConsole console = new MessageConsole("Bench4Q Console",null);
	
	/**
	 * The console stream for the message
	 */
	private static MessageConsoleStream inMessageStream;

	/**
	 * constructor
	 */
	public BqConsole(){
		super("BqConsole",null);
		inMessageStream = console.newMessageStream();
		System.setOut(new PrintStream(inMessageStream));
		System.setErr(System.out);
	}

	/**
	 * Method to open console
	 */
	public void openConsole() {
		showConsole();
	}
	
	/**
	 * Method to show the console
	 */
	public static void showConsole() {		
		if (console != null) {   
		       IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();   
		       IConsole[] existing = manager.getConsoles();   
		       boolean exists = false;    
		       for (int i = 0; i < existing.length; i++) {   
		           if (console == existing[i])   
		               exists = true;   
		       }       
		       if(!exists){    
		           manager.addConsoles(new IConsole[] { console });   
		       }   
		       manager.showConsoleView(console);  
		   }  

    }
	
	/**
	 * Method to close the console
	 * @throws IOException
	 */
    public void closeConsole() throws IOException{
        IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
        if (console != null){
            manager.removeConsoles(new IConsole[]{ console });
        }
        System.setOut(System.out);
		System.setErr(System.err);
		inMessageStream.close();
    }
    
    /**
     * Static method to get the console
     * @return  The instance of console
     */
    public static MessageConsole getConsole(){
        return console;
    }
    /**
     * Method to print information
     * @param message
     */
    public static void showMessage(String message){
    	inMessageStream.print(message);
    }
    
}

