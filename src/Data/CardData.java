package Data;

import java.util.Map;
import java.util.TreeMap;

public class CardData {

	Map<String, String> cards = new TreeMap<String, String>();

	public CardData() {
		// TODO Auto-generated constructor stub
	}

	public void put(String key, String value, int i) {
		cards.put(key, value);

		if (i == 0)
			DbStart.insertData(key, value);
		else
			DbEnd.insertData(key, value);
	}

	public void printCards() {
		for (String key : cards.keySet()) {
			System.out.println(key + " : " + cards.get(key));
		}

	}

}
