package ver1;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientFrame extends JFrame{
	
	private JTabbedPane tabPane;
	private JPanel mainPanel;
	
	// 로그인 창
	private  IndexPanel indexPanel;
	
	// 대기실 창
	private WaitingRoomPanel waitingRoomPanel;
	
	// 메세지 창
	private MessagePanel messagePanel;
	
	// 기능 인터페이스
	private CallBackClientService callBackClientService;
	
	public ClientFrame(CallBackClientService callBackClientService) {
		this.callBackClientService = callBackClientService;
		initObject();
		initSetting();
	}
	
	private void initObject() {
		indexPanel = new IndexPanel(callBackClientService);
		waitingRoomPanel = new WaitingRoomPanel(callBackClientService);
		messagePanel = new MessagePanel(callBackClientService);
		tabPane = new JTabbedPane(JTabbedPane.TOP);
		mainPanel = new JPanel();
	}
	
	private void initSetting() {
		setTitle(" ✿✿ Lilac Talk ✿✿ ");
		setSize(500, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(null);
		setContentPane(mainPanel);
		
		tabPane.setBounds(0, 0, getWidth(), getHeight());
		mainPanel.add(tabPane);
		
		indexPanel.setLayout(null);
		tabPane.addTab("로그인", null, indexPanel, null);
		
		tabPane.addTab("대기실", null, waitingRoomPanel, null);
		
		tabPane.addTab("채팅", null, messagePanel, null);
		
		setVisible(true);
	}

}
