package Data;

import java.util.Date;

public class UserData {

	int id;
	String cardID;
	String name;
	String email;
	Date start;
	Date end;
	
	public UserData() {
		// TODO Auto-generated constructor stub
	}

	public void put(String cardID, String name, String email) {
		DbUser.insertData(cardID, name, email); 
	}
	

}
