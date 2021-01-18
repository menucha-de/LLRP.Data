package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class CloseConnection implements Message {

	private static final long serialVersionUID = 7461486199764026506L;

	private MessageHeader messageHeader;

	public CloseConnection() {
	}

	public CloseConnection(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.CLOSE_CONNECTION);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "CloseConnection [messageHeader=" + messageHeader + "]";
	}
}