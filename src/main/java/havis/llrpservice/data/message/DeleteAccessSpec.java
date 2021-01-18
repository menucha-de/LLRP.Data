package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class DeleteAccessSpec implements Message {

	private static final long serialVersionUID = 6162181926856814926L;

	private MessageHeader messageHeader;
	private long accessSpecID;

	public DeleteAccessSpec() {
	}

	public DeleteAccessSpec(MessageHeader messageHeader, long accessSpecID) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.DELETE_ACCESSSPEC);
		this.accessSpecID = accessSpecID;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public long getAccessSpecID() {
		return accessSpecID;
	}

	public void setAccessSpecID(long accessSpecID) {
		this.accessSpecID = accessSpecID;
	}

	@Override
	public String toString() {
		return "DeleteAccessSpec [messageHeader=" + messageHeader + ", accessSpecID=" + accessSpecID + "]";
	}
}