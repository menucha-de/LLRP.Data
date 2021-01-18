package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class StartROSpec implements Message {

	private static final long serialVersionUID = -4952884739435458375L;

	private MessageHeader messageHeader;
	private long roSpecID;

	public StartROSpec() {
	}

	public StartROSpec(MessageHeader messageHeader, long roSpecID) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.START_ROSPEC);
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
		return "StartROSpec [messageHeader=" + messageHeader + ", roSpecID=" + roSpecID + "]";
	}
}