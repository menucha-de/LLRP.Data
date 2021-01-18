package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class SetReaderConfigResponse implements Message {

	private static final long serialVersionUID = 6944056070777546566L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public SetReaderConfigResponse() {
	}

	public SetReaderConfigResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.SET_READER_CONFIG_RESPONSE);
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
		return "SetReaderConfigResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}