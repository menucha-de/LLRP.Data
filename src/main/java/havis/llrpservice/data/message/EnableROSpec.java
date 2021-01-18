package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class EnableROSpec implements Message {

	private static final long serialVersionUID = 8822261480038518231L;

	private MessageHeader messageHeader;
	private long roSpecID;

	public EnableROSpec() {
	}

	public EnableROSpec(MessageHeader messageHeader, long roSpecID) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ENABLE_ROSPEC);
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
		return "EnableROSpec [messageHeader=" + messageHeader + ", roSpecID=" + roSpecID + "]";
	}
}