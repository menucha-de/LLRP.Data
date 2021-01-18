package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class DisableAccessSpecResponse implements Message {

	private static final long serialVersionUID = -8728710464401684734L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public DisableAccessSpecResponse() {
	}

	public DisableAccessSpecResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.DISABLE_ACCESSSPEC_RESPONSE);
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
		return "DisableAccessSpecResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}