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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JScrollBar;

public class Menu {

	private static final Logger logger = Logger.getLogger( Menu.class.getName() );
	
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
					LogManager.getLogManager().readConfiguration(new FileInputStream("mylogging.properties"));
					Handler fileHandler = new FileHandler("logger.xml");
					logger.addHandler(fileHandler);
				} catch (SecurityException | IOException e1 ) {
					e1.printStackTrace();
				} 
				logger.setLevel(Level.FINE);
				logger.addHandler(new ConsoleHandler());
				try {
					logger.log(Level.INFO,"Belépünk a main metódusba.");
					Menu window = new Menu();
					window.frame.setVisible(true);
			        logger.log(Level.INFO, "Ez az első log üzenet.");					
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

		JButton btnStopReading = new JButton("Stop reading");
		JMenuItem mntmStart = new JMenuItem("Start");
		JMenuItem mntmEnd = new JMenuItem("Cél");

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
					logger.log(Level.INFO, "Egy kártya olvasása kezdődik");
					nfc = new NFC();
					String cardId = nfc.readOneCard();
					cardID.setText(cardId);
					logger.log(Level.FINE, "A " + cardId + " kártya azonosító beolvasásra és kiírásra került.");
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
				userData.put(cardID.getText(), txtName.getText(),
						txtEmail.getText());
				cardID.setEnabled(true);
				txtpnReadFail.setVisible(false);
				cardID.setText("");
				txtName.setText("");
				txtEmail.setText("");
			}
		});
		panel_3.add(btnSave);

		JPanel startWindow = new JPanel();
		fomenu.add(startWindow, "startWindow");
		startWindow.setLayout(new BoxLayout(startWindow, BoxLayout.X_AXIS));

		JButton btnReadCards = new JButton("Start - kezdőpont");
		btnReadCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nfc = new NFC(cardData, 0);
				nfc.setLevel(0); // ezzel a start fog elindulni
				nfc.start();
				c1.show(fomenu, "stopWindow");
				btnStopReading.setEnabled(true);
				mntmStart.setEnabled(false);
				mntmEnd.setEnabled(false);
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

		JButton btnStartCllloms = new JButton("Start - célállomás");
		btnStartCllloms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nfc = new NFC(cardData, 1);
				nfc.setLevel(1); // ezzel az end fog elindulni.
				nfc.start();
				c1.show(fomenu, "stopWindow");
				btnStopReading.setEnabled(true);
				mntmStart.setEnabled(false);
				mntmEnd.setEnabled(false);
			}
		});
		endWindow.add(btnStartCllloms);

		JTextPane txtpnHaARead_1 = new JTextPane();
		txtpnHaARead_1
				.setText("Ha a Read cards gomra kattintasz, elkezdőik a kártyák olvasása, és rögzítésre kerülnek a célállomáshoz.");
		endWindow.add(txtpnHaARead_1);

		JPanel stopWindow = new JPanel();
		fomenu.add(stopWindow, "stopWindow");
		stopWindow.setLayout(new BoxLayout(stopWindow, BoxLayout.X_AXIS));
		JTextPane txtpnHaAStop = new JTextPane();
		txtpnHaAStop
				.setText("Ha a stop reading gombra kattintasz akkor megáll a kártyák beolvasása.");
		stopWindow.add(txtpnHaAStop);

		btnStopReading.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nfc.done();

				btnStopReading.setEnabled(false);
				String log = cardData.printCards();
				txtpnHaAStop.setText("A következő kártya logok történtek, az időbélyegek csak tájékoztató jellegűek: \n"
						+ log);
			}
		});
		stopWindow.add(btnStopReading);

		JPanel writexmlWindow = new JPanel();
		fomenu.add(writexmlWindow, "writexmlWindow");

		JPanel eredmeny = new JPanel();
		fomenu.add(eredmeny, "eredmeny");
		eredmeny.setLayout(new BoxLayout(eredmeny, BoxLayout.PAGE_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		eredmeny.add(scrollPane);
		
		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);

		JButton btnEredmnyGenerls = new JButton("Eredmény generálás");
		btnEredmnyGenerls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Runner> runners = new ArrayList<Runner>();
				DBRunner.selectData(runners);

				Collections.sort(runners, new Runner.OrderByTime());
				String log = "";
				for (Runner runner : runners) {
					if (runner.getName() == null) {
						System.out.print(runner.getCardID() + "\t");
						log += runner.getCardID() + "\t";
					} else {
						System.out.print(runner.getName() + "\t");
						log += runner.getName() + "\t";
					}

					System.out.println(runner.getTime());
					log += runner.getTime() + "\n";
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

		JMenuItem mntmRegistration = new JMenuItem("Regisztráció");
		mnFile.add(mntmRegistration);

		mntmRegistration.setSelected(true);

		mntmRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c1.show(fomenu, "registerWindow");
			}
		});

		mnFile.add(mntmStart);

		mnFile.add(mntmEnd);

		JMenuItem mntmEredmny = new JMenuItem("Eredmény");
		mnFile.add(mntmEredmny);
		mntmEredmny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu, "eredmeny");
			}
		});

		mntmEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu, "endWindow");
			}
		});

		mntmStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu, "startWindow");
			}
		});

	}

}
