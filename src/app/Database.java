import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Database {
    public static final String TBL_INCOME = "sales";
    //public static final String TBL_EXPENSES = "tbl_expenses";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/BO1","root","");
    }

    public List<Sale> getData(String tbl_name) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "select * from " + tbl_name;

            try(ResultSet rsSale = connection.createStatement().executeQuery(query)) {
                List<Sale> list = new ArrayList<>();
                while (rsSale.next()) {
                    Sale data = new Sale();
                	data.setId(new Integer(rsSale.getInt("ID")));
    				data.setProduct(rsSale.getString("product"));
    				data.setAmt(rsSale.getDouble("amt"));
    				data.setCost(rsSale.getDouble("cost"));
    				data.setDate(rsSale.getString("date"));
    				data.setQuantite(rsSale.getInt("quantite"));
    				data.setRegion(rsSale.getString("region"));
    				data.setTax(rsSale.getDouble("tax"));
    				data.setTotal(rsSale.getDouble("total"));

                    list.add(data);
                }

                return list;
            }
        }
    }
}