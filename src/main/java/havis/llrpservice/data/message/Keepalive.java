package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class Keepalive implements Message {

	private static final long serialVersionUID = -8158135289876990403L;

	private MessageHeader messageHeader;

	public Keepalive() {
	}

	public Keepalive(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.KEEPALIVE);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "Keepalive [messageHeader=" + messageHeader + "]";
	}
}