package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class CloseConnectionResponse implements Message {

	private static final long serialVersionUID = -1000930729291384037L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public CloseConnectionResponse() {
	}

	public CloseConnectionResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.CLOSE_CONNECTION_RESPONSE);
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
		return "CloseConnectionResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}