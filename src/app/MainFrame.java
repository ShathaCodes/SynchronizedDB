package app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
	
	
public class MainFrame extends JFrame  {

    private static JTable table = new JTable(new MyTableModel());
    
    ButtonC button = new ButtonC(table, 9);
     
    public MainFrame() throws Exception {
    	
        super("MainFrame");
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
			channel.queueDeclare("BO1", false, false, false, null);
			JSONWriter rabbitmqJson = new JSONWriter();
			String jsonmessage = rabbitmqJson.write(((MyTableModel) table.getModel()).getIncomeData());
			channel.basicPublish("", "BO1", null, jsonmessage.getBytes());
			System.out.println(" [x] Sent '" + jsonmessage + "'");
		}
    	
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
			try {
				new MainFrame().setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
    }
}
