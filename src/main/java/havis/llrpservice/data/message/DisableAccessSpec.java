package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class DisableAccessSpec implements Message {

	private static final long serialVersionUID = -2977960357820107514L;

	private MessageHeader messageHeader;
	private long accessSpecID;

	public DisableAccessSpec() {
	}

	public DisableAccessSpec(MessageHeader messageHeader, long accessSpecID) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.DISABLE_ACCESSSPEC);
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
		return "DisableAccessSpec [messageHeader=" + messageHeader + ", accessSpecID=" + accessSpecID + "]";
	}
}