package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class GetROSpecs implements Message {

	private static final long serialVersionUID = -5157679562068473342L;

	private MessageHeader messageHeader;

	public GetROSpecs() {
	}

	public GetROSpecs(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.GET_ROSPECS);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "GetROSpecs [messageHeader=" + messageHeader + "]";
	}
}