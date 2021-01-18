package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class ErrorMessage implements Message {

	private static final long serialVersionUID = 5063966996720648131L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public ErrorMessage() {
	}

	public ErrorMessage(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ERROR_MESSAGE);
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
		return "ErrorMessage [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}