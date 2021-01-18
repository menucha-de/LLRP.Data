package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

public class SetProtocolVersion implements Message {

	private static final long serialVersionUID = 3451164423176456803L;

	private MessageHeader messageHeader;
	private ProtocolVersion protocolVersion;

	public SetProtocolVersion() {
	}

	public SetProtocolVersion(MessageHeader header, ProtocolVersion protocolVersion) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.SET_PROTOCOL_VERSION);
		this.protocolVersion = protocolVersion;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public ProtocolVersion getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(ProtocolVersion protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	@Override
	public String toString() {
		return "SetProtocolVersion [messageHeader=" + messageHeader + ", protocolVersion=" + protocolVersion + "]";
	}
}