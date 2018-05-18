package pl.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import pl.dbtool.dataService.DBService;
import pl.models.Test;

public class RunApp {

	static DBService<Test> dao = new DBService<>(Test.class);
	
	public static void main(String[] args) {
//		Test test = new Test();
//		test.setId(1);
//		test.setTitle("tutul");
//		test.setName("nazwa");
//		dao.save(test);
//		dao.update(test);
//		dao.remove(test);
//		
//		Test item = dao.getById(1);
//		System.out.println("getId " + item.getId());
//		System.out.println("getName " + item.getName());
//		System.out.println("getTitle " + item.getTitle());
//		
//		List<Test> list = dao.getAll();
//		for (Test xxx : list) {
//			System.out.println("xxx name " + xxx.getName());
//			System.out.println("xxx title " + xxx.getTitle());
		
		
//		}
		


		

	    try {
			String driver = "com.mysql.jdbc.Driver";
		    String connection = "jdbc:mysql://localhost:3306/test?useSSL=false";
		    String user = "root";
		    String password = "root";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(connection, user, password);
			if (conn != null) {
				System.out.println("Po³¹czony!");
			} else {
				System.out.println("Nieprawid³owe po³¹czenie!");
			}
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
			while (rs.next()) {
				String id = rs.getString("enabled");
				String username = rs.getString("username");
				System.out.println("id " + id + " user: " + username);
			}
			conn.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	   
	    
	}
	
}
	