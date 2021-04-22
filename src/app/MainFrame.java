package app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;


	
	
public class MainFrame extends JFrame  {

    private JTable table = new JTable(new MyTableModel());
    
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
        //addI.addActionListener(e -> ((MyTableModel)table.getModel()).setIncomeData(new Sale()));
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
