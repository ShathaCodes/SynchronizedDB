import java.awt.Component;
import java.lang.Object;

import javax.swing.event.TableModelEvent;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;
import java.util.ArrayList;


public class MyTableModel extends AbstractTableModel implements TableModel {
    private List<Sale> inclomeList;
  //
    private List<Sale> expenseList;




    private void setData(List<Sale> list, Sale data) {
        int rows = getRowCount();
        int row = list.size();
        list.add(data);

        if(row < rows) {
            fireTableRowsUpdated(row, row);
        }
        else {
            fireTableRowsInserted(row, row);
        }
    }

    public void setIncomeData(Sale data) {
        if(inclomeList == null) {
            inclomeList = new ArrayList<>();
        }

        setData(inclomeList, data);
    }

 /*   public void setExpenseData(Sale data) {
        if(expenseList == null) {
            expenseList = new ArrayList<>();
        }

        setData(expenseList, data);
    }*/

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Date";

            case 1: return "Product";
            case 2: return "Amount";
            case 3:return "Cost";
            case 4:
                return "Quantite";
            case 5:
            	return "Region";
            case 6:
            	return "Tax";

            case 7:
            	return "Total";
         /*   case 8:
            	return "Tax";

            case 9:
            	return "Total";*/


            default:
                return super.getColumnName(column);
        }
    }

    @Override
    public int getRowCount() {
       // if(inclomeList == null || expenseList == null) {
            if(inclomeList != null) {
                return inclomeList.size();
            }

            return 0;
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Sale inclome = null;

        if(inclomeList != null && inclomeList.size() > row) {
            inclome = inclomeList.get(row);
        }

       /* if(expenseList != null && expenseList.size() > row) {
            expense = expenseList.get(row);
        }*/

        switch (column) {
            case 0: return inclome != null ? inclome.getDate() : "";
            case 1: return inclome != null ? inclome.getProduct() : "";
            case 2: return inclome != null ? inclome.getAmt() : "";
            case 3: return inclome != null ? inclome.getCost() : "";
            case 4: return inclome  != null ? inclome.getQuantite() : "";
            case 5: return inclome != null ? inclome.getRegion() : "";
            case 6: return inclome  != null ? inclome.getTax() : "";
            case 7: return inclome != null ? inclome.getTotal() : "";
           // case 8: return expense != null ? inclome.getTotal() : "";


        }

        return null;
    }


@Override
public void setValueAt(Object value, int row, int col)
{
   Sale sale =inclomeList.get(row);
   if (col==0)
	    sale.setDate((String)value);

    if (col==1)
    	sale.setProduct((String)value);
    else if (col==2)
    	sale.setAmt((double)value);
    if (col==3)
	    sale.setCost((double)value);

    if (col==4)
    	sale.setQuantite((int)value);
    else if (col==5)
    	sale.setRegion((String)value);
    if (col==6)
	    sale.setTax((double)value);

    if (col==7)
    	sale.setTotal((double)value);

    fireTableCellUpdated(row,col);
}
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }


    public boolean isCellEditable(int row, int col)
    {
        return true;
    }
    
    void tableChanged(TableModelEvent e)
    {Sale s=new Sale();
    s.setProduct("YEEES");
    inclomeList.add(s);
    
    }

    public void update() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Database database = new Database();
                inclomeList = database.getData(Database.TBL_INCOME);
                //expenseList = database.getData(Database.TBL_EXPENSES);

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    fireTableDataChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }
}