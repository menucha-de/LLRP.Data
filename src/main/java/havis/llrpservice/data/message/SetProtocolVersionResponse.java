package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class SetProtocolVersionResponse implements Message {

	private static final long serialVersionUID = -5329938065313006085L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public SetProtocolVersionResponse() {
	}

	public SetProtocolVersionResponse(MessageHeader header, LLRPStatus status) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.SET_PROTOCOL_VERSION_RESPONSE);
		this.status = status;

	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public LLRPStatus getStatus() {
		return status;
	}

	public void setStatus(LLRPStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SetProtocolVersionResponse [messageHeader=" + messageHeader + ", llrpStatus=" + status + "]";
	}
}