package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class KeepaliveAck implements Message {

	private static final long serialVersionUID = 1438500672864945450L;

	private MessageHeader messageHeader;

	public KeepaliveAck() {
	}

	public KeepaliveAck(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.KEEPALIVE_ACK);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "KeepaliveAck [messageHeader=" + messageHeader + "]";
	}
}