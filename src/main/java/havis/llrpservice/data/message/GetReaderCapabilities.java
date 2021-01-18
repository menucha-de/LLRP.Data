package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.Custom;

import java.util.List;

public class GetReaderCapabilities implements Message {

	private static final long serialVersionUID = 2610265020922303641L;

	private MessageHeader messageHeader;
	private List<Custom> customExtensionPoint;
	private GetReaderCapabilitiesRequestedData requestedData;

	public GetReaderCapabilities() {
	}

	public GetReaderCapabilities(MessageHeader header, GetReaderCapabilitiesRequestedData requestedData) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.GET_READER_CAPABILITIES);
		this.requestedData = requestedData;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public GetReaderCapabilitiesRequestedData getRequestedData() {
		return requestedData;
	}

	public void setRequestedData(GetReaderCapabilitiesRequestedData requestedData) {
		this.requestedData = requestedData;
	}

	public List<Custom> getCustomExtensionPoint() {
		return customExtensionPoint;
	}

	public void setCustomExtensionPoint(List<Custom> customExtensionPoint) {
		this.customExtensionPoint = customExtensionPoint;
	}

	@Override
	public String toString() {
		return "GetReaderCapabilities [messageHeader=" + messageHeader + ", customExtensionPoint=" + customExtensionPoint + ", requestedData=" + requestedData
				+ "]";
	}
}