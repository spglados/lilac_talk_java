package ver1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
	private ImageIcon icon = new ImageIcon("images/error1_clear.jpg");

	public Client() {
		clientFrame = new ClientFrame(this);
		// Client의 화면 부분의 컴포넌트 가져오기
		mainMessageBox = clientFrame.getMessagePanel().getMainMessageBox();
		userList = clientFrame.getWaitingRoomPanel().getUserList();
		roomList = clientFrame.getWaitingRoomPanel().getRoomList();
		enterRoomButton = clientFrame.getWaitingRoomPanel().getEnterRoomButton();
		makeRoomButton = clientFrame.getWaitingRoomPanel().getMakeRoomButton();
		outRoomButton = clientFrame.getWaitingRoomPanel().getOutRoomButton();
		secretMessageButton = clientFrame.getWaitingRoomPanel().getSecretMsgButton();
		sendMessageButton = clientFrame.getMessagePanel().getSendMessageButton();
	}

	/**
	 * ConnectServerButton을 눌렀을때 실행된다. 소켓연결 하는 connectNetWork(), 입출력 장치를 연결하는
	 * connectIO() 호출 id를 서버로 보내고, 화면 부분의 제목을 변경하고, 버튼을 활성화한다.
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

			clientFrame.getIndexPanel().getConnectButton().setEnabled(false);
			makeRoomButton.setEnabled(true);
			enterRoomButton.setEnabled(true);
			secretMessageButton.setEnabled(true);
			sendMessageButton.setEnabled(true);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "접속 에러 !", "알림", JOptionPane.ERROR_MESSAGE, icon);
		}
	}

	// 소켓 장치 연결
	private void connectNetWork() {
		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "접속 에러 !", "알림", JOptionPane.ERROR_MESSAGE, icon);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "접속 에러 !", "알림", JOptionPane.ERROR_MESSAGE, icon);
		}
	}

	// 입출력 장치 연결한 후, readThread()를 호출한다.
	private void connectIO() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			// 입력 스레드
			readThread();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "클라이언트 입출력 장치 에러 !", "알림", JOptionPane.ERROR_MESSAGE, icon);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "클라이언트 입출력 장치 에러 !", "알림", JOptionPane.ERROR_MESSAGE, icon);
		}
	}

	// 서버측의 요청을 받아올 Reader
	// 요청을 받아 msg에 저장하고, checkProtocol() 호출 한다.
	private void readThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						String msg = reader.readLine();

						checkProtocol(msg);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "클라이언트 입력 장치 에러 !", "알림", JOptionPane.ERROR_MESSAGE, icon);
						break;
					}
				}
			}
		}).start();
	}

	
	// 클라이언트 측에서 서버측으로 보내는 Writer
	// 서버측에서 readLine()으로 읽기 때문에, "\n" 엔터가 필요함
	private void writer(String str) {
		try {
			writer.write(str + "\n");
			writer.flush();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "클라이언트 출력 장치 에러 !", "알림", JOptionPane.ERROR_MESSAGE, icon );
		}
	}

	/**
	 * 
	 * 서버측에서 읽어온 요청을 프로토콜 별로 구별해서 해당 메소드를 호출한다.
	 * 요청이 문자열로 넘어오고 / 슬러시를 기준으로 문자열을 나눈다.
	 * protocol  ->  어떤 작업을 해야하는지 알려주는 약속
	 * from      ->  보내는 측이 담겨 있다.
	 * message   ->  보내고자 하는 메세지를 담는다.
	 * 
	 */
	private void checkProtocol(String msg) {
		StringTokenizer tokennizer = new StringTokenizer(msg, "/");
		
		protocol = tokennizer.nextToken();
		from = tokennizer.nextToken();
		
		if (protocol.equals("Chatting")) {
			message = tokennizer.nextToken();
			chatting();
		} else if (protocol.equals("SecretMessage")) {
			message = tokennizer.nextToken();
			secretMessage();
		} else if (protocol.equals("MakeRoom")) {
			makeRoom();
		} else if (protocol.equals("MadeRoom")) {
			madeRoom();
		} else if (protocol.equals("NewRoom")) {
			newRoom();
		} else if (protocol.equals("OutRoom")) {
			outRoom();
		} else if (protocol.equals("EnterRoom")) {
			enterRoom();
		} else if (protocol.equals("NewUser")) {
			newUser();
		} else if (protocol.equals("ConnectedUser")) {
			connectedUser();
		} else if (protocol.equals("EmptyRoom")) {
			roomNameList.remove(from);
			roomList.setListData(roomNameList);
			makeRoomButton.setEnabled(true);
			enterRoomButton.setEnabled(true);
			outRoomButton.setEnabled(false);
		} else if (protocol.equals("FailMakeRoom")) {
			JOptionPane.showMessageDialog(null, "같은 이름의 방이 존재합니다 !", "[알림]", JOptionPane.ERROR_MESSAGE, icon);
		} else if (protocol.equals("UserOut")) {
			userIdList.remove(from);
			userList.setListData(userIdList);
		}
	}

	// 프로토콜 인터페이스
	@Override
	public void chatting() {
		if (id.equals(from)) {
			mainMessageBox.append("[나] \n" + message + "\n");
		} else if (from.equals("입장")) {
			mainMessageBox.append("✿✿✿ " + from + " ✿✿✿" + message + "\n");
		} else if (from.equals("퇴장")) {
			mainMessageBox.append("XX " + from + " XX" + message + "\n");
		} else {
			mainMessageBox.append("✿ " + from + " ✿" + message + "\n");
		} 
	}

	@Override
	public void secretMessage() {
		JOptionPane.showMessageDialog(null, from + "님의 메세지\n\"" + message + "\"");
	}

	@Override
	public void makeRoom() {
		myRoomName = from;
		makeRoomButton.setEnabled(false);
		enterRoomButton.setEnabled(false);
		outRoomButton.setEnabled(false);
	}

	@Override
	public void madeRoom() {
		roomNameList.add(from);
		if (!(roomNameList.size() == 0)) {
			roomList.setListData(roomNameList);
		}
	}

	@Override
	public void newRoom() {
		roomNameList.add(from);
		roomList.setListData(roomNameList);
	}

	@Override
	public void outRoom() {
		myRoomName = null;
		mainMessageBox.setText("");
		makeRoomButton.setEnabled(true);
		enterRoomButton.setEnabled(true);
		outRoomButton.setEnabled(false);
	}

	@Override
	public void enterRoom() {
		myRoomName = from;
		makeRoomButton.setEnabled(false);
		enterRoomButton.setEnabled(false);
		outRoomButton.setEnabled(true);
	}

	@Override
	public void newUser() {
		if (!from.equals(this.id)) {
			userIdList.add(from);
			userList.setListData(userIdList);
		}
	}

	@Override
	public void connectedUser() {
		userIdList.add(from);
		userList.setListData(userIdList);
	}

	// 클라이언트 화면단에서 정보를 받아오는 콜백 인터페이스
	@Override
	public void clickSendMessageButton(String messageText) {
		writer("Chatting/" + myRoomName + "/" + messageText);
	}

	@Override
	public void clickSendSecretMessageButton(String msg) {
		String user = (String) clientFrame.getWaitingRoomPanel().getUserList().getSelectedValue();
		writer("SecretMessage/" + user + "/" + msg);
	}

	@Override
	public void clickMakeRoomButton(String roomName) {
		writer("MakeRoom/" + roomName);
	}

	@Override
	public void clickOutRoomButton(String roomName) {
		writer("OutRoom/" + roomName);
	}

	@Override
	public void clickEnterRoomButton(String roomName) {
		writer("EnterRoom/" + roomName);
	}

	// 메인
	public static void main(String[] args) {
		new Client();
	} 
}
