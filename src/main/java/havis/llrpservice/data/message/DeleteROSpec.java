package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class DeleteROSpec implements Message {

	private static final long serialVersionUID = 5776731607795340509L;

	private MessageHeader messageHeader;
	private long roSpecID;

	public DeleteROSpec() {
	}

	public DeleteROSpec(MessageHeader messageHeader, long roSpecID) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.DELETE_ROSPEC);
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
		return "DeleteROSpec [messageHeader=" + messageHeader + ", roSpecID=" + roSpecID + "]";
	}
}