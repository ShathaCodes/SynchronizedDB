package app;

import java.sql.Connection;
import java.sql.DriverManager;
import com.rabbitmq.client.*;

public class HO {
	private static Connection conn; 
	static{
		try{ 
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection ("jdbc:mysql://localhost:3306/HO","root","enafadeet");
		}
		catch(Exception e){ e.printStackTrace();
		}
		}
	public static Connection getConnection(){ return conn;
	}
}
