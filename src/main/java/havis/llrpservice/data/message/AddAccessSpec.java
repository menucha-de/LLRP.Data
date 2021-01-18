package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.AccessSpec;

public class AddAccessSpec implements Message {

	private static final long serialVersionUID = -5027797535307946056L;

	private MessageHeader messageHeader;
	private AccessSpec accessSpec;

	public AddAccessSpec() {
	}

	public AddAccessSpec(MessageHeader messageHeader, AccessSpec accessSpec) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ADD_ACCESSSPEC);
		this.accessSpec = accessSpec;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public AccessSpec getAccessSpec() {
		return accessSpec;
	}

	public void setAccessSpec(AccessSpec accessSpec) {
		this.accessSpec = accessSpec;
	}

	@Override
	public String toString() {
		return "AddAccessSpec [messageHeader=" + messageHeader + ", accessSpec=" + accessSpec + "]";
	}
}