package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import com.rabbitmq.tools.json.JSONWriter;


public class BO1 extends JFrame  {

    private static JTable table = new JTable(new MyTableModel());
	private static GestUsersDAO bd = new GestUsersDAO();

    
    ButtonC button = new ButtonC(table, 9);
     
    public BO1() throws Exception {
    	
        super("BO1");
        createGUI();
    }

    private void createGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 400));

        JButton addI = new JButton("+");
        addI.addActionListener(e -> ((MyTableModel)table.getModel()).setIncomeData(new Sale()));
        JButton synch = new JButton("Synchronize");
        synch.addActionListener(e -> {
			try {
				sendData();
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
		});
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panel.add(addI);
        panel.add(synch);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(null);

        ((MyTableModel)table.getModel()).update();
    }
    
    
    private static void sendData() throws Exception{
    	ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (com.rabbitmq.client.Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.queueDeclare("HO1", false, false, false, null);
			JSONWriter rabbitmqJson = new JSONWriter();
			String jsonmessage = rabbitmqJson.write(((MyTableModel) table.getModel()).getIncomeData());
			channel.basicPublish("", "HO1", null, jsonmessage.getBytes());
			System.out.println(" [x] Sent '" + jsonmessage + "'");
		}
		
		
    	
    }
private static void verif(Sale[] sales) {
		
		for(Sale s : sales) {
			System.out.println(s.getProduct());
			
        	Sale sale = bd.find(s.getId());
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
		((MyTableModel) table.getModel()).update();
	}
	

    public static void main(String[] args) throws Exception{
        SwingUtilities.invokeLater(() -> {
			try {
				new BO1().setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        Gson gson = new Gson();
		  
		  
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    com.rabbitmq.client.Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare("BO1", false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        Sale[] sales1 = gson.fromJson(message, Sale[].class);
	        verif(sales1);
	        
	    };
	    channel.basicConsume("BO1", true, deliverCallback, consumerTag -> { });
    }
}
