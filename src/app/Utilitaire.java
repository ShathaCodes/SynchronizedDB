package app;

import java.sql.*;

public class Utilitaire {
	private static Connection conn; 
	static{
		try{ 
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection ("jdbc:mysql://localhost:3306/BO1","root","enafadeet");

		}
		catch(Exception e){ e.printStackTrace();
		}
		}
	public static Connection getConnection(){ return conn;
	}
	public static void main(String [] args)
	{
		Connection c = Utilitaire.getConnection();
		}
	}
