package Data;

public class UserData {

	public UserData() {
		// TODO Auto-generated constructor stub
	}

	public void put(String cardID, String name, String email) {
		DbUser.insertData(cardID, name, email);
	}
}
