package ver1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import lombok.Data;

@Data
public class ServerFrame extends JFrame {

	private Server mContext;

	// 스크롤패인
	private ScrollPane scrollPane; 

	// 백그라운드 패널
	private BackgroundPanel backgroundPanel;

	// 메인보드
	private JPanel mainPanel;
	private JTextArea mainBoard;

	// 포트패널
	private JPanel portPanel;
	private JLabel portLabel;
	private JTextField inputPort;
	private JButton connectButton;

	// Server mContext 추가 후 수정
	public ServerFrame(Server mContext) {
		this.mContext = mContext;
		initObject();
		initSetting();
		initListener();
	}

	private void initObject() {
		// 백그라운드 패널
		backgroundPanel = new BackgroundPanel();

		// 메인 패널
		mainPanel = new JPanel();
		mainBoard = new JTextArea();

		scrollPane = new ScrollPane();

		// 포트패널
		portPanel = new JPanel();
		portLabel = new JLabel("PORT NUMBER");
		inputPort = new JTextField(10);
		connectButton = new JButton("Connect");

		// 테스트 코드
		inputPort.setText("10000");
	}

	private void initSetting() {
		setTitle("** Lilac Talk ** - 서버관리자");
		setSize(500, 700);
		setLocationRelativeTo(null); // 화면 가운데 비치
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		// 백그라운드 패널
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);

		// 포트패널 컴포넌트
		// 조절 필요
		portPanel.setBounds(150, 30, 300, 50);
		portPanel.setBackground(new Color(0, 0, 0, 0));
		portPanel.add(portLabel);
		portPanel.add(inputPort);
		portPanel.add(connectButton);
		backgroundPanel.add(portPanel);

		// 메인패널 컴포넌트
		mainPanel.setBorder(new TitledBorder(new LineBorder(Color.black, 5), "Server"));
		mainPanel.setBounds(100, 100, 500, 500);
		mainPanel.setBackground(Color.WHITE);
		
		mainBoard.setEnabled(false); // 2번클릭 방지
		mainPanel.add(scrollPane);
		scrollPane.setBounds(105, 100, 480, 480);
		scrollPane.add(mainBoard);
		backgroundPanel.add(mainPanel);
		
		setVisible(true);
	}

	private void initListener() {
		connectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mContext.startServer();
			}
		});
	}

	// 내부 클래스
	private class BackgroundPanel extends JPanel {
		private JPanel backgroundPanel;
		private Image backgroundImage;

		public BackgroundPanel() {
			// 500 x 700
			backgroundImage = new ImageIcon("images/background500.png").getImage();
			backgroundPanel = new JPanel();
			add(backgroundPanel);
		}

		@Override
		public void paintComponents(Graphics g) {
			super.paintComponents(g);
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		}

	} // end of BackgroundPanel ( 내부 클래스 )

}