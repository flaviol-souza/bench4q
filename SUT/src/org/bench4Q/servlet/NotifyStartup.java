package org.bench4Q.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NotifyStartup
 */
@WebServlet("/NotifyStartup")
public class NotifyStartup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NotifyStartup() {
		super();
		// TODO Auto-generated constructor stub
		Socket client = null;
		PrintStream out = null;
		try {
			client = new Socket("bench4qdb", 8888);
			out = new PrintStream(client.getOutputStream());  
			String fromUser = "algo:8080";
			out.print(fromUser);
			out.close();
		    client.close();
		} catch (UnknownHostException ex) {
			System.out.println("Couldn't connect to the server");
		} catch (IOException ex) {
			System.err.println(ex.toString());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
