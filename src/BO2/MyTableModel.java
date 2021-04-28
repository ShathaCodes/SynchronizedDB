package BO2;

import java.awt.Button;
import java.awt.Component;
import java.lang.Object;
import java.sql.Connection;
import java.util.Optional;
import javax.swing.table.*;
import javax.swing.event.TableModelEvent;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;


import java.util.ArrayList;


public class MyTableModel extends AbstractTableModel implements TableModel {
    
	public List<Sale> inclomeList;
    private GestUsersDAO salesdb=new GestUsersDAO();

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
    
    public List<Sale> getIncomeData() {
        return inclomeList;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
        	case 0:  return "ID";
            case 1:return "Date";
            case 2: return "Product";
            case 3: return "Amount";
            case 4:return "Cost";
            case 5:return "Quantite";
            case 6:return "Region";
            case 7:return "Tax";
            case 8:return "Total";
            case 9: return "Delete";
            default:
                return super.getColumnName(column);
        }
    }

    @Override
    public int getRowCount() {
            if(inclomeList != null) 
                return inclomeList.size();
            return 0;
    }

    @Override
    public int getColumnCount() {
        return 10;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Sale inclome = null;

        if(inclomeList != null && inclomeList.size() > row) {
            inclome = inclomeList.get(row);
        }

        switch (column) {
        case 0: return inclome != null ? inclome.getId() : null;
        case 1: return inclome != null ? inclome.getDate() : null;
        case 2: return inclome != null ? inclome.getProduct() : null;
        case 3: return inclome != null ? inclome.getAmt() : null;
        case 4: return inclome != null ? inclome.getCost() : null;
        case 5: return inclome  != null ? inclome.getQuantite() : null;
        case 6: return inclome != null ? inclome.getRegion() : null;
        case 7: return inclome  != null ? inclome.getTax() : null;
        case 8: return inclome != null ? inclome.getTotal() : null;
        }

        return null;
    }

@Override
public void setValueAt(Object value, int row, int col)
{
   Sale sale =inclomeList.get(row);
   if (col==1)
	    sale.setDate((String)value);
    if (col==2)
    	sale.setProduct((String)value);
    else if (col==3)
    	sale.setAmt((double)value);
    if (col==4)
	    sale.setCost((double)value);

    if (col==5)
    	sale.setQuantite((int)value);
    else if (col==6)
    	sale.setRegion((String)value);
    if (col==7)
	    sale.setTax((double)value);

    if (col==8)
    	sale.setTotal((double)value);
    if (col==9)
    	
    {   System.out.println(sale.getId()+"id fasakh");
    	salesdb.deleteSale(sale.getId());
    	inclomeList.remove(row);
    	}
    else {
    if (row>=inclomeList.size()-1 && sale.getId() == 0)
    {
    	System.out.println("ya nariiiii  " + row + " - " + col  );
    	System.out.println(inclomeList.size());
   sale.setId(inclomeList.get(inclomeList.size()-2).getId()+1);
  salesdb.addSale(sale.getId(),Optional.ofNullable(sale.getProduct()).orElse(""),Optional.ofNullable(sale.getRegion()).orElse(""),
		
		  Optional.ofNullable(sale.getDate()).orElse(""),sale.getQuantite(), sale.getCost(),sale.getAmt(),sale.getTax(),sale.getTotal());

    }
  else {
	  System.out.println("badl " + row + " - " + col);
	  System.out.println(inclomeList.size());
    	salesdb.updateSale(sale);
    	
    }
    }
    fireTableCellUpdated(row,col);
    fireTableDataChanged();
}

    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }
  
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return true;
    }
    

    public void update() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                inclomeList = salesdb.selectAll();
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