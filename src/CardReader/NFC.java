package CardReader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardNotPresentException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import Data.CardData;
import Display.WrongCardReading;

public class NFC extends Thread {
	private volatile Thread blinker;
	CardData cards;
	int level;

	public NFC(){
		
	}
	
	
	public NFC(CardData cards,int level) {
		super();
		this.cards = cards;
		this.level = level;
	}

	public void run() {
		Thread thisThread = Thread.currentThread();
		blinker = Thread.currentThread();
		try {
		
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();
			CardTerminal terminal = terminals.get(0);
			do {

				try{
					String kartya = cardRead(terminal);
					if(kartya=="6300"){
						throw new CardException(kartya);
					}
					cards.put(kartya,getCurrentTimeStamp(),level); 
				}
				catch(CardNotPresentException e){
					System.out.println("nincs kártya vagy befejeztük a kártya olvasást");
					//WrongCardReading.set();
				}
				catch(CardException e2){
					e2.printStackTrace();
					WrongCardReading.set();
				}
				
				
			} while (blinker == thisThread);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void done() {
		blinker = null;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public String readOneCard() throws Exception{
		try {
			blinker = Thread.currentThread();
			// Display the list of terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();

			// Use the first terminal
			CardTerminal terminal = terminals.get(0);
			return cardRead(terminal);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			throw e1;
		}
	}

	private String cardRead(CardTerminal terminal) throws Exception {
		try {
			Thread thisThread = Thread.currentThread();

			while (!terminal.waitForCardPresent(1000) && blinker == thisThread );
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
				"HH:mm:ss.SSS");
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