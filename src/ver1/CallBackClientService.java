package ver1;

// Client의 화면 부분에서 이벤트가 일어났을때 데이터를 가지고 오기 위한 콜백 인터페이스
public interface CallBackClientService {
	void clickConnectServerButton(String ip, int port, String id);
	
	void clickSendMessageButton(String messageText);
	
	void clickSendSecretMessageButton(String msg);
	
	void clickMakeRoomButton(String roomName);
	
	void clickOutRoomButton(String roomName);
	
	void clickEnterRoomButton(String roomName);
}
