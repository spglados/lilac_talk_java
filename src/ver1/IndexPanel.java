package ver1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
public class IndexPanel extends JPanel{
	
	// 백그라운드 이미지 컴포넌트
	private Image backgroundImage;
	private JPanel backgroundPanel;
	
	// 보더 컴포넌트
	private JPanel borderPanel;
	
	// ip 컴포넌트
	private JPanel ipPanel;
	private JLabel ipLabel;
	private JTextField inputIp;
	
	// port 컴포넌트
	private JPanel portPanel;
	private JLabel portLabel;
	private JTextField inputPort;
	
	// id 컴포넌트
	private JPanel idPanel;
	private JLabel idLabel;
	private JTextField inputId;
	
	// 로그인 버튼
	private JButton connectButton;
	
	private CallBackClientService callBackClientService;
	
	public IndexPanel(CallBackClientService callBackClientService) {
		this.callBackClientService = callBackClientService;
		initObject();
		initSetting();
		initListener();
	}
	
	private void initObject() {
		// 백그라운드 이미지 컴포넌트
		backgroundImage = new ImageIcon("images/background_image.png").getImage();
		backgroundPanel = new JPanel();
		
		// 보더 컴포넌트
		borderPanel = new JPanel();
		
		// IP 컴포넌ㅌ
		ipPanel = new JPanel();
		ipLabel = new JLabel("HOST IP");
		inputIp = new JTextField(10);
		
		// PORT 컴포넌트
		portPanel = new JPanel();
		portLabel = new JLabel("PORT NUMNER");
		inputPort = new JTextField(10);
		
		// ID 컴포넌트
		idPanel = new JPanel();
		idLabel = new JLabel();
		inputId = new JTextField(10);
		
		// 로그인 버튼
		connectButton = new JButton("Connect");
	}
	
	private void initSetting() {
		setSize(getWidth(), getHeight());
		setLayout(null);
		
		// 백그라운드 이미지 패널
		backgroundPanel.setSize(getWidth(), getHeight());
		backgroundPanel.setLayout(null);
		add(backgroundPanel);
		
		// 보더 컴포넌트
		// 위치크기 수정 필요
		borderPanel.setBounds(100, 60, 190, 380);
		borderPanel.setLayout(null);
		borderPanel.setBackground(Color.white);
		borderPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 5), "Login"));
		add(backgroundPanel);
		
		// IP 컴포넌트
		// 위치크기 수정 필요
		ipPanel.setBounds(30, 40, 120, 100);
		ipPanel.setBackground(new Color(0,0,0,0));
		ipPanel.add(ipLabel);
		ipPanel.add(inputIp);
		borderPanel.add(ipPanel);
		
		// PORT 컴포넌트
		// 위치크기 수정 필요
		portPanel.setBounds(30, 140, 120, 100);
		portPanel.setBackground(new Color(0,0,0,0));
		portPanel.add(portLabel);
		portPanel.add(inputPort);
		borderPanel.add(portPanel);
		
		// ID 컴포넌트
		// 위치크기 수정 필요
		idPanel.setBounds(30, 40, 120, 100);
		idPanel.setBackground(new Color(0,0,0,0));
		idPanel.add(idLabel);
		idPanel.add(inputId);
		borderPanel.add(idPanel);
		
		// LoginButton 컴포넌트
		// 위치크기 수정 필요
		connectButton.setBackground(Color.WHITE);
		connectButton.setBounds(30, 340, 120, 20);
		borderPanel.add(connectButton);
		
		// 테스트 코드
		inputIp.setText("128.0.0.1");
		inputPort.setText("10000");
	}
	
	private void initListener() {
		connectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clickConnectButton();
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					clickConnectButton();
				}
			}
		});
	}
	
	// 각 입력칸이 null이 아닐때에 실행된다.
	// 각 입력칸의 Text를 ( ip, port, id) 가지고 와서 메소드 호출
	private void clickConnectButton() {
		if((!inputIp.getText().equals(null)) && (!inputPort.getText().equals(null))
				&& (!inputId.getText().equals(null))) {
			
			String ip = inputIp.getText();
			String stringPort = inputPort.getText();
			int port = Integer.parseInt(stringPort);
			String id = inputId.getText();
			
			callBackClientService.clickConnectServerButton(ip, port, id);
		} else {
			JOptionPane.showMessageDialog(null, "입력한 정보를 확인하세요", "알림", JOptionPane.INFORMATION_MESSAGE );
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}

}
