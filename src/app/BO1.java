package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.rabbitmq.client.*;
import com.rabbitmq.tools.json.JSONWriter;

public class BO1 {
	private static Connection conn; 
	static{
		try{ 
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection ("jdbc:mysql://localhost:3306/BO1","root","enafadeet");
		}
		catch(Exception e){ e.printStackTrace(); }
		}
	public static Connection getConnection(){ return conn;
	}
	
	private final static String QUEUE_NAME = "BO1";
	
	public static Vector<Sale> selectAll(){
		Connection conn=BO1.getConnection();
		Vector<Sale> sales=new Vector<Sale>();
		Sale s=null; 
		try {
			PreparedStatement ps=conn.prepareStatement("select * from `sales` ");
			ResultSet rsSale=ps.executeQuery();
			while(rsSale.next()){
				s=new Sale();
				s.setId(new Integer(rsSale.getInt("ID")));
				s.setProduct(rsSale.getString("product"));
				s.setAmt(rsSale.getDouble("amt"));
				s.setCost(rsSale.getDouble("cost"));
				s.setDate(rsSale.getString("date"));
				s.setQuantite(rsSale.getInt("quantite"));
				s.setRegion(rsSale.getString("region"));
				s.setTax(rsSale.getDouble("tax"));
				s.setTotal(rsSale.getDouble("total"));
				sales.add(s);
				}
			} catch (SQLException e) { e.printStackTrace();
			}
		return sales;
		}
	
	public static void main(String[] args) throws Exception{
		
		Vector<Sale> sales = selectAll();
		String message = "";

		for (Sale sale : sales) {
			message += sale.getId() + ";" + sale.getProduct() +";" + sale.getRegion()+ ";" + sale.getDate()+";"+ sale.getQuantite()+";" + sale.getCost() +";" + sale.getAmt() + ";" + sale.getTax() + ";" + sale.getTotal()+":";
		}
		System.out.println(message);
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (com.rabbitmq.client.Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			JSONWriter rabbitmqJson = new JSONWriter();
			String jsonmessage = rabbitmqJson.write(sales);
			channel.basicPublish("", QUEUE_NAME, null, jsonmessage.getBytes());
			System.out.println(" [x] Sent '" + jsonmessage + "'");
		}
		}
}
