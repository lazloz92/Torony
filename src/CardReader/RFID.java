package CardReader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import Data.CardData;

public class RFID extends Thread {
	private volatile Thread blinker;
	CardData cards;

	public RFID(CardData cards) {
		super();
		this.cards = cards;
	}

	public void run() {
		Thread thisThread = Thread.currentThread();
		blinker = Thread.currentThread();
		try {
			// Display the list of terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();

			// Use the first terminal
			CardTerminal terminal = terminals.get(0);
			do {
				/////itt figyelj
				
				
				
				cards.put(cardRead(terminal), getCurrentTimeStamp(),0); //// itt azért van nulla mert akkor a start indul el.
				
				/////
				
				
				
				
				/////
			} while (blinker == thisThread);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void done() {
		blinker = null;
	}
	
	public String readOneCard(){
		try {
			// Display the list of terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();

			// Use the first terminal
			CardTerminal terminal = terminals.get(0);
			return cardRead(terminal);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
		
	}

	private String cardRead(CardTerminal terminal) throws Exception {
		try {

			while (!terminal.waitForCardPresent(1000))
				;
			// Connect with the card
			Card card = terminal.connect("*");
			CardChannel channel = card.getBasicChannel();

			// Send Select Applet command
			byte[] aid = { (byte) 0xFF, (byte) 0xCA, 0x00, 0x00, 0x04 };
			ResponseAPDU answer = channel.transmit(new CommandAPDU(aid));

			byte r[] = answer.getBytes();

			while (terminal.isCardPresent()) {
				Thread.sleep(100);
			};
			// Disconnect the card
			card.disconnect(false);

			return bytesToHex(r);
		} catch (Exception e) {
			throw e;
		}

	}

	private String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	final private char[] hexArray = "0123456789ABCDEF".toCharArray();

	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[8];
		for (int j = 0; j < bytes.length && j < 4; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}