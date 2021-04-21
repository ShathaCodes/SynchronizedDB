package app;

import java.sql.*;
import java.util.Vector;


public class GestUsersDAO {
	/*public Utilisateur loadUser(String login, String pass){ 
		Connection conn=Utilitaire.getConnection();
		Sale s=null;
		try {
			PreparedStatement ps=conn.prepareStatement("select * from `sales` where LOGIN=? and PASS=?");
			ps.setString(1,login);
			ps.setString(2,pass);
			ResultSet rsUser=ps.executeQuery();
			if(rsUser.next()){
				u=new Utilisateur();
				u.setIdUser(new Integer(rsUser.getInt("ID_USER")));
				u.setLogin(rsUser.getString("LOGIN"));
				u.setPass(rsUser.getString("PASS")); 
				u.setEmail(rsUser.getString("EMAIL")); 
				u.setVille(rsUser.getString("VILLE"));
				u.setName(rsUser.getString("NAME"));
				}
			} catch (SQLException e) { e.printStackTrace();
			
			}
		return u;
		}*/
	public Sale find(int id) {
		Connection conn=Utilitaire.getConnection();
		Sale s=null;
		try {
			PreparedStatement ps=conn.prepareStatement("select * from `sales` where ID=?");
			ps.setInt(1,id);
			ResultSet rsUser=ps.executeQuery();
			if(rsUser.next()){
				s=new Sale();
				s.setId(new Integer(rsUser.getInt("ID")));
				s.setProduct(rsUser.getString("product"));
				s.setRegion(rsUser.getString("region"));
				s.setDate(rsUser.getString("date"));
				s.setQuantite(new Integer(rsUser.getString("quantite")));
				s.setCost(Double.parseDouble(rsUser.getString("cost")));
				s.setAmt(Double.parseDouble(rsUser.getString("amt")));
				s.setTax(Double.parseDouble(rsUser.getString("tax")));
				s.setTotal(Double.parseDouble(rsUser.getString("total")));
				
				}
			} catch (SQLException e) { e.printStackTrace();
			
			}
		return s;
		}
	/*private int id;
	private String product;
	private String region;
	private String date;
	private int quantite;
	private double cost;
	private double amt;
	private double tax;
	private double total;*/
	public void addSale(int id , String product,String region,String date,int quantite,double cost, double amt,double tax,double total )
	{ 
		Connection conn=Utilitaire.getConnection();
		Sale s=null;
		try {
			PreparedStatement ps=conn.prepareStatement("INSERT INTO `sales`(`ID`, `product`, `region`, `date`, `quantite`, `cost`, `amt`, `tax`, `total`) VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setInt(1,id); ps.setString(2,product);ps.setString(3,region); ps.setString(4, date); ps.setInt(5,quantite);
			ps.setDouble(6,cost); ps.setDouble(7,amt);ps.setDouble(8,tax); ps.setDouble(9, total); 
			ps.executeUpdate();
			} catch (SQLException e2) { e2.printStackTrace();
			}
		}
	public Vector<Sale> selectAll(){
		Connection conn=Utilitaire.getConnection();
		Vector<Sale> sales=new Vector<Sale>();
		Sale s=null; 
		try {
			PreparedStatement ps=conn.prepareStatement("select * from `sales` ");
			ResultSet rsUser=ps.executeQuery();
			while(rsUser.next()){
				s=new Sale();
				s.setId(new Integer(rsUser.getInt("ID")));
				s.setProduct(rsUser.getString("product"));
				s.setRegion(rsUser.getString("region"));
				s.setDate(rsUser.getString("date"));
				s.setQuantite(new Integer(rsUser.getString("quantite")));
				s.setCost(Double.parseDouble(rsUser.getString("cost")));
				s.setAmt(Double.parseDouble(rsUser.getString("amt")));
				s.setTax(Double.parseDouble(rsUser.getString("tax")));
				s.setTotal(Double.parseDouble(rsUser.getString("total")));
				sales.add(s);
				
				
				}
			} catch (SQLException e) { e.printStackTrace();
			}
		return sales;
		}
	
	
	
	public void deleteSale(int id) {
		Connection conn=Utilitaire.getConnection();
		try {
			PreparedStatement ps=conn.prepareStatement("delete from `sales` where ID=? ");
			ps.setInt(1,id);
			ps.executeUpdate();
			} catch (SQLException e2) { e2.printStackTrace();
			}				}

	public void updateSale(Sale s) {
		Connection conn=Utilitaire.getConnection();
		try {
			PreparedStatement ps= conn.prepareStatement("update `sales` set product=?,region=? , date=? , quantite=? , cost=? ,amt=?,tax=?,total=? where ID=? ");
			ps.setString(1, s.getProduct());ps.setString(2, s.getRegion());ps.setString(3, s.getDate());
			ps.setInt(4, s.getQuantite());ps.setDouble(5, s.getCost());ps.setDouble(6, s.getAmt());ps.setDouble(7, s.getTax());ps.setDouble(8, s.getTotal());
			ps.setInt(9,s.getId());
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace();
		}
		}
	
	}
