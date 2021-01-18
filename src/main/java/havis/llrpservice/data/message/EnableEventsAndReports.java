package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class EnableEventsAndReports implements Message {

	private static final long serialVersionUID = -3887735177304172849L;

	private MessageHeader messageHeader;

	public EnableEventsAndReports() {
	}

	public EnableEventsAndReports(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ENABLE_EVENTS_AND_REPORTS);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "EnableEventsAndReports [messageHeader=" + messageHeader + "]";
	}
}