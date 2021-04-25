package HO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Vector;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import com.rabbitmq.tools.json.JSONWriter;

public class HO {
	private final static String HO1= "HO1";
	private final static String HO2 = "HO2";
	private final static String BO1 = "BO1";
	private final static String BO2 = "BO2";
	
	private static GestUsersDAO bd = new GestUsersDAO();
	
	private static void verif(Sale[] sales) throws Exception{
		
		for(Sale s : sales) {
			System.out.println(s.getProduct());
			
        	Sale sale = bd.find(s.getId());
        	//System.out.println(sale.getId());
        	if(sale == null) {
        		System.out.println("inseriiiii");
        		bd.addSale(s.getId(), s.getProduct(),s.getRegion() , s.getDate(), s.getQuantite(), s.getCost(), s.getAmt(),s.getTax(),s.getTotal());
        	}
        	else {
        		if (sale.hasChanged(s))
        			System.out.println("tbadel");
        			bd.updateSale(s);
        	}
        }
		System.out.println("lol");
		Vector<Sale> sbd =  bd.selectAll();
		System.out.println(sbd.get(1));
		boolean there=false;
		if (sbd.size() > sales.length) {
			for ( Sale s : sbd) {
				there=false;
				for (Sale ss : sales) {
					if(s.getId()==ss.getId())
						there=true;
				}
				if(!there)
					bd.deleteSale(s.getId());
			}
		}
		sendData(BO1);
	    sendData(BO2);
	}
	private static void sendData(String Q) throws Exception{
		Vector<Sale> sbd =  bd.selectAll();
    	ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (com.rabbitmq.client.Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.queueDeclare(Q, false, false, false, null);
			JSONWriter rabbitmqJson = new JSONWriter();
			String jsonmessage = rabbitmqJson.write(sbd);
			channel.basicPublish("", Q, null, jsonmessage.getBytes());
			System.out.println(" [x] Sent '" + jsonmessage + "'");
		}
    	
    }
	

	  public static void main(String[] argv) throws Exception {
		  
		  Gson gson = new Gson();
		  
		  
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    com.rabbitmq.client.Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(HO1, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        Sale[] sales1 = gson.fromJson(message, Sale[].class);
	        try {
				verif(sales1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    };
	    channel.basicConsume(HO1, true, deliverCallback, consumerTag -> { });
	    
	    Channel channel2 = connection.createChannel();
	    channel2.queueDeclare(HO2, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        Sale[] sales2 = gson.fromJson(message, Sale[].class);
	        try {
				verif(sales2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    };
	    channel2.basicConsume(HO2, true, deliverCallback2, consumerTag -> { });
	   
	  }
	
}
