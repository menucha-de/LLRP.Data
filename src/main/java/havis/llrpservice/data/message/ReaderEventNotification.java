package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.ReaderEventNotificationData;

public class ReaderEventNotification implements Message {

	private static final long serialVersionUID = 7712936725490484197L;

	private MessageHeader messageHeader;
	private ReaderEventNotificationData readerEventNotificationData;

	public ReaderEventNotification() {
	}

	public ReaderEventNotification(MessageHeader header, ReaderEventNotificationData data) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.READER_EVENT_NOTIFICATION);
		this.readerEventNotificationData = data;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public ReaderEventNotificationData getReaderEventNotificationData() {
		return readerEventNotificationData;
	}

	public void setReaderEventNotificationData(ReaderEventNotificationData readerEventNotificationData) {
		this.readerEventNotificationData = readerEventNotificationData;
	}

	@Override
	public String toString() {
		return "ReaderEventNotification [messageHeader=" + messageHeader + ", readerEventNotificationData=" + readerEventNotificationData + "]";
	}
}