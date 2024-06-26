package ver1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WaitingRoomPanel extends JPanel implements ActionListener {

	private Image backgroundImage;
	private JPanel backgroundPanel;

	private JPanel userListPanel;
	private JPanel roomListPanel;
	private JPanel roomButtonPanel;
	private JPanel sendMessagePanel;

	private JList<String> userList;
	private JList<String> roomList;

	private JTextField inputSecretMsg;
	private JButton secretMsgButton;

	private JButton makeRoomButton;
	private JButton outRoomButton;
	private JButton enterRoomButton;

	private Vector<String> userIdVector = new Vector<>();
	private Vector<String> roomNameVector = new Vector<>();

	private CallBackClientService callBackClientService;

	public WaitingRoomPanel(CallBackClientService callBackClientService) {
		this.callBackClientService = callBackClientService;
		initObject();
		initSetting();
		initListener();
	}

	private void initObject() {
		backgroundImage = new ImageIcon("images/background_image.png").getImage();
		backgroundPanel = new JPanel();

		userListPanel = new JPanel();
		roomListPanel = new JPanel();
		roomButtonPanel = new JPanel();
		sendMessagePanel = new JPanel();

		userList = new JList<>();
		roomList = new JList<>();

		inputSecretMsg = new JTextField();
		secretMsgButton = new JButton("메세지 보내기");
		makeRoomButton = new JButton("방 만들기");
		outRoomButton = new JButton("방 나가기");
		enterRoomButton = new JButton("방 입장하기");
	}

	private void initSetting() {
		setSize(getWidth(), getHeight());
		setLayout(null);

		userListPanel.setBounds(50, 30, 380, 150);
		userListPanel.setBackground(Color.WHITE);
		userListPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3), "✿ 접속중인 유저 ✿"));

		userListPanel.add(userList);
		add(userListPanel);

		roomListPanel.setBounds(50, 210, 380, 150);
		roomListPanel.setBackground(Color.WHITE);
		roomListPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3), "✿ 방 목록 ✿"));
		roomListPanel.add(roomList);
		add(roomListPanel);

		roomButtonPanel.setBounds(50, 370, 380, 35);
		roomButtonPanel.setBackground(new Color(163, 132, 255));
		roomButtonPanel.setLayout(null);

		makeRoomButton.setBackground(Color.WHITE);
		makeRoomButton.setBounds(5, 5, 120, 25);
		makeRoomButton.setEnabled(false);

		outRoomButton.setBackground(Color.WHITE);
		outRoomButton.setBounds(130, 5, 120, 25);
		outRoomButton.setEnabled(false);

		enterRoomButton.setBackground(Color.WHITE);
		enterRoomButton.setBounds(255, 5, 120, 25);
		enterRoomButton.setEnabled(false);

		roomButtonPanel.add(makeRoomButton);
		roomButtonPanel.add(outRoomButton);
		roomButtonPanel.add(enterRoomButton);
		add(roomButtonPanel);

		inputSecretMsg.setBounds(10, 5, 360, 23);
		secretMsgButton.setBounds(10, 35, 360, 20);
		secretMsgButton.setBackground(Color.WHITE);
		secretMsgButton.setEnabled(false);

		sendMessagePanel.setBounds(50, 450, 380, 60);
		sendMessagePanel.setBackground(new Color(163, 132, 255));
		sendMessagePanel.setLayout(null);
		sendMessagePanel.add(inputSecretMsg);
		sendMessagePanel.add(secretMsgButton);
		add(sendMessagePanel);
	}

	private void initListener() {
		makeRoomButton.addActionListener(this);
		outRoomButton.addActionListener(this);
		secretMsgButton.addActionListener(this);
		enterRoomButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == secretMsgButton) {

			String msg = inputSecretMsg.getText();
			if (!msg.equals(null)) {
				callBackClientService.clickSendSecretMessageButton(msg);
				inputSecretMsg.setText("");
				userList.setSelectedValue(null, false);
			}
		} else if (e.getSource() == makeRoomButton) {

			String roomName = JOptionPane.showInputDialog("[ 방 이름 설정 ]");

			if (!roomName.equals(null)) {
				callBackClientService.clickMakeRoomButton(roomName);
			}

		} else if (e.getSource() == outRoomButton) {
			String roomName = roomList.getSelectedValue();
			callBackClientService.clickOutRoomButton(roomName);
			roomList.setSelectedValue(null, false);

		} else if (e.getSource() == enterRoomButton) {

			String roomName = roomList.getSelectedValue();
			callBackClientService.clickEnterRoomButton(roomName);
			roomList.setSelectedValue(null, false);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}

} // end of class
