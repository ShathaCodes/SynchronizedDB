package app;

import java.util.Vector;

public class test {
	public static void main(String[] args) throws Exception{
		
		GestUsersDAO dao =new GestUsersDAO();
		
		System.out.println("avant suppression");
		Vector<Sale> sales = dao.selectAll();
		for (Sale sale :sales) {
			System.out.print(sale.getId());
		}
		
		
		//dao.deleteSale(1);
		//dao.addSale(10, "prduct10", "region10", "2020-05-05", 10, 10, 10, 10, 10);
		
		//Sale s=dao.find(10);
		//System.out.println("sale avant update  "+ s.getProduct());
		
		//s.setProduct("update fait");
		//Sale s1=s;
		//s1.setProduct("update fait");
		
		//dao.updateSale(s1);	
		//Sale s1=dao.find(10);
		//System.out.println("sale apres update"+ s1.getProduct());
		
		dao.deleteSale(10);
		
		System.out.println("apres suppression");
		Vector<Sale> sales2 = dao.selectAll();
		for (Sale sale :sales2) {
			System.out.print(sale.getId());
		}
	}
}
