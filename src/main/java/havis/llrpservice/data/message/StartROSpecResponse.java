package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class StartROSpecResponse implements Message {

	private static final long serialVersionUID = -7221200930071499028L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public StartROSpecResponse() {
	}

	public StartROSpecResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.START_ROSPEC_RESPONSE);
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
		return "StartROSpecResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}