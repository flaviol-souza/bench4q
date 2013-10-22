//a test file ,not in use currently
package src.storage;
import java.sql.*;
import org.sqlite.*;
public class StoSQLite {
	private String m_fileName;
	private Connection m_conn;
	private Statement m_stmt;
	public StoSQLite(String fileName) {
		try {
			m_fileName = fileName;
			Class.forName("org.sqlite.JDBC");
			m_conn = DriverManager.getConnection("jdbc:sqlite:"+m_fileName);
			m_stmt = m_conn.createStatement();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.toString());
		}
	}
	public void initSto() {
		try {
			if(m_stmt == null)
				throw new Exception("SQLite init error.");
			m_stmt.executeUpdate("CREATE DATABASE AgentDB");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.toString());
		}
	}
}
