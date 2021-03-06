//STEP 1. Import required packages
package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Display.NoConnection;

public class DbUser {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = General.JDBC_DRIVER;
	static final String DB_URL = General.DB_URL;

	// Database credentials
	static final String USER = General.USER;
	static final String PASS = General.PASS;

	public static void insertData(String CardID, String Name, String Email) {
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
			sql = "SELECT `Id`, `CardID`, `Name`, `E-mail`, `Start`, `End`, `time`, `modifiedDate` FROM `Runner` where CardID ='"
					+ CardID + "' and Name is null";
			ResultSet isnull = stmt.executeQuery(sql);
			isnull.beforeFirst();
			boolean bolla=!isnull.next();

			sql = "SELECT `Id`, `CardID`, `Name`, `E-mail`, `Start`, `End`, `time`, `modifiedDate` FROM `Runner` where CardID ='"
					+ CardID + "' and Name is not null";
			ResultSet isnotnull = stmt.executeQuery(sql);
			isnotnull.beforeFirst();
			boolean bol=!isnotnull.next();

			if (bol) {
				if (bolla) {
					System.out.println("no data");

					// create the java mysql update preparedstatement
					String query = "INSERT INTO `Runner`(`CardID`, `Name`, `E-mail`) VALUES ( ? , ? , ? )";
					PreparedStatement preparedStmt = conn
							.prepareStatement(query);
					preparedStmt.setString(1, CardID);
					preparedStmt.setString(2, Name);
					preparedStmt.setString(3, Email);
					

					// execute the java preparedstatement
					preparedStmt.executeUpdate();

				} else {
					System.out.println("no Name");
					// create the java mysql update preparedstatement
					String query = "UPDATE `Runner` SET Name = ?, `E-mail` = ? where CardID = ?";
					PreparedStatement preparedStmt = conn.prepareStatement(query);
					preparedStmt.setString(1, Name);
					preparedStmt.setString(2, Email);
					preparedStmt.setString(3, CardID);
				

					// execute the java preparedstatement
					preparedStmt.executeUpdate();
				}
			}
			
			// STEP 6: Clean-up environment
			isnotnull.close();
			isnull.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
			
			NoConnection.set();

		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
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