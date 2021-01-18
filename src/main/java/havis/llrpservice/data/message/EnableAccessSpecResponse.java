package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class EnableAccessSpecResponse implements Message {

	private static final long serialVersionUID = 4487025923462482410L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public EnableAccessSpecResponse() {
	}

	public EnableAccessSpecResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ENABLE_ACCESSSPEC_RESPONSE);
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
		return "EnableAccessSpecResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}