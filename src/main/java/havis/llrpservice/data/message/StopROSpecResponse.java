package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class StopROSpecResponse implements Message {

	private static final long serialVersionUID = 1054553035242301522L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public StopROSpecResponse() {
	}

	public StopROSpecResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.STOP_ROSPEC_RESPONSE);
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
		return "StopROSpecResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}