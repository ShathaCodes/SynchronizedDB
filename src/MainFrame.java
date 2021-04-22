import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

	
	
public class MainFrame extends JFrame {

    private JTable table = new JTable(new MyTableModel());
     
    public MainFrame() throws Exception {
    	
        super("MainFrame");
    
        createGUI();
        
    }

    private void createGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));

        JButton addI = new JButton("+");
        addI.addActionListener(e -> ((MyTableModel)table.getModel()).setIncomeData(new Sale()));
     /*   ((MyTableModel)table.getModel().addTableModelListener(new TableModelListener() {

      	  public void tableChanged(TableModelEvent e) {
      	     // your code goes here, whatever you want to do when something changes in the table
      	  }
      	});*/
       // JButton addE = new JButton("+");
       // addE.addActionListener(e -> ((MyTableModel)table.getModel()).setExpenseData(new Sale()));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panel.add(addI);
     //   panel.add(addE);

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }
}
