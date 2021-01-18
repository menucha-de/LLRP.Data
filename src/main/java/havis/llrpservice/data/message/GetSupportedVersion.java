package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class GetSupportedVersion implements Message {

	private static final long serialVersionUID = 4776447640850889718L;

	private MessageHeader messageHeader;

	public GetSupportedVersion() {
	}

	public GetSupportedVersion(MessageHeader header) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.GET_SUPPORTED_VERSION);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "GetSupportedVersion [messageHeader=" + messageHeader + "]";
	}
}