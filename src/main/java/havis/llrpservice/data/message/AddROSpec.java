package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.ROSpec;

public class AddROSpec implements Message {

	private static final long serialVersionUID = 6419104326189136493L;

	private MessageHeader messageHeader;
	private ROSpec roSpec;

	public AddROSpec() {
	}

	public AddROSpec(MessageHeader messageHeader, ROSpec roSpec) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.ADD_ROSPEC);
		this.roSpec = roSpec;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public ROSpec getRoSpec() {
		return roSpec;
	}

	public void setRoSpec(ROSpec roSpec) {
		this.roSpec = roSpec;
	}

	@Override
	public String toString() {
		return "AddROSpec [messageHeader=" + messageHeader + ", roSpec=" + roSpec + "]";
	}
}