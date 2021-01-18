package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class EnableROSpecResponse implements Message {

	private static final long serialVersionUID = -6181623527048736194L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public EnableROSpecResponse() {
	}

	public EnableROSpecResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ENABLE_ROSPEC_RESPONSE);
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
		return "EnableROSpecResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}