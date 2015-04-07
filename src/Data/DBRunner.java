//STEP 1. Import required packages
package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import Display.NoConnection;
public class DBRunner {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = General.JDBC_DRIVER;
	static final String DB_URL = General.DB_URL;
	static boolean warning = true;
	// Database credentials
	static final String USER = General.USER;
	static final String PASS = General.PASS;

	public static void selectData(List<Runner> runners) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT `Id`, `CardID`, `Name`, `E-mail`, `Start`, `End`, `time`, `modifiedDate` FROM `Runner`";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				
				Runner runner = new Runner();
				runner.setId(rs.getInt("Id"));
				runner.setCardID(rs.getString("CardID"));
				runner.setName(rs.getString("Name"));
				runner.setEmail(rs.getString("E-mail"));
				runner.setStart(rs.getString("Start"));
				runner.setEnd(rs.getString("End"));
				runner.setTime(rs.getString("time"));
 
				runners.add(runner);
			}

			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			if (warning) {
				NoConnection.set();
				warning = false;
			}
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye!");
	}// end main
}// end FirstExample