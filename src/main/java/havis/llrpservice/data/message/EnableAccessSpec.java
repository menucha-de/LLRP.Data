package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class EnableAccessSpec implements Message {

	private static final long serialVersionUID = -4739801538569981659L;

	private MessageHeader messageHeader;
	private long accessSpecID;

	public EnableAccessSpec() {
	}

	public EnableAccessSpec(MessageHeader messageHeader, long accessSpecID) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ENABLE_ACCESSSPEC);
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
		return "EnableAccessSpec [messageHeader=" + messageHeader + ", accessSpecID=" + accessSpecID + "]";
	}
}