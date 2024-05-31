package ver1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessagePanel extends JPanel {

	// 백그라운드 이미지 컴포넌트
	private Image backgroundImage;
	private JPanel backgroundPanel;

	// 패널
	private JPanel mainPanel;
	private JPanel bottomPanel;

	// 스크롤
	private ScrollPane scrollPane;

	// 텍스트 컴포넌트
	private JTextArea mainMessageBox;
	private JTextField writeMessageBox;

	// 메세지 보내기 버튼
	private JButton sendMessageButton;

	private CallBackClientService callBackClientService;

	public MessagePanel(CallBackClientService callBackClientService) {
		this.callBackClientService = callBackClientService;
		initObject();
		initSetting();
		initListener();
	}

	private void initObject() {
		backgroundImage = new ImageIcon("images/background_image.png").getImage();
		backgroundPanel = new JPanel();

		mainPanel = new JPanel();
		bottomPanel = new JPanel();

		scrollPane = new ScrollPane();

		mainMessageBox = new JTextArea();
		writeMessageBox = new JTextField(17);
		sendMessageButton = new JButton("전송");
	}

	private void initSetting() {
		setSize(getWidth(), getHeight());
		setLayout(null);

		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(bottomPanel);

		mainMessageBox.setEnabled(false);
		mainPanel.setBounds(50, 30, 380, 470);
		mainPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 5), "Message"));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.add(scrollPane);
		scrollPane.setBounds(55, 25, 360, 430);
		scrollPane.add(mainMessageBox);
		add(mainPanel);

		sendMessageButton.setBackground(Color.WHITE);
		sendMessageButton.setPreferredSize(new Dimension(90, 20));
		sendMessageButton.setEnabled(false);
		bottomPanel.setBounds(50, 520, 380, 35);
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.setBorder(new LineBorder(Color.BLACK, 2));
		bottomPanel.add(writeMessageBox);
		bottomPanel.add(sendMessageButton);
		add(bottomPanel);
	}

	private void initListener() {
		sendMessageButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendMessage();
			}
		});

		writeMessageBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});
	}

	private void sendMessage() {
		if (!writeMessageBox.getText().equals(null)) {
			String msg = writeMessageBox.getText();
			callBackClientService.clickSendMessageButton(msg);
			writeMessageBox.setText("");
			writeMessageBox.requestFocus();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}

}
