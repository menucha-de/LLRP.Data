package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class GetReport implements Message {

	private static final long serialVersionUID = 550614096675744381L;

	private MessageHeader messageHeader;

	public GetReport() {
	}

	public GetReport(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.GET_REPORT);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "GetReport [messageHeader=" + messageHeader + "]";
	}
}