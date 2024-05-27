import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;

import lombok.Data;

@Data
public class Server {

	// 배열가변 추가 삭제 자유롭게하기 위해서
	// 접속된 유저 벡터
	private Vector<ConnectedUser> connectedUsers = new Vector<>();
	// 만들어진 방 벡터
	private Vector<MyRoom> madeRooms = new Vector<>();

	// 프레임 창
	private ServerFrame serverFrame;

	private JTextArea mainBoard;

	// 소켓 장치
	private ServerSocket serverSocket;
	private Socket socket;

	// 방 만들기 같은 방 이름 체크
	private boolean roomCheck;

	private String protocol;
	private String from;
	private String message;

	private ImageIcon icon = new ImageIcon("images/lilac1.jpg");

	public Server() {
		serverFrame = new ServerFrame(this);
		roomCheck = true;
		mainBoard = serverFrame.getMainBoard();
	}

	// 포트번호 입력 -> 버튼누르기 -> 서버 시작
	public void startServer() {
		// 서버 소켓 장치
		try {
			serverSocket = new ServerSocket(5000);
			// 파일에 알림저장 들어갈 위치
			serverFrame.getConnectButton().setEnabled(false);
			connectClient(); // 클라이언트 연결

		} catch (IOException e) {
			// 임시로 프린트스택트레이스
			e.printStackTrace();
		}
	}

	private void connectClient() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {

						// 소켓 장치
						socket = serverSocket.accept();
						serverViewAppendWriter("[알림] 사용자 접속 대기 \n");

						// 연결을 대기 하다가 유저가 들어오면 유저 생성, 소켓으로 유저 구분 가능
						connectedUsers user = new ConnectedUser(socket);
						user.start();
					} catch (IOException e) {
						// 임시로 프린트스택트레이스
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	// 전체 접속된 유저에게 출력하는 것 ( 알람 공지 접속 퇴장 생성??)
	private void broadCast(String msg) {
		for (int i = 0; i < connectedUsers.size(); i++) {
			connectedUsers user = connectedUsers.elementAt(i);
			user.writer(msg);
		}
	}

	private void serverViewAppendWriter(String str) {

	}

	private class ConnectedUser extends Thread implements ProtocolImpl {
		// 소켓 장치
		private Socket socket;

		// 입출력 장치
		private BufferedReader reader; // 입력
		private BufferedWriter writer; // 출력

		// 유저 정보
		private String id; // 아이디
		private String myRoomName; // 접속한 방번호

		@Override
		public void ConnectedUser(Socket socket) {
			this.socket = socket;
			connectIO();
		}

		// 입출력장치 연결 BufferedReader/Writer
		private void connectIO() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				sendInfomation();
			} catch (IOException e) {
				// 임시로 프린트스택트레이스
				e.printStackTrace();
			}
		}

		// 정보 보내기
		private void sendInfomation() {
			try {
				// 유저 아이디 가져 오기
				id = reader.readLine();
				serverViewAppendWriter(id + "님 접속");

				// 접속된 유저들어게 명단 업데이트를 위한 출력
				newUser();

				// 새로운 유저측에서 유저 명단 업데이트를 위한 출력
				connectedUser();

				// 새로운 유저측에서 룸 명단 업데이트를 위한 출력
				madeRoom();

			} catch (Exception e) {
				// 임시로 프린트스택트레이스
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					String str = reader.readLine();
					checkProtocol(str);
				}
			} catch (Exception e) {
				// 임시로 프린트스택트레이스
				e.printStackTrace();
			}
		}

		private void checkProtocol(String str) {
			StringTokenizer tokenizer = new StringTokenizer(str, "/");
			
			protocol = tokenizer.nextToken();
			from = tokenizer.nextToken();
			
			if(protocol.equals("Chatting")) {
				message = tokenizer.nextToken();
				chatting();
			} else if (protocol.equals("SecretMessage")) {
				message = tokenizer.nextToken();
				secretMessage();
			} else if (protocol.equals("MakeRoom")) {
				makeRoom();
			} else if (protocol.equals("OutRoom")) {
				outRoom();
			} else if (protocol.equals("EnterRoom")) {
				enterRoom();
			}
		}
		
		// 클라이언트 측으로 보내는 응답
		private void writer(String str) {
			try {
				writer.write(str + "\n");
				writer.flush();
			} catch (Exception e) {
				// 임시로 프린트스택트레이스
				e.printStackTrace();
			}
		}
		
		@Override
		public void chatting() {
			serverViewAppendWriter("[메세지]" + from + "-" + message + "\n");
			
			for (int i = 0; i < madeRooms.size(); i++) {
				MyRoom myRoom = madeRooms.elementAt(i);
				
				if(myRoom.roomName.equals(from)) {
					myRoom.roomBroadCast("Chatting/" + id + "/" + message);
				}
			}
		}
		
		@Override
		public void secretMessage() {
			serverViewAppendWriter("[비밀 메세지]" + id + "->" + from + "-" + message + "\n");
		
			for (int i = 0; i < connectedUsers.size(); i++) {
				connectedUsers user = connectedUsers.elementAt(i);
				
				if (user.id.equals(from)) {
					user.writer("SecetMessage/" + id + "/" + message);
				}
			}
		}
		
		@Override
		public void makeRoom() {
			for (int i = 0; i <madeRooms.size(); i++) {
				MyRoom room = madeRooms.elementAt(i);
				
				if(room.roomName.equals(from)) {
					writer("FailMakeRoom/" + from);
					serverViewAppendWriter("[방 생성 실패]" + id + "-" + from + "\n");
					roomCheck = false;
				} else {
					roomCheck = true;
				}
			}
			
			if (roomCheck) {
				myRoomName = from;
				MyRoom myRoom = new MyRoom(from, this);
				madeRooms.add(myRoom);
				serverViewAppendWriter("[방 생성]" + id + "-" + from + "\n");
				
				newRoom();
				writer("MakeRoom/" + from);
			}
			
		}
		
		@Override
		public void newRoom() {
			broadCast("NewRoom/" + from);
		}
		
		@Override
		public void outRoom() {
			for(int i = 0; i < madeRooms.size(); i++) {
				MyRoom myRoom = madeRooms.elementAt(i);
				
				if ()
			}
			
		}
		

	} // end of ConnectedUser

	public static void main(String[] args) {
		new Server();
	}

}
