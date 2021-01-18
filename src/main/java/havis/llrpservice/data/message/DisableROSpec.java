package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class DisableROSpec implements Message {

	private static final long serialVersionUID = 2070239726381179570L;

	private MessageHeader messageHeader;
	private long roSpecID;

	public DisableROSpec() {
	}

	public DisableROSpec(MessageHeader messageHeader, long roSpecID) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.DISABLE_ROSPEC);
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
		return "DisableROSpec [messageHeader=" + messageHeader + ", roSpecID=" + roSpecID + "]";
	}
}