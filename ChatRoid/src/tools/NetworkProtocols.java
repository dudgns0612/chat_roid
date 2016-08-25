package tools;

public class NetworkProtocols {
	//Socket Info
	public final static int SERVER_PORT_INFO = 8080;
	public final static String SERVER_IP_INFO = "localhost";
	
	
	//Network Protocol
	public final static String MESSAGE_RECIEVER_PROTOCOL_REQUEST = "MSG_REC_PROTOCOL_REQ";
	public final static String MESSAGE_RECIEVER_PROTOCOL_RESPOND = "MSG_REC_PRITICOL_RES";
	
	public final static String WORD_STUDY_ONE_PROTOCOL_REQUEST = "STUDY_ONE_PROTOCOL_REQ";
	public final static String WORD_STUDY_ONE_PROTOCOL_RESPOND = "STUDY_ONE_PROTOCOL_RES";
	
	
	//FILE
	public final static String FILE_SERVER_OPEN_REQUEST = "FILE_STU_OPEN_REQ";
	public final static String FILE_SERVER_OPEN_RESPOND = "FILE_STU_OPEN_RES";
	
	public final static String FILE_SERVER_RECIEBER_TEXT_REQUEST = "FILE_SER_REC_BAS_REQ";
	public final static String FILE_SERVER_RECIEBER_TEXT_RESPOND = "FILE_SER_REC_BAS_REQ";
	
	public final static String FILE_SERVER_BASIC_SEND_REQUEST = "FILE_SER_BAS_SEND_REQ";
	public final static String FILE_SERVER_BASIC_SEND_RESPOND = "FILE_SER_BAS_SEND_RES";
	
	
	public final static String FILE_BASIC_STUDY_REQUEST = "FILE_BAS_STU_REQ";
	public final static String FILE_BASIC_STUDY_RESPOND = "FILE_BAS_STU_RES";
		
	public final static String FILE_SERVER_KAKAO_SEND_REQUEST = "FILE_SER_KAKAO_SEND_REQ";
	public final static String FILE_SERVER_KAKAO_SEND_RESPOND = "FILE_SER_KAKAO_SEND_RES";
	
	public final static String FILE_KAKAO_STUDY_REQUEST = "FILE_KAKAO_STU_REQ";
	public final static String FILE_KAKAO_STUDY_RESPOND = "FILE_KAKAO_STU_RES";
	
	//파일 전송 상태 요청
	public final static String FILE_SEND_STATE_REQUEST = "FILE_SEND_STATE_REQ";
	public final static String FILE_SEND_STATE_RESPOND = "FILE_SEND_STATE_RES";
	
	
	public final static String FILE_SEND_SUCCESS_REQUEST = "FILE_SEMD_SUCCESS_REQ";
	public final static String FILE_SEND_SUCCESS_RESPOND = "FILE_SEMD_SUCCESS_RES";
	
	public final static String LISTENER_CLOSE_REQUEST = "LISTENER_CLOSE_REQ";
	public final static String LISTENER_CLOSE_RESPOND = "LISTENER_CLOSE_RES";
	
	//욕설 포함 상태 전송
	public final static String WORD_SWEAR_INCLUDE_REQUEST = "WORD_SWEAR_INCLUDE_REQ";
	
	//파일 욕설 포함 상태 전송
	public final static String FILE_SWEAR_INCLUDE_REQUEST = "FILE_SWEAR_STATE_REQ";
	//public final static String FILE_SWEAR_STATE_RESPOND = "FILE_SWEAR_STATE_RES";
	
	public final static String SERVER_CONNECT_CHECK_REQUEST = "SERVER_CHECK_REQ";
	public final static String SERVER_CONNECT_CHECK_RESPOND = "SERVER_CHECK_RES";
	
	
	
}
