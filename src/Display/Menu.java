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

import CardReader.RFID;
import Data.CardData;
import Data.UserData;

public class Menu {

	private JFrame frame;
	private JPanel fomenu;
	CardData cardData = new CardData();
	UserData userData = new UserData();
	RFID rfid = new RFID(cardData);
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
		cardID.setEditable(false);
		panel_2.add(cardID);
		cardID.setColumns(10);
		btnReadCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				cardID.setText(rfid.readOneCard());
			}
		});

		JPanel panel_1 = new JPanel();
		registWindow.add(panel_1);

		txtName = new JTextField();
		txtName.setText("Name");
		panel_1.add(txtName);
		txtName.setColumns(20);

		JPanel panel = new JPanel();
		registWindow.add(panel);

		txtEmail = new JTextField();
		txtEmail.setText("Email");
		panel.add(txtEmail);
		txtEmail.setColumns(20);

		JPanel panel_3 = new JPanel();
		registWindow.add(panel_3);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userData.put(cardID.getText(), txtName.getText(), txtEmail.getText());
			}
		});
		panel_3.add(btnSave);

		JPanel startWindow = new JPanel();
		fomenu.add(startWindow, "startWindow");
		startWindow.setLayout(new BoxLayout(startWindow, BoxLayout.X_AXIS));

		JButton btnReadCards = new JButton("Read cards");
		btnReadCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rfid.start();
				c1.show(fomenu, "endWindow");
			}
		});
		startWindow.add(btnReadCards);

		// a startwindow-val kezdünk.
		c1.show(fomenu, "startWindow");

		JTextPane txtpnHaARead = new JTextPane();
		txtpnHaARead
				.setText("Ha a Read cards gomra kattintasz, elkezdőik a kártyák olvasása");
		startWindow.add(txtpnHaARead);

		JPanel endWindow = new JPanel();
		fomenu.add(endWindow, "endWindow");
		endWindow.setLayout(new BoxLayout(endWindow, BoxLayout.X_AXIS));

		JButton btnStopReading = new JButton("Stop reading");
		btnStopReading.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rfid.done();
				cardData.printCards();
			}
		});
		endWindow.add(btnStopReading);

		JTextPane txtpnHaAStop = new JTextPane();
		txtpnHaAStop
				.setText("Ha a stop reading gombra kattintasz akkor megáll a kártyák beolvasása.");
		endWindow.add(txtpnHaAStop);

		JPanel writexmlWindow = new JPanel();
		fomenu.add(writexmlWindow, "writexmlWindow");

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmRegistration = new JMenuItem("Registration");
		mntmRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c1.show(fomenu, "registerWindow");
			}
		});
		mnFile.add(mntmRegistration);

		JMenuItem mntmStart = new JMenuItem("Start");
		mntmStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu, "startWindow");
			}
		});
		mnFile.add(mntmStart);

		JMenuItem mntmConvertToXml = new JMenuItem("Convert to XML");
		mntmConvertToXml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c1.show(fomenu, "writexmlWindow");
			}
		});
		mnFile.add(mntmConvertToXml);
	}

}
