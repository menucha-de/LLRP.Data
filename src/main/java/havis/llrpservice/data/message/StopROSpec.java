package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class StopROSpec implements Message {

	private static final long serialVersionUID = 4641201129811904045L;

	private MessageHeader messageHeader;
	private long roSpecID;

	public StopROSpec() {
	}

	public StopROSpec(MessageHeader messageHeader, long roSpecID) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.STOP_ROSPEC);
		this.roSpecID = roSpecID;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public long getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(long roSpecID) {
		this.roSpecID = roSpecID;
	}

	@Override
	public String toString() {
		return "StopROSpec [messageHeader=" + messageHeader + ", roSpecID=" + roSpecID + "]";
	}
}