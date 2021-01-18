package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class AddROSpecResponse implements Message {

	private static final long serialVersionUID = -2992078404118732463L;

	private MessageHeader messageHeader;
	private LLRPStatus status;

	public AddROSpecResponse() {
	}

	public AddROSpecResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ADD_ROSPEC_RESPONSE);
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
		return "AddROSpecResponse [messageHeader=" + messageHeader + ", status=" + status + "]";
	}
}