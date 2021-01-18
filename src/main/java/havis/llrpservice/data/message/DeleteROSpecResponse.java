package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class DeleteROSpecResponse implements Message {

	private static final long serialVersionUID = 576624698161233441L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public DeleteROSpecResponse() {
	}

	public DeleteROSpecResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.DELETE_ROSPEC_RESPONSE);
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
		return "DeleteROSpecResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}