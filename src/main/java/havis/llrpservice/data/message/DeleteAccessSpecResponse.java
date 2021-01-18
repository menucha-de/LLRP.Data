package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class DeleteAccessSpecResponse implements Message {

	private static final long serialVersionUID = -5307599475089326345L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public DeleteAccessSpecResponse() {
	}

	public DeleteAccessSpecResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.DELETE_ACCESSSPEC_RESPONSE);
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
		return "DeleteAccessSpecResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}