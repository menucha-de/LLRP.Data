package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

import java.util.Arrays;

public class CustomMessage implements Message {

	private static final long serialVersionUID = -1732307815362187445L;

	private MessageHeader messageHeader;
	private long vendorIdentifier;
	private short messageSubType;
	private byte[] vendorPayload;

	public CustomMessage() {
	}

	public CustomMessage(MessageHeader messageHeader, long vendorIdentifier, short messageSubType, byte[] vendorPayload) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.CUSTOM_MESSAGE);
		this.vendorIdentifier = vendorIdentifier;
		this.messageSubType = messageSubType;
		this.vendorPayload = vendorPayload;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public long getVendorIdentifier() {
		return vendorIdentifier;
	}

	public void setVendorIdentifier(long vendorIdentifier) {
		this.vendorIdentifier = vendorIdentifier;
	}

	public short getMessageSubType() {
		return messageSubType;
	}

	public void setMessageSubType(short messageSubType) {
		this.messageSubType = messageSubType;
	}

	public byte[] getVendorPayload() {
		return vendorPayload;
	}

	public void setVendorPayload(byte[] vendorPayload) {
		this.vendorPayload = vendorPayload;
	}

	@Override
	public String toString() {
		return "CustomMessage [messageHeader=" + messageHeader + ", vendorIdentifier=" + vendorIdentifier + ", messageSubType=" + messageSubType
				+ ", vendorPayload=" + Arrays.toString(vendorPayload) + "]";
	}
}