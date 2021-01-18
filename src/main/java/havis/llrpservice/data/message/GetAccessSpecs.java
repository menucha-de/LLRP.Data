package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class GetAccessSpecs implements Message {

	private static final long serialVersionUID = 397451965362577679L;

	private MessageHeader messageHeader;

	public GetAccessSpecs() {
	}

	public GetAccessSpecs(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.GET_ACCESSSPECS);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "GetAccessSpecs [messageHeader=" + messageHeader + "]";
	}
}