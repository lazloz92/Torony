package Display;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import CardReader.NFC;
import Data.CardData;
import Data.DBRunner;
import Data.Runner;
import Data.UserData;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {

	private JFrame frame;
	private JPanel fomenu;
	CardData cardData = new CardData();
	UserData userData = new UserData();
	NFC nfc;
	private JTextField txtName;
	private JTextField txtEmail;
	private JTextField cardID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CardLayout c1 = new CardLayout();

		fomenu = new JPanel();
		frame.getContentPane().add(fomenu, BorderLayout.CENTER);
		fomenu.setLayout(c1);

		JPanel registWindow = new JPanel();
		fomenu.add(registWindow, "registerWindow");
		registWindow
				.setLayout(new BoxLayout(registWindow, BoxLayout.PAGE_AXIS));

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		registWindow.add(panel_2);

		JButton btnReadCard = new JButton("Read card");
		btnReadCard.setVerticalAlignment(SwingConstants.TOP);
		btnReadCard.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(btnReadCard);

		cardID = new JTextField();
		panel_2.add(cardID);
		cardID.setColumns(10);
		
		JTextPane txtpnReadFail = new JTextPane();
		txtpnReadFail.setForeground(new Color(255, 0, 0));
		txtpnReadFail.setBackground(SystemColor.menu);
		txtpnReadFail.setEditable(false);
		txtpnReadFail.setText("Nem sikerült beolvasni a kártyát.");
		txtpnReadFail.setVisible(false);
		panel_2.add(txtpnReadFail);
		btnReadCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					nfc = new NFC();
					cardID.setText(nfc.readOneCard());
					cardID.setEnabled(false);
					txtpnReadFail.setVisible(false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					txtpnReadFail.setVisible(true);
				}
			}
		});

		JPanel panel_1 = new JPanel();
		registWindow.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
		
		JTextPane txtpnNv = new JTextPane();
		txtpnNv.setEditable(false);
		txtpnNv.setBackground(SystemColor.menu);
		txtpnNv.setText("Full name");
		panel_1.add(txtpnNv);

		txtName = new JTextField();
		txtName.setText("Name");
		panel_1.add(txtName);
		txtName.setColumns(25);

		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setHgap(30);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		registWindow.add(panel);
		
		JTextPane txtpnEmail = new JTextPane();
		txtpnEmail.setText("Email");
		txtpnEmail.setEditable(false);
		txtpnEmail.setBackground(SystemColor.menu);
		panel.add(txtpnEmail);

		txtEmail = new JTextField();
		txtEmail.setText("Email");
		panel.add(txtEmail);
		txtEmail.setColumns(25);

		JPanel panel_3 = new JPanel();
		registWindow.add(panel_3);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userData.put(cardID.getText(), txtName.getText(), txtEmail.getText());
				cardID.setEnabled(true);
				txtpnReadFail.setVisible(false);
				cardID.setText("");
				txtName.setText("");
				txtEmail.setText("");
			}
		});
		panel_3.add(btnSave);

		
		JMenuItem mntmRegistration = new JMenuItem("Registration");
		JMenuItem mntmEnd = new JMenuItem("End");
		JMenuItem mntmStart = new JMenuItem("Start");
		JMenuItem mntmConvertToXml = new JMenuItem("Convert to XML");
		
		JPanel startWindow = new JPanel();
		fomenu.add(startWindow, "startWindow");
		startWindow.setLayout(new BoxLayout(startWindow, BoxLayout.X_AXIS));

		JButton btnReadCards = new JButton("Read cards");
		btnReadCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nfc = new NFC(cardData,0);
				nfc.setLevel(0); // ezzel a start fog elindulni
				nfc.start();
				c1.show(fomenu, "stopWindow");
				mntmEnd.setEnabled(false);
				mntmStart.setEnabled(false);
			}
		});
		startWindow.add(btnReadCards);


		JTextPane txtpnHaARead = new JTextPane();
		txtpnHaARead
				.setText("Ha a Read cards gomra kattintasz, elkezdőik a kártyák olvasása és a starthoz rögzítésre kerülnek az időbélyegeik.");
		startWindow.add(txtpnHaARead);
		
		JPanel endWindow = new JPanel();
		fomenu.add(endWindow, "endWindow");
		endWindow.setLayout(new BoxLayout(endWindow, BoxLayout.X_AXIS));
		
		JButton button = new JButton("Read cards");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nfc = new NFC(cardData,1);
				nfc.setLevel(1); // ezzel az end fog elindulni.
				nfc.start();
				c1.show(fomenu, "stopWindow");
				mntmEnd.setEnabled(false);
				mntmStart.setEnabled(false);
			}
		});
		endWindow.add(button);
		
		JTextPane txtpnHaARead_1 = new JTextPane();
		txtpnHaARead_1.setText("Ha a Read cards gomra kattintasz, elkezdőik a kártyák olvasása, és rögzítésre kerülnek a célállomáshoz.");
		endWindow.add(txtpnHaARead_1);

		JPanel stopWindow = new JPanel();
		fomenu.add(stopWindow, "stopWindow");
		stopWindow.setLayout(new BoxLayout(stopWindow, BoxLayout.X_AXIS));
		JTextPane txtpnHaAStop = new JTextPane();
		txtpnHaAStop
				.setText("Ha a stop reading gombra kattintasz akkor megáll a kártyák beolvasása.");
		stopWindow.add(txtpnHaAStop);

		JButton btnStopReading = new JButton("Stop reading");
		btnStopReading.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nfc.done();
				
				//btnStopReading.setEnabled(false);
				String log = cardData.printCards();
				txtpnHaAStop.setText("A következő kártya logok történtek: \n" + log);
			}
		});
		stopWindow.add(btnStopReading);

		

		JPanel writexmlWindow = new JPanel();
		fomenu.add(writexmlWindow, "writexmlWindow");
		
		JPanel eredmeny = new JPanel();
		fomenu.add(eredmeny, "eredmeny");
		eredmeny.setLayout(new BoxLayout(eredmeny, BoxLayout.PAGE_AXIS));
		
		JTextPane textPane = new JTextPane();
		eredmeny.add(textPane);
		
		JButton btnEredmnyGenerls = new JButton("Eredmény generálás");
		btnEredmnyGenerls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Runner> runners = new ArrayList<Runner>();
				DBRunner.selectData(runners);
				
				for (Runner runner : runners){
					runner.getTime();
				}
				
				Collections.sort(runners, new Runner.OrderByTime());
				String log="";
				for (Runner runner : runners){
					System.out.print(runner.getCardID() + "   ");
					log+=runner.getCardID() + "   ";
					System.out.println(runner.getTime());
					log+=runner.getTime() + "\n";
				}
				textPane.setText(log);
			}
		});
		btnEredmnyGenerls.setAlignmentX(0.5f);
		eredmeny.add(btnEredmnyGenerls);
		


		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);


		
		mntmRegistration.setSelected(true);
		mnFile.add(mntmRegistration);
		mnFile.add(mntmStart);
		mnFile.add(mntmEnd);
		mnFile.add(mntmConvertToXml);
		
		JMenuItem mntmEredmny = new JMenuItem("Eredmény gen.");
		mntmEredmny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu, "eredmeny");
			}
		});
		mnFile.add(mntmEredmny);
		
		mntmRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c1.show(fomenu, "registerWindow");
			}
		});
		
		mntmStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu, "startWindow");
			}
		});
		
		mntmEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu,  "endWindow");
			}
		});
		
		mntmConvertToXml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu, "writexmlWindow");
			}
		});
		

	}

}
