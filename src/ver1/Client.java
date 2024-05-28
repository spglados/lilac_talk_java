package ver1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextArea;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Client implements CallBackClientService, ProtocolImpl {
	
	// 프레임 창
	private ClientFrame clientFrame;
	
	// Client의 화면 부분의 컴포넌트를 멤버변수로 가져와 담을 변수
	private JTextArea mainMessageBox;
	private JList<String> userList;
	private JList<String> roomList;
	private JButton enterRoomButton;
	private JButton makeRoomButton;
	private JButton outRoomButton;
	private JButton secretMessageButton;
	private JButton sendMessageButton;

	// 소켓 장치
	private Socket socket;
	
	// 입출력 장치
	private BufferedReader reader;
	private BufferedWriter writer;
	
	// 연결 주소
	private String ip;
	private int port;
	
	// 유저 정보
	private String id;
	private String myRoomName;
	
	// 토크나이저 사용 변수
	private String protocol;
	private String from;
	private String message;
	
	// 접속자 명단(userList), 방 명단(roomList)을 업데이트 하기 위한 문자열 벡터
	private Vector<String> userIdList = new Vector<>();
	private Vector<String> roomNameList = new Vector<>();
	
	// JOptionPane 사용할때 알림창의 아이콘 변경할 ImageIcon
	private ImageIcon icon = new ImageIcon("images/lilac1.jpg");
	
	public Client() {
		clientFrame = new ClientFrame(this);
		// Client의 화면 부분의 컴포넌트 가져오기
		// mainMessageBox = clientFrame.getMessagePanel().getmain
	}
	
	/**
	 *  ConnectServerButton을 눌렀을때 실행된다.
	 *  소켓연결 하는 connectNetWork(), 입출력 장치를 연결하는 connectIO() 호출
	 *  id를 서버로 보내고, 화면 부분의 제목을 변경하고, 버튼을 활성화한다.
	 */
	@Override
	public void clickConnectServerButton(String ip, int port, String id) {
		this.ip = ip;
		this.port = port;
		this.id = id;
		try {
			connectNetWork();
			connectIO();
			
			writer.write(id.trim() + "\n");
			writer.flush();
			clientFrame.setTitle("✿✿ Lilac Talk_" + id + "님 ✿✿");
			
			clientFrame.getIndexPanel().get
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void connectNetWork() {

	}
	
	private void connectIO() {
		
	}
	
	private void readThread() {
		
	}
	
	private void writer(String str) {
		
	}
	
	private void checkProtocol(String msg) {

	}
	
	@Override
	public void chatting() {
		
	}
	
	@Override
	public void secretMessage() {
		
	}
	
	@Override
	public void makeRoom() {
		
	}
	
	@Override
	public void madeRoom() {
		
	}
	
	@Override
	public void newRoom() {
		
	}
	
	@Override
	public void outRoom() {
		
	}
	
	@Override
	public void enterRoom() {
		
	}
	
	@Override
	public void newUser() {
		
	}
	
	@Override
	public void connectedUser() {
		
	}
	
	@Override
	public void clickSendMessageButton(String messageText) {
	}
	
	@Override
	public void clickSendSecretMessageButton(String msg) {
		
	}
	
	@Override
	public void clickMakeRoomButton(String roomName) {
		
	}
	
	@Override
	public void clickOutRoomButton(String roomName) {
		
	}
	
	@Override
	public void clickEnterRoomButton(String roomName) {
		
	}
	
	public static void main(String[] args) {
		new Client();
	}
	
}
